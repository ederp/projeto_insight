package insight.model;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class HorarioTrabalho{
	
	private LocalTime entrada;
    private LocalTime saida;
    
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
    
	public HorarioTrabalho() {
		super();
		// TODO Auto-generated constructor stub
	}
		
	public HorarioTrabalho(LocalTime entrada, LocalTime saida) {
		super();
		this.entrada = entrada;
		this.saida = saida;
	}

	public LocalTime getEntrada() {
		return entrada;
	}
	
	public void setEntrada(LocalTime entrada) {
		this.entrada = entrada;
	}
	
	public void setEntrada(String entrada) {
		this.entrada = this.parseLocalTime(entrada);
	}
	
	public LocalTime getSaida() {
		return saida;
	}
	
	public void setSaida(LocalTime saida) {
		this.saida = saida;
	}
	
	public void setSaida(String saida) {
		this.saida = this.parseLocalTime(saida);
	}
	
	private LocalTime parseLocalTime(String timeString) {
        return (timeString != null && !timeString.isEmpty()) ? LocalTime.parse(timeString, dtf) : null;
    }
}
