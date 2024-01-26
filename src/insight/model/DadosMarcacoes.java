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
	
	public Integer calcularIntervalo() {
		Integer intervaloHorarioTrabalho = 0, intervaloMarcacao = 0;
		for (HorarioTrabalho horarioTrabalho : this.horarios) {
			intervaloHorarioTrabalho += horarioTrabalho.getIntervalo();
		}
		
		for (Marcacao marcacao : this.marcacoes) {
			intervaloMarcacao += marcacao.getIntervalo();
		}
		
        return intervaloMarcacao - intervaloHorarioTrabalho;
    }
	
	public boolean saoIguais() {
		if(marcacoes.size() == horarios.size()) {
			for (int i = 0; i < marcacoes.size(); i++) {
				if(! marcacoes.get(i).getEntrada().equals(horarios.get(i).getEntrada()) &&
						! marcacoes.get(i).getSaida().equals(horarios.get(i).getSaida())	) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
}
