# projeto_insight
Sistema web que gerencia horas extras e atrasos dado um horário de trabalho informado pelo usuário.

Seguem alguns dos requisitos: 

Uma tela com os seguintes componentes:
 
 1 – Criar uma tabela que liste “Horário de trabalho”, com dois campos tipo caracter de tamanho 5 e formato HH:MM, chamados entrada e saída, que pode receber até 3 registros. Coloque os componentes necessários para se cadastrar entrada e saída, (edits, buttons, dataTable e etc) e visualizá-los na tela.   
 
2 - Criar uma tabela que lista “Marcações Feitas”, com dois campos tipo caracter de tamanho 5 e formato HH:MM, chamados entrada e saída, que pode receber até n registros. Coloque os componentes necessários para se cadastrar entrada e saída, (edits, buttons, dataTable e etc) e visualizá-los na tela.

3 – Tabela que liste os cálculos de “Atraso” com os componentes necessários para se visualizar seus registros na tela, com a mesma estrutura das demais tabelas.

4 - Tabela que liste os cálculos de “Hora extra” com os componentes necessários para se visualizar seus registros na tela, com a mesma estrutura dos demais Tabelas.
 
Crie uma função de subtração entre os registros as tabelas do padrão entrada e saída.

Exemplo:
 Tabela-Horario De Trabalho:
    registros:
      08:00  12:00
 
 Tabela-Marcacoes Feitas:
   registros:
     07:00 11:00
    
Se chamarmos a função de subtração passando como parâmetro 1 os registros da Tabela-Horario De Trabalho e 2 os registros da Tabela-Marcacoes Feitas, a função tem que retornar os períodos da Tabela 1 menos o da tabela 2.
 
Seria assim: das 08:00 às 12:00 eu devo tirar das 07:00 às 11:00, portanto fica no primeiro período 11:00 às 12:00
 
Vamos analisar, 11:00 às 12:00 seria o período que o funcionário não cumpriu, então seria o atraso. Se chamarmos a função de subtração passando como parâmetro 1 os registros da Tabela-Marcacoes Feitas e o 2 os registros da Tabela-Horario De Trabalho, a função tem que retornar o período da Tabela 1 menos o do 2.
 
Seria assim: das 07:00 às 11:00 eu devo tirar das 08:00 às 12:00, portanto fica no primeiro período 07:00 às 08:00

Vamos analisar, 07:00 às 08:00 seria o período que o funcionário trabalhou fora do horário dele, então seria o hora extra. Esses dois exemplos foram de 1 registro de marcações e 1 registro de horários, mas pode-se ter n registros de marcações e até 3 registro de horários e a função tem que atender.

Linguagens e frameworks utilizados:

Front-end/web: HTML, Bootstrap e JavaScript/JQuery
Back-end: Java 13 e servidor Tomcat v10.0.
