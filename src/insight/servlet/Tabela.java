package insight.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import insight.model.DadosMarcacoes;
import insight.model.HorarioTrabalho;
import insight.model.Marcacao;
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
		String resultadoAtraso = null;
	    String resultadoHoraExtra = null;
		// Recuperar os dados da requisição AJAX
	    String requestBody = request.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);
	    System.out.println(requestBody);
	    // Processar esses dados
	    ObjectMapper objectMapper = new ObjectMapper();
	    DadosMarcacoes dados = objectMapper.readValue(requestBody, DadosMarcacoes.class);
	    
	    //se a marcação corresponde ao horário de trabalho, não há atrasos nem horas extras;
	    if (dados.saoIguais()) {
	    	resultadoAtraso = "      -      ";
		    resultadoHoraExtra = "      -      ";
	    }
	    //caso em que a marcação é menor do que o horário de trabalho: haverá somente atrasos
	    else if (dados.calcularIntervalo() < 0) {
	    	resultadoAtraso = calcularAtraso(dados.getHorarios(), dados.getMarcacoes());
	    	resultadoHoraExtra = "      -      ";
	    }
	    //marcação é maior do que o horário de trabalho: haverá somente horas extras
	    else if (dados.calcularIntervalo() > 0) {
	    	resultadoAtraso = "      -      ";
	    	resultadoHoraExtra = calcularHoraExtra(dados.getHorarios(), dados.getMarcacoes());
	    }
	 // Calcular atraso e hora extra
	    else {
	    	resultadoAtraso = calcularAtraso(dados.getHorarios(), dados.getMarcacoes());
		    resultadoHoraExtra = calcularHoraExtra(dados.getHorarios(), dados.getMarcacoes());
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

	private String calcularHoraExtra(List<HorarioTrabalho> horarios, List<Marcacao> marcacoes) {
		// TODO Auto-generated method stub
		StringBuilder resultadoHoraExtra = new StringBuilder();

        for (Marcacao marcacao : marcacoes) {
            for (HorarioTrabalho horario : horarios) {
            	int entradas = this.calcularIntervalos(marcacao.getEntrada(), horario.getEntrada());
                int saidas = this.calcularIntervalos(horario.getSaida(), marcacao.getSaida());
                
                if (entradas < 0) {
                	resultadoHoraExtra.append(marcacao.getEntrada())
                    .append(" - ")
                    .append(horario.getEntrada())
                    .append("<br>");
                }
                
                if (saidas < 0) {
                	resultadoHoraExtra.append(horario.getSaida())
                    .append(" - ")
                    .append(marcacao.getSaida())
                    .append("<br>");
                }
            }
        }

        return resultadoHoraExtra.toString();
	}

	private String calcularAtraso(List<HorarioTrabalho> horarios, List<Marcacao> marcacoes) {
		// TODO Auto-generated method stub
		StringBuilder resultadoAtraso = new StringBuilder();

        for (Marcacao marcacao : marcacoes) {
            for (HorarioTrabalho horario : horarios) {
                int entradas = this.calcularIntervalos(horario.getEntrada(), marcacao.getEntrada());
                int saidas = this.calcularIntervalos(marcacao.getSaida(), horario.getSaida());
                
                if (entradas < 0) {
                    resultadoAtraso.append(horario.getEntrada())
                    .append(" - ")
                    .append(marcacao.getEntrada())
                    .append("<br>");
                }
                
                if (saidas < 0) {
                    resultadoAtraso.append(marcacao.getSaida())
                    .append(" - ")
                    .append(horario.getSaida())
                    .append("<br>");
                }
            }
        }

        return resultadoAtraso.toString();
	}
	
	private Integer calcularIntervalos(LocalTime inicio1, LocalTime inicio2) {
        return inicio1.compareTo(inicio2);
    }
}
