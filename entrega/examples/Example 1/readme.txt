Correr o exemplo:
(3 alternativas)

>example1

>cflow src main\ExampleProgram.java main.ExampleProgram

> java -jar cflow.jar src 
> javac -cp "cflow.jar" cflow\main\ExampleProgram.java 
> java -cp "cflow;cflow.jar" main.ExampleProgram


Com este exemplo pretendemos demonstrar as funcionalidades mais basicas da nossa ferramente, 
com um programa que � apenas de exemplo e n�o tem qualquer fim defenido.


A expressao regular que usamos para este exemplo replica o que ocorre a n�vel de c�digo.

Regex: "constructor"{2}"success"?"forloop"{0,10}("big" | "small")


O construtor � chamado duas vezes. O sucesso � opcional pois depende de um random entre 0 e 1, o
ciclo for � executado 10 vezes, e assim fica confirmado que funciona. Dependendo do valor total, � usado um "or" para ver me que if o programa entra