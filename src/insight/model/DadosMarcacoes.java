package insight.model;

import java.util.List;

public class DadosMarcacoes {

	private List<Marcacao> marcacoes;
	private List<HorarioTrabalho> horarios;

	public List<Marcacao> getMarcacoes() {
		return marcacoes;
	}

	public void setMarcacoes(List<Marcacao> marcacoes) {
		this.marcacoes = marcacoes;
	}

	public List<HorarioTrabalho> getHorarios() {
		return horarios;
	}

	public void setHorarios(List<HorarioTrabalho> horarios) {
		this.horarios = horarios;
	}
	
	public Integer getIntervaloMarcacoes() {
		Integer intervalos = 0;
		for (Marcacao marcacao : this.marcacoes) {
			intervalos += marcacao.getIntervalo();
		}
		return intervalos;
	}
	
	public Integer getIntervaloHorariosTrabalho() {
		Integer intervalos = 0;
		for (HorarioTrabalho ht : this.horarios) {
			intervalos += ht.getIntervalo();
		}
		return intervalos;
	}

}
