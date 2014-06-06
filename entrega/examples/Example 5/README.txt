Neste exemplo, podemos verificar que é possível executar a ferramenta em projetos com vários packages.
No entanto, o batch file teve de ser alterado para conseguir compilar os ficheiros de diferentes pastas.

Ou seja, esta linha:
javac -cp "cflow.jar" cflow\%2 

passou a:
javac -cp "cflow.jar" cflow\%2 cflow\%3 cflow\%4

sendo este um cuidado que o utilizador terá de tomar aquando da execução da ferramenta.

Este foi um projeto realizado para outra unidade curricular, sendo um jogo de tabuleiro.
Para executar rapidamente até finalizar, sugere-se que se vá executando a jogada "0" quando pedido o input do utilizador.


A expressão regular usada foi:
//@cflow start ("play""show""minimax"+"move"+"switchturn""nowinner")+("play""show""minimax"+"move"+"switchturn""winner")

Desta forma, comprovamos o fluxo desejado na aplicação, ou seja, mostrando também a verdadeira funcionalidade prática desta aplicação:
o início da jogada começa por mostrar o tabuleiro, executando o algoritmo minimax, que entra diversas vezes na sua função de cálculo, seguido de vários movimentos que constituem uma jogada.
Segue-se a troca de turno e não havendo um vencedor, volta-se ao ciclo inicial. Havendo um vencedor, o jogo acaba.

Para testar o exemplo, deve correr-se o batch file example5.