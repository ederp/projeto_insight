package insight.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.ObjectMapper;

import insight.model.DadosMarcacoes;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Tabela
 */
@WebServlet("/Tabela")
public class Tabela extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public Tabela() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String resultadoAtraso = "";
	    String resultadoHoraExtra = "";
		// Recuperar os dados da requisição AJAX
	    String requestBody = request.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);
	    // Processar esses dados
	    ObjectMapper objectMapper = new ObjectMapper();
	    DadosMarcacoes dados = objectMapper.readValue(requestBody, DadosMarcacoes.class);
	    
	    List<LocalTime> horariosOrdenados = this.todosHorarios(dados);
	    List<LocalTime> listaMarcacoes = new ArrayList<>();
        List<LocalTime> listaHorariosTrabalho = new ArrayList<>();
        
        dados.getMarcacoes().stream().forEach(marcacao -> {
        	listaMarcacoes.add(marcacao.getEntrada());
			listaMarcacoes.add(marcacao.getSaida());
        });
               
        dados.getHorarios().stream().forEach(horarioTrabalho -> {
        	listaHorariosTrabalho.add(horarioTrabalho.getEntrada());
			listaHorariosTrabalho.add(horarioTrabalho.getSaida());
        });
        
        //para os casos em que uma marcação seja superior a todos os horários de trabalho
        boolean marcacaoMaiorQueHorariosDeTrabalho = dados.getMarcacoes().size() == 1 &&
        		dados.getHorarios().size() > 1 && 
        		dados.getIntervaloMarcacoes() >= dados.getIntervaloHorariosTrabalho();    
        LocalTime primeiraMarcacao = dados.getMarcacoes().get(0).getEntrada();
        LocalTime ultimaMarcacao = dados.getMarcacoes().get(0).getSaida();
        
	    for (int i = 0; i < horariosOrdenados.size() - 1; i+=2) {

	    	LocalTime atual = horariosOrdenados.get(i);
            LocalTime proximo = horariosOrdenados.get(i + 1);
            
            boolean marcacaoEntreHorarioDeSaidaEEntrada = dados.getHorarios().stream()
            		.anyMatch(ht -> ht.getSaida().equals(atual) && ht.getEntrada().equals(proximo)) && 
            		dados.getMarcacoes().stream()
            		.anyMatch(m -> m.getEntrada().equals(atual) && m.getSaida().equals(proximo));

            boolean marcacaoAntesHorarioEntrada = (dados.getMarcacoes().stream()
            		.anyMatch(m -> m.getEntrada().equals(atual)) && 
            		dados.getHorarios().stream()
            		.anyMatch(ht -> ht.getEntrada().equals(proximo))) || 
            		(i == 0 && listaMarcacoes.contains(atual) &&
        					listaMarcacoes.contains(proximo));
            
            boolean marcacaoPosHorarioEntrada = dados.getHorarios().stream()
            		.anyMatch(ht -> ht.getEntrada().equals(atual)) && 
            		dados.getMarcacoes().stream()
            		.anyMatch(m -> m.getEntrada().equals(proximo)) && 
            		! marcacaoEntreHorarioDeSaidaEEntrada || 
            		(i == 0 && listaHorariosTrabalho.contains(atual) &&
        					listaHorariosTrabalho.contains(proximo));
            
            boolean marcacaoAntesHorarioSaida = dados.getMarcacoes().stream()
            		.anyMatch(m -> m.getSaida().equals(atual)) && 
            		dados.getHorarios().stream()
            		.anyMatch(ht -> ht.getSaida().equals(proximo)) &&
            		! marcacaoEntreHorarioDeSaidaEEntrada;
            
            boolean marcacaoPosHorarioSaida = (dados.getHorarios().stream()
            		.anyMatch(ht -> ht.getSaida().equals(atual)) && 
            		dados.getMarcacoes().stream()
            		.anyMatch(m -> m.getSaida().equals(proximo))) || 
            		(i == horariosOrdenados.size() - 2 &&
        					listaMarcacoes.contains(atual) &&
        					listaMarcacoes.contains(proximo));
            
            boolean horariosTrabalhoEntreMarcacoes = marcacaoMaiorQueHorariosDeTrabalho &&
            		listaHorariosTrabalho.contains(atual) &&
            		listaHorariosTrabalho.contains(proximo) &&
            		horariosOrdenados.indexOf(atual) > horariosOrdenados.indexOf(primeiraMarcacao) &&
            		horariosOrdenados.indexOf(proximo) < horariosOrdenados.indexOf(ultimaMarcacao);
            
            boolean condicaoHoraExtra = (marcacaoAntesHorarioEntrada || marcacaoPosHorarioSaida ||
            		horariosTrabalhoEntreMarcacoes) && 
            		!(marcacaoPosHorarioEntrada || marcacaoAntesHorarioSaida);
            
            if(!atual.equals(proximo)) {
            	if(condicaoHoraExtra) {
            		resultadoHoraExtra += retornarHoraExtra(atual, proximo);
            	}
            	else {
            		resultadoAtraso += retornarAtraso(atual, proximo);
            	}
            }
		}

	    // Enviar a resposta de volta para o cliente
	    response.setContentType("text/html;charset=UTF-8");
	    PrintWriter out = response.getWriter();
	    out.println("<html><body>");
	    out.println("<div class=\"mt-4\">");
	    out.println("<h2>Resultados:</h2>");
	    out.println("<p>Atraso: </p>");
	    out.println("<p>" + resultadoAtraso + "</p>");
	    out.println("<p>Hora extra: </p>");
	    out.println("<p>" + resultadoHoraExtra + "</p>");
	    out.println("</div>");
	    out.println("</body></html>");
	}

	private String retornarHoraExtra(LocalTime horario1, LocalTime horario2) {
		// TODO Auto-generated method stub
		StringBuilder resultadoHoraExtra = new StringBuilder();
		
		resultadoHoraExtra.append(horario1)
        .append(" - ")
        .append(horario2)
        .append("<br>");
        return resultadoHoraExtra.toString();
	}

	private String retornarAtraso(LocalTime horario1, LocalTime horario2) {
		// TODO Auto-generated method stub
		StringBuilder resultadoAtraso = new StringBuilder();

		resultadoAtraso.append(horario1)
        .append(" - ")
        .append(horario2)
        .append("<br>");
        return resultadoAtraso.toString();
	}
	
	private List<LocalTime> todosHorarios(DadosMarcacoes dadosMarcacoes) {
	    Function<LocalTime, Integer> periodoClassifier = horario -> {
	        int hora = horario.getHour();
	        if (hora >= 0 && hora < 6) {
	            // Madrugada
	            return 0;
	        } else if (hora >= 6 && hora < 12) {
	            // Manhã
	            return 1;
	        } else if (hora >= 12 && hora < 18) {
	            // Tarde
	            return 2;
	        } else {
	            // Noite
	            return 3;
	        }
	    };
	    
	    boolean turnoMadrugada = dadosMarcacoes.getHorarios()
	    		.get(0)
	    		.getEntrada()
	    		.isAfter(LocalTime.of(18, 59)) && 
	    		dadosMarcacoes.getHorarios()
	    		.get(0)
	    		.getSaida()
	    		.isAfter(LocalTime.MIDNIGHT);

	    Map<Integer, List<LocalTime>> horariosAgrupados = dadosMarcacoes.getHorarios().stream()
	            .flatMap(horarioTrabalho -> Stream.of(horarioTrabalho.getEntrada(), horarioTrabalho.getSaida()))
	            .collect(Collectors.groupingBy(horario -> periodoClassifier.apply(horario),
	                    Collectors.mapping(Function.identity(), Collectors.toList())));

	    dadosMarcacoes.getMarcacoes().stream()
	            .flatMap(marcacao -> Stream.of(marcacao.getEntrada(), marcacao.getSaida()))
	            .collect(Collectors.groupingBy(horario -> periodoClassifier.apply(horario),
	                    Collectors.mapping(Function.identity(), Collectors.toList())))
	            .forEach((key, value) -> horariosAgrupados.merge(key, value, (list1, list2) -> {
	                list1.addAll(list2);
	                return list1;
	            }));

	    List<LocalTime> todosHorariosOrdenados = horariosAgrupados.values().stream()
	            .flatMap(List::stream)
	            .sorted((horario1, horario2) -> {
	                int periodo1 = periodoClassifier.apply(horario1);
	                int periodo2 = periodoClassifier.apply(horario2);

	                if (periodo1 != periodo2) {
	                	//Significa que é o turno da madrugada
	                	if(turnoMadrugada && periodo2 < periodo1 && (periodo2 == 0 || periodo2 == 1) && periodo1 == 3) {
	                		return -1;
	                	} else {
	                		return Integer.compare(periodo1, periodo2);
	                	}
	                } else {
	                    return horario1.compareTo(horario2);
	                }
	            })
	            .collect(Collectors.toList());

	    return todosHorariosOrdenados;
	}
}
