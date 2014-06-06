Neste exemplo, podemos verificar que � poss�vel executar a ferramenta em projetos com v�rios packages.
No entanto, o batch file teve de ser alterado para conseguir compilar os ficheiros de diferentes pastas.

Ou seja, esta linha:
javac -cp "cflow.jar" cflow\%2 

passou a:
javac -cp "cflow.jar" cflow\%2 cflow\%3 cflow\%4

sendo este um cuidado que o utilizador ter� de tomar aquando da execu��o da ferramenta.

Este foi um projeto realizado para outra unidade curricular, sendo um jogo de tabuleiro.
Para executar rapidamente at� finalizar, sugere-se que se v� executando a jogada "0" quando pedido o input do utilizador.


A express�o regular usada foi:
//@cflow start ("play""show""minimax"+"move"+"switchturn""nowinner")+("play""show""minimax"+"move"+"switchturn""winner")

Desta forma, comprovamos o fluxo desejado na aplica��o, ou seja, mostrando tamb�m a verdadeira funcionalidade pr�tica desta aplica��o:
o in�cio da jogada come�a por mostrar o tabuleiro, executando o algoritmo minimax, que entra diversas vezes na sua fun��o de c�lculo, seguido de v�rios movimentos que constituem uma jogada.
Segue-se a troca de turno e n�o havendo um vencedor, volta-se ao ciclo inicial. Havendo um vencedor, o jogo acaba.

Para testar o exemplo, deve correr-se o batch file example5.