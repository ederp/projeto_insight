$(function() {
    let contadorCampos = 2;

    function adicionarCampo() {
        const novoCampo = `
            <div class="form-group mt-2 col-1">
                <label for="entrada${contadorCampos}Marcacoes">Entrada ${contadorCampos}</label>
                <input type="text" class="form-control" id="entrada${contadorCampos}Marcacoes" placeholder="HH:MM">
				<small id="marcacao$Help" class="form-text text-danger"></small>
            </div>
            <div class="form-group mt-2 col-1">
                <label for="saida${contadorCampos}Marcacoes">Saída ${contadorCampos}</label>
                <input type="text" class="form-control" id="saida${contadorCampos}Marcacoes" placeholder="HH:MM">
				<small id="marcacao$Help" class="form-text text-danger"></small>
            </div>
        `;
        $('#camposExtras').append(novoCampo);
        contadorCampos++;
    }
	
    function calcularMarcacoes() {
        // Implemente a lógica para calcular a diferença entre marcações e horário de trabalho
        // Atualize o conteúdo de resultadoMarcacoesFeitas
    }

    $('#adicionarMarcacao').on('click', adicionarCampo);
});

$(document).ready(function() {
    $('.form-control').on('blur', function() {
        validarFormato(this);
    });
});

function validarFormato(input) {
    const formatoValido = /^([01]\d|2[0-3]):([0-5]\d)$/.test(input.value);

    const smallElement = $(`#${input.id}Help`);
    if (!formatoValido) {
        smallElement.text('Formato inválido. Use HH:MM.');
    } else {
        smallElement.text('');
    }
}

