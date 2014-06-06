**PROJECT TITLE: 
	"Cflow"

**GROUP: 
	NAME1: Francisco Miguel Amaro Maciel  | NR1: 201100692 | GRADE: 18 | CONTR: 25%
	NAME2: Hugo Miguel Ribeiro de Sousa   | NR2: 201100690 | GRADE: 18 | CONTR: 25%
	NAME3: Manuel Carlos Correia Mesquita | NR3: 201302812 | GRADE: 17 | CONTR: 25%
	NAME4: Ricardo Daniel Soares da Silva | NR4: 201108043 | GRADE: 18 | CONTR: 25%

**SUMMARY: 	
	O Cflow é uma ferramenta que permite fazer o controlo de fluxo da execução de um programa.
	Indicando o ponto de início e uma expressão regular, é possível verificar se esse fluxo é executado, ou não, com sucesso.
	Esta ferramenta gera internamente um autómato (DFA na sua fase final). 
	A ferramenta substitui o código da aplicação que se pretende executar, através de macros reconhecidas.
	Esta inicialmente está implementada para ficheiros Java, podendo ser posteriormente adaptada para outras linguagens.

**DEALING WITH SYNTACTIC ERRORS: 
	A expressão regular a ser usada pela ferramenta é inserida no ficheiro .java a analisar após a macro "//@cflow start".
	Exemplo: "//@cflow start "a""b"{2,3}". Os identificadores começam por uma letra, sendo constituídos por letras e números. Um identificador deve ser definido entre aspas.

	Caso haja erros sintáticos, a aplicação avisa o utilizador que a expressão regular usada está errada. Assim sendo, o utilizador terá de a modificar no ficheiro pretendido.
	Sendo a expressão introduzida através do ficheiro, não haveria outra forma de avisar o utilizador. Caso fosse por input, seria possível pedir para corrigir a expressão até que esta estivesse correta.

**SEMANTIC ANALYSIS:
	É de notar que todos os identificadores usados nos pontos de controlo da ferramenta, devem também estar especificados na REGEX a ser analisada, caso contrário haverá um erro devido a uma transição inválida no DFA.

	Exemplo:
	//@cflow start "a""b"

	...
	//@cflow a
	...
	//@cflow c  <-- Aqui ocorrerá um erro, pois este identificador não é sequer reconhecido pela REGEX, não havendo transições com este identificador no DFA.

**INTERMEDIATE REPRESENTATIONS (IRs):
	A árvore concreta foi, obviamente, convertida para uma AST. 
	Esta árvore agrupa os elementos de uma forma sistemática e consistente para uma mais fácil interpretação e consequente transformação em NFA mais tarde.
	Exemplo: a expressão regular ("a"|"b")"c" gera a seguinte AST:
			Sequence
				Union
					Identifier->a
					Identifier->b
				Identifier->c	
	ou seja, é uma sequência de uma união de "a" e "b" com "c".
	
	A partir desta AST, é convertido para um E-NFA. Este é posteriormente convertido para um DFA. 
	Ambas as representações são construídas com o recurso da biblioteca externa JUNG, que facilita o uso e construção de grafos.

**CODE GENERATION:
	É efetuado um pré-processamento que substitui todas as macros reconhecidas pela nossa ferramenta, pelas respetivas funções.
	As macros reconhecidas são:
		1) //@cflow start <regex>
		2) //@cflow <identificador>  
		3) //@cflow @debug 
		3) //@cflow @finish

	A macro 1) indica a regex que se pretende vericar o fluxo. Apenas uma expressão está a ser executada a cada momento.
	No entanto, esta macro pode ser chamada múltiplas vezes, fazendo "reset" à expressão anterior.

	A macro 2) identifica a passagem por um ponto de controlo do programa, identificada pelo respetivo identifcador que pode ser usado na expressão regular.

	A macro 3) permite executar em modo debug a partir do ponto especificado. Neste modo, podemos visualizar prints que nos indicam as passagens pelos diferentes estados do DFA, e consequentemente, ver onde falha a execução, por exemplo.

	A macro 4) permite indicar um ponto onde se pretende parar a execução do DFA. Neste ponto, é verificado se o estado atual do DFA é um estado final e retornando a respetiva resposta ao utilizador.

