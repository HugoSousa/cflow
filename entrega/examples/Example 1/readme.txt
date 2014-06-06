Para testar correr o batch file example1.

Com este exemplo pretendemos demonstrar as funcionalidades mais básicas da nossa ferramenta, 
com um programa que é apenas de exemplo e não tem qualquer fim definido.

A expressão regular que usamos para este exemplo replica o que ocorre a nível de código.

Regex: "constructor"{2}"success"?"forloop"{0,10}("big" | "small")

O construtor é chamado duas vezes. O sucesso é opcional pois depende de um random entre 0 e 1, o
ciclo for é executado 10 vezes, e assim fica confirmado que funciona. 
Dependendo do valor total, é usado um "or" para ver em que if o programa entra.