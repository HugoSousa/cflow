Neste teste podemos verificar o correto funcionamento de uma expres�o regular um pouco mais elaborada:
//@cflow start ( ("A"("A1"|"A2"|"A3"|"A4")+("loop"{5,6}"return"?)) | ("B""loop"{5}("loop"|"return")) )

Podemos verificar que se a alterarmos, por exemplo para:
//@cflow start ( ("A"("A1"|"A2"|"A3"|"A4")+("loop"{5,6}"return"?)) | ("B""loop"{4}("loop"|"return")) )
										^
									alterando este valor

o cflow vai retornar erro em alguns casos.
Aqui s�o testados v�rios elementos pertencentes ao subset da PCRE implementado pela aplica��o, tais como:
+, ?, |, quantificadores, grupos.

Para testar, correr o batch file example3.