**PROJECT TITLE: 
	"Cflow"

**GROUP: 
	NAME1: Francisco Miguel Amaro Maciel  | NR1: 201100692 | GRADE: 18 | CONTR: 25%
	NAME2: Hugo Miguel Ribeiro de Sousa   | NR2: 201100690 | GRADE: 18 | CONTR: 25%
	NAME3: Manuel Carlos Correia Mesquita | NR3: 201302812 | GRADE: 17 | CONTR: 25%
	NAME4: Ricardo Daniel Soares da Silva | NR4: 201108043 | GRADE: 18 | CONTR: 25%

**SUMMARY: 	
	O Cflow � uma ferramenta que permite fazer o controlo de fluxo da execu��o de um programa.
	Indicando o ponto de in�cio e uma express�o regular, � poss�vel verificar se esse fluxo � executado, ou n�o, com sucesso.
	Esta ferramenta gera internamente um aut�mato (DFA na sua fase final). 
	A ferramenta substitui o c�digo da aplica��o que se pretende executar, atrav�s de macros reconhecidas.
	Esta inicialmente est� implementada para ficheiros Java, podendo ser posteriormente adaptada para outras linguagens.

**DEALING WITH SYNTACTIC ERRORS: 
	A express�o regular a ser usada pela ferramenta � inserida no ficheiro .java a analisar ap�s a macro "//@cflow start".
	Exemplo: "//@cflow start "a""b"{2,3}". Os identificadores come�am por uma letra, sendo constitu�dos por letras e n�meros. Um identificador deve ser definido entre aspas.

	Caso haja erros sint�ticos, a aplica��o avisa o utilizador que a express�o regular usada est� errada. Assim sendo, o utilizador ter� de a modificar no ficheiro pretendido.
	Sendo a express�o introduzida atrav�s do ficheiro, n�o haveria outra forma de avisar o utilizador. Caso fosse por input, seria poss�vel pedir para corrigir a express�o at� que esta estivesse correta.

**SEMANTIC ANALYSIS:
	� de notar que todos os identificadores usados nos pontos de controlo da ferramenta, devem tamb�m estar especificados na REGEX a ser analisada, caso contr�rio haver� um erro devido a uma transi��o inv�lida no DFA.

	Exemplo:
	//@cflow start "a""b"

	...
	//@cflow a
	...
	//@cflow c  <-- Aqui ocorrer� um erro, pois este identificador n�o � sequer reconhecido pela REGEX, n�o havendo transi��es com este identificador no DFA.

**INTERMEDIATE REPRESENTATIONS (IRs):
	A �rvore concreta foi, obviamente, convertida para uma AST. 
	Esta �rvore agrupa os elementos de uma forma sistem�tica e consistente para uma mais f�cil interpreta��o e consequente transforma��o em NFA mais tarde.
	Exemplo: a express�o regular ("a"|"b")"c" gera a seguinte AST:
			Sequence
				Union
					Identifier->a
					Identifier->b
				Identifier->c	
	ou seja, � uma sequ�ncia de uma uni�o de "a" e "b" com "c".
	
	A partir desta AST, � convertido para um E-NFA. Este � posteriormente convertido para um DFA. 
	Ambas as representa��es s�o constru�das com o recurso da biblioteca externa JUNG, que facilita o uso e constru��o de grafos.

**CODE GENERATION:
	� efetuado um pr�-processamento que substitui todas as macros reconhecidas pela nossa ferramenta, pelas respetivas fun��es.
	As macros reconhecidas s�o:
		1) //@cflow start <regex>
		2) //@cflow <identificador>  
		3) //@cflow @debug 
		3) //@cflow @finish

	A macro 1) indica a regex que se pretende vericar o fluxo. Apenas uma express�o est� a ser executada a cada momento.
	No entanto, esta macro pode ser chamada m�ltiplas vezes, fazendo "reset" � express�o anterior.

	A macro 2) identifica a passagem por um ponto de controlo do programa, identificada pelo respetivo identifcador que pode ser usado na express�o regular.

	A macro 3) permite executar em modo debug a partir do ponto especificado. Neste modo, podemos visualizar prints que nos indicam as passagens pelos diferentes estados do DFA, e consequentemente, ver onde falha a execu��o, por exemplo.

	A macro 4) permite indicar um ponto onde se pretende parar a execu��o do DFA. Neste ponto, � verificado se o estado atual do DFA � um estado final e retornando a respetiva resposta ao utilizador.

