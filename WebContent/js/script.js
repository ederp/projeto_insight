let contadorCampos = 2;

function calcularMarcacoes() {
const dadosHorarios = [];
for (let i = 1; i <= 3; i++) {
	const entrada = $(`#horarioEntrada${i}`).val();
	const saida = $(`#horarioSaida${i}`).val();
	if(entrada !== "" && saida !== ""){
		dadosHorarios.push({ entrada, saida });
	}
}

const dadosMarcacoes = [];
for (let i = 1; i < contadorCampos; i++) {
	const entrada = $(`#marcacaoEntrada${i}`).val();
	const saida = $(`#marcacaoSaida${i}`).val();
	if(entrada !== "" && saida !== ""){
		dadosMarcacoes.push({ entrada, saida });
	}
}

// Envia os dados para o servidor usando AJAX
$.ajax({
	url: 'http://localhost:8080/insight/Tabela',
	type: 'POST',
	contentType: 'application/json',
	data: JSON.stringify({ horarios: dadosHorarios, marcacoes: dadosMarcacoes }),
	success: function (resultado) {
		// Atualiza o conteúdo de resultadoMarcacoesFeitas com os dados recebidos
		$('#resultadoMarcacoesFeitas').html(resultado);
	}
});
}

function validarFormato(input) {
	const formatoValido = /^([01]\d|2[0-3]):([0-5]\d)$/.test(input.value);
	const smallElement = $(`#${input.id}Help`);
	if (!formatoValido){
		smallElement.text('Formato inválido. Use HH:MM.');
	}
	else{
		smallElement.text('');
	}
}

function verificarCamposPreenchidos() {
    // Verifica se há pelo menos um campo de horário de trabalho preenchido
    const horariosTrabalhoPreenchidos = $('[id^="horario"]').filter((index, element) => {
        const entrada = $(`#horarioEntrada${element.id.slice(-1)}`).val();
        const saida = $(`#horarioSaida${element.id.slice(-1)}`).val();
        return (entrada !== "" && entrada !== undefined) && (saida !== "" && saida !== undefined);
    }).length > 0;

    // Verifica se não há nenhum campo de marcação em branco
    const marcacoesPreenchidas = $('[id^="marcacao"]').filter((index, element) => {
        const entrada = $(`#marcacaoEntrada${element.id.slice(-1)}`).val();
        const saida = $(`#marcacaoSaida${element.id.slice(-1)}`).val();
        return entrada === "" || saida === "";
    }).length === 0;

    // Desabilita o botão se alguma condição não for atendida
    $('#calcularMarcacoes').prop('disabled', !(horariosTrabalhoPreenchidos && marcacoesPreenchidas));
}

//Função para formatar a entrada de hora adicionando ":" automaticamente
function formatarHoraInput(input) {
    let valorAtual = input.value.replace(':', ''); // Remove qualquer ':'
    valorAtual = valorAtual.replace(/\D/g, ''); // Remove caracteres não numéricos

    // Limita a entrada para apenas números e até 4 caracteres
    if (/^\d{0,4}$/.test(valorAtual)) {
        if (valorAtual.length >= 2) {
            input.value = valorAtual.substring(0, 2) + (valorAtual.length === 4 ? ':' : '') + valorAtual.substring(2);
        } else {
            input.value = valorAtual;
        }
    }
}

// Aplique a função aos campos de entrada de hora
$(document).on('input', '.form-control', function() {
    formatarHoraInput(this);
});

// Corrige o problema de 'backspace' removendo o ':' automaticamente
$(document).on('keydown', '.form-control', function(e) {
    if (e.key === 'Backspace') {
        let valorAtual = this.value.replace(':', '');
        if (valorAtual.length > 1) {
            this.value = valorAtual.substring(0, valorAtual.length - 1);
            formatarHoraInput(this);
        } else {
            this.value = '';
        }
        e.preventDefault();
    }
});

$(document).ready(function () {
	verificarCamposPreenchidos();
	function adicionarCampo() {
		const novoCampo = `
			<div class="row">
				<div class="form-group mt-2 col-2">
					<label>
						Entrada 
						<input type="text" class="form-control" id="marcacaoEntrada${contadorCampos}" placeholder="HH:MM">
						<small id="marcacaoEntrada${contadorCampos}Help" class="form-text text-danger"></small>
					</label>
					
				</div>
				<div class="form-group mt-2 col-2">
					<label>
						Saída
						<input type="text" class="form-control" id="marcacaoSaida${contadorCampos}" placeholder="HH:MM">
						<small id="marcacaoSaida${contadorCampos}Help" class="form-text text-danger"></small>
					</label>	
				</div>
			</div>
		`;
		$('#camposExtras').append(novoCampo);
		contadorCampos++;
	}
	
	// Botão de limpar todas as entradas
	$('#limparEntradas').on('click', function () {
		// Limpa os campos de horário de trabalho
		$('[id^="horario"]').val('');
		// Limpa os campos de marcação
		$('[id^="marcacao"]').val('');
		// Limpa o resultado exibido
		$('#resultadoHorarioTrabalho').html('');
		$('#resultadoMarcacoesFeitas').html('');
		// Remove os campos adicionais de marcação
		$('#camposExtras').empty();
		contadorCampos = 2;
	});
	
	// Botão de limpar somente as marcações
	$('#limparMarcacoes').on('click', function () {
		// Limpa os campos de marcação
		$('[id^="marcacao"]').val('');
		// Limpa o resultado exibido
		$('#resultadoMarcacoesFeitas').html('');
		// Remove os campos adicionais de marcação
		$('#camposExtras').empty();
		contadorCampos = 2;
	});
	
	$('#adicionarMarcacao').on('click', adicionarCampo);
	$('#calcularMarcacoes').on('click', calcularMarcacoes);
	$('.form-control').on('blur', function () {
		validarFormato(this);
		verificarCamposPreenchidos();
	});
});