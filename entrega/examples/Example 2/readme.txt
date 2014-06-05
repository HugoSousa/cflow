Correr o exemplo:
(3 alternativas)

>example2

>cflow src main.ExampleProgram

>java -jar cflow.jar src
>find cflow -name "*.java" -print | xargs javac -cp "cflow.jar"
>java -cp "cflow;cflow.jar" logic.Super 


Com este exemplo podemos confirmar a utilidade de analisar quais das sub-classes estao guardadas no Array e sao executadas.
Assim podemos analisar todos os elementos do array e confirmar que nenhum deles é da classe Dog pois isto lançaria um erro no Cflow