**OVERVIEW:
	Para facilitar o uso da nossa aplica��o, foi criado um batch file base, que executa os 3 passos necess�rios de forma mais r�pida e c�mda, sendo os seguintes:
	1) fazer o pr�-processamento de um ficheiro ou pasta indicados. Os ficheiros .java s�o processados, sendo substitu�das as macros por c�digo da nossa aplica��o. Os restantes ficheiros e diret�rios s�o simplesmente replicados para uma pasta "cflow" com a mesma estrutura da pasta original.
	2) compilar o c�digo gerado juntamente com o c�digo da nossa aplica��o (.jar).
	3) executar o main pretendido e verificar se o controlo de execu��o � sucedido ou n�o.

	Este batch file necessita de 3 par�metros, sendo executado da seguinte forma:
	cflow.bat %1 %2 %3
	
	%1: pasta que o utilizador pretende verificar o controlo
	%2: diretorio do ficheiro .java com o main que se pretende executar
	%3: igual ao ponto %2, mas substituindo "/" por "." e removendo o ".java" final. Corresponde ao ".class" que ser� executado.
	Por exemplo o ficheiro "snippet/ExampleProgram.java" no par�metro %2 deve ser chamado como "snippet.ExampleProgram" no par�metro %3.
	
	Nota: Este batch file base funciona apenas para projetos com 1 package. Para projetos com v�rios packages , � necess�rio replicar as pastas do argumento %2. 
	� poss�vel ver um caso semelhante a este, em que o batch file foi alterado, no example 5.
	
	Os exemplos providenciados mostram exemplos de execu��o funcionais da ferramenta. 
	Na fase de pr�-processamento, s�o tamb�m verificados e tratados erros como � o caso de uso de identificadores inv�lidos ou de express�es regulares inv�lidas.
	Caso seja o caso, o programa p�ra a execu��o no pr�-processamento e as 2 fases seguintes n�o s�o executadas, sendo o utilizador advertido para este facto.		

	Foram usado algoritmos j� existentes na convers�o de E-NFA para DFA.
	
**TESTSUITE AND TEST INFRASTRUCTURE:
	Foram realizados testes unit�rios que incidem, essencialmente, na an�lise sint�tica. 
	Estes testam os elementos do sub-set da PCRE implementada pela ferramenta.
	Esta an�lise � importante, pois falhando, compromete o resto da atividade da aplica��o, ou seja, n�o � poss�vel a transforma��o para NFA/DFA e consequente controlo do fluxo.

**TASK DISTRIBUTION:
	A primeira fase de an�lise lexical, sint�tica, gram�tica da REGEX foi feita pelos 4 membros de igual forma, assim como a representa��o interm�dia.
	A partir daqui foi poss�vel repartir um pouco mais o trabalho.
	O Ricardo e o Francisco focaram-se mais na convers�o da representa��o interm�dia para NFA/DFA, enquanto que o Manuel e o Hugo se focaram mais na constru��o de testes unit�rios, handling de erros e pr�-processamento dos ficheiros.
	Na fase mais final, a constru��o de exemplos e maior valida��o da implementa��o foi executada, mais uma vez, de forma repartida pelos 4 elementos do grupo.
	
**PROS:
	Ferramenta que pode, em alguns casos, ser realmente �til para testar a cobertura de c�digo.
	Os quantificadores (exemplo "a"{5,6} ou "a"{5} ou "a"{5,}) podem ser bastante �teis na cobertura de c�digo.
	
**CONS:
	Dificuldade na sua utiliza��o, como a configura��o do batch file.
	Em casos simples de cobertura de c�digo, simples "prints" tornam-se mais c�modos e f�ceis de usar.
	Adaptada apenas para a linguagem de programa��o Java.
	Seria interessante a possibilidade de criar identificadores dinamicamente, como referido no example 4.

