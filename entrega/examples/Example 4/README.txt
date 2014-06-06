Neste teste podemos verificar novamente uma expressão regular relativamente complexa e com elementos não utilizados no example3, tais como:

quantificador sem limite máximo: 
	exemplo - "fim"{2,}
. (qualquer identificador): 
	exemplo - .{300}
* (0 ou mais vezes)
	exemplo - "child2array"*

A expressão regular usada foi:
//@cflow start "create".{300}("child1"|"child2"){100}"child1array"{0,100}"child2array"*"fim"{2,}

Para fazer a contra-prova da execução desta RE, pode-se alterar para:
//@cflow start "create".{299}("child1"|"child2"){100}"child1array"{0,100}"child2array"*"fim"{2,}
			  ^	
		   valor alterado

e verificar que a execução falha.


Neste exemplo podemos reparar que uma caraterística interessante a implementar nesta ferramenta seria a criação de macros dinâmicas.
Por exemplo, em vez de assumirmos que "child1array" e "child2array" apareceriam até 100 vezes cada um, seria interessante captar o resultado das passagens em "child1array" e, consequentemente, saber quantas vezes passaria em "child2array".
Ou seja, se "child1array" tem 20 transições neste caso, saberíamos que "child2array" teria de transitar 80 vezes, devido ao array conter 100 elementos.


Para testar, correr o batch file example4.