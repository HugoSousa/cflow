Para testar correr o batch file example1.

Com este exemplo pretendemos demonstrar as funcionalidades mais b�sicas da nossa ferramenta, 
com um programa que � apenas de exemplo e n�o tem qualquer fim definido.

A express�o regular que usamos para este exemplo replica o que ocorre a n�vel de c�digo.

Regex: "constructor"{2}"success"?"forloop"{0,10}("big" | "small")

O construtor � chamado duas vezes. O sucesso � opcional pois depende de um random entre 0 e 1, o
ciclo for � executado 10 vezes, e assim fica confirmado que funciona. 
Dependendo do valor total, � usado um "or" para ver em que if o programa entra.