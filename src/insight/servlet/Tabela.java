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
		// Recuperar os dados da requisição AJAX
	    String requestBody = request.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);

	    // Processar esses dados
	    ObjectMapper objectMapper = new ObjectMapper();
	    DadosMarcacoes dados = objectMapper.readValue(requestBody, DadosMarcacoes.class);

	    // Calcular atraso e hora extra
	    String resultadoAtraso = calcularAtraso(dados.getHorarios(), dados.getMarcacoes());
	    String resultadoHoraExtra = calcularHoraExtra(dados.getHorarios(), dados.getMarcacoes());

	    // Enviar a resposta de volta para o cliente
	    response.setContentType("text/html;charset=UTF-8");
	    PrintWriter out = response.getWriter();
	    out.println("<html><body>");
	    out.println("<h2>Resultados:</h2>");
	    out.println("<p>Atraso: " + resultadoAtraso + "</p>");
	    out.println("<p>Hora Extra: " + resultadoHoraExtra + "</p>");
	    out.println("</body></html>");
	}

	private String calcularHoraExtra(List<HorarioTrabalho> horarios, List<Marcacao> marcacoes) {
		// TODO Auto-generated method stub
		StringBuilder resultadoHoraExtra = new StringBuilder("Hora extra:<br>");

        for (Marcacao marcacao : marcacoes) {
            for (HorarioTrabalho horario : horarios) {
                Duration diff = calcularDiferenca(horario.getEntrada(), horario.getSaida(), marcacao.getEntrada(), marcacao.getSaida());

                if (diff.compareTo(Duration.ZERO) > 0) {
                    resultadoHoraExtra.append(formatarPeriodo(diff)).append("<br>");
                }
            }
        }

        return resultadoHoraExtra.toString();
	}

	private String calcularAtraso(List<HorarioTrabalho> horarios, List<Marcacao> marcacoes) {
		// TODO Auto-generated method stub
		StringBuilder resultadoAtraso = new StringBuilder("Atraso:<br>");

        for (Marcacao marcacao : marcacoes) {
            for (HorarioTrabalho horario : horarios) {
                Duration diff = calcularDiferenca(marcacao.getEntrada(), marcacao.getSaida(), horario.getEntrada(), horario.getSaida());

                if (diff.compareTo(Duration.ZERO) > 0) {
                    resultadoAtraso.append(formatarPeriodo(diff)).append("<br>");
                }
            }
        }

        return resultadoAtraso.toString();
	}
	
	private Duration calcularDiferenca(LocalTime inicio1, LocalTime fim1, LocalTime inicio2, LocalTime fim2) {
        LocalTime maxInicio = inicio1.isAfter(inicio2) ? inicio1 : inicio2;
        LocalTime minFim = fim1.isBefore(fim2) ? fim1 : fim2;

        return Duration.between(maxInicio, minFim);
    }

    private String formatarPeriodo(Duration periodo) {
        long horas = periodo.toHours();
        long minutos = periodo.toMinutesPart();

        return String.format("%02d:%02d - %02d:%02d", horas, minutos, horas + periodo.toHoursPart(), minutos + periodo.toMinutesPart());
    }

}
