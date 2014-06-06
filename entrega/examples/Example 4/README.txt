Neste teste podemos verificar novamente uma express�o regular relativamente complexa e com elementos n�o utilizados no example3, tais como:

quantificador sem limite m�ximo: 
	exemplo - "fim"{2,}
. (qualquer identificador): 
	exemplo - .{300}
* (0 ou mais vezes)
	exemplo - "child2array"*

A express�o regular usada foi:
//@cflow start "create".{300}("child1"|"child2"){100}"child1array"{0,100}"child2array"*"fim"{2,}

Para fazer a contra-prova da execu��o desta RE, pode-se alterar para:
//@cflow start "create".{299}("child1"|"child2"){100}"child1array"{0,100}"child2array"*"fim"{2,}
			  ^	
		   valor alterado

e verificar que a execu��o falha.


Neste exemplo podemos reparar que uma carater�stica interessante a implementar nesta ferramenta seria a cria��o de macros din�micas.
Por exemplo, em vez de assumirmos que "child1array" e "child2array" apareceriam at� 100 vezes cada um, seria interessante captar o resultado das passagens em "child1array" e, consequentemente, saber quantas vezes passaria em "child2array".
Ou seja, se "child1array" tem 20 transi��es neste caso, saber�amos que "child2array" teria de transitar 80 vezes, devido ao array conter 100 elementos.


Para testar, correr o batch file example4.