**OVERVIEW:
	Para facilitar o uso da nossa aplicação, foi criado um batch file base, que executa os 3 passos necessários de forma mais rápida e cómda, sendo os seguintes:
	1) fazer o pré-processamento de um ficheiro ou pasta indicados. Os ficheiros .java são processados, sendo substituídas as macros por código da nossa aplicação. Os restantes ficheiros e diretórios são simplesmente replicados para uma pasta "cflow" com a mesma estrutura da pasta original.
	2) compilar o código gerado juntamente com o código da nossa aplicação (.jar).
	3) executar o main pretendido e verificar se o controlo de execução é sucedido ou não.

	Este batch file necessita de 3 parâmetros, sendo executado da seguinte forma:
	cflow.bat %1 %2 %3
	
	%1: pasta que o utilizador pretende verificar o controlo
	%2: diretorio do ficheiro .java com o main que se pretende executar
	%3: igual ao ponto %2, mas substituindo "/" por "." e removendo o ".java" final. Corresponde ao ".class" que será executado.
	Por exemplo o ficheiro "snippet/ExampleProgram.java" no parâmetro %2 deve ser chamado como "snippet.ExampleProgram" no parâmetro %3.
	
	Nota: Este batch file base funciona apenas para projetos com 1 package. Para projetos com vários packages , é necessário replicar as pastas do argumento %2. 
	É possível ver um caso semelhante a este, em que o batch file foi alterado, no example 5.
	
	Os exemplos providenciados mostram exemplos de execução funcionais da ferramenta. 
	Na fase de pré-processamento, são também verificados e tratados erros como é o caso de uso de identificadores inválidos ou de expressões regulares inválidas.
	Caso seja o caso, o programa pára a execução no pré-processamento e as 2 fases seguintes não são executadas, sendo o utilizador advertido para este facto.		

	Foram usado algoritmos já existentes na conversão de E-NFA para DFA.
	
**TESTSUITE AND TEST INFRASTRUCTURE:
	Foram realizados testes unitários que incidem, essencialmente, na análise sintática. 
	Estes testam os elementos do sub-set da PCRE implementada pela ferramenta.
	Esta análise é importante, pois falhando, compromete o resto da atividade da aplicação, ou seja, não é possível a transformação para NFA/DFA e consequente controlo do fluxo.

**TASK DISTRIBUTION:
	A primeira fase de análise lexical, sintática, gramática da REGEX foi feita pelos 4 membros de igual forma, assim como a representação intermédia.
	A partir daqui foi possível repartir um pouco mais o trabalho.
	O Ricardo e o Francisco focaram-se mais na conversão da representação intermédia para NFA/DFA, enquanto que o Manuel e o Hugo se focaram mais na construção de testes unitários, handling de erros e pré-processamento dos ficheiros.
	Na fase mais final, a construção de exemplos e maior validação da implementação foi executada, mais uma vez, de forma repartida pelos 4 elementos do grupo.
	
**PROS:
	Ferramenta que pode, em alguns casos, ser realmente útil para testar a cobertura de código.
	Os quantificadores (exemplo "a"{5,6} ou "a"{5} ou "a"{5,}) podem ser bastante úteis na cobertura de código.
	
**CONS:
	Dificuldade na sua utilização, como a configuração do batch file.
	Em casos simples de cobertura de código, simples "prints" tornam-se mais cómodos e fáceis de usar.
	Adaptada apenas para a linguagem de programação Java.
	Seria interessante a possibilidade de criar identificadores dinamicamente, como referido no example 4.

