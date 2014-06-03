Caso generico

java -jar cflow.jar src -o scr_transformado

javac -cp "<DIRETORIO_DO_JAR\NOME_DO_JAR.jar>" scr_transformado\<MAIN_FILE.java>

cd src_transformado

java -cp ".;<DIRETORIO_DO_JAR\NOME_DO_JAR.jar>" <package.main.mainclass>


///---------------------------------------------------------------
Caso especifico:

:projeto
        :cflow.jar
	:src
    	    :snippet
	           :ExampleProgram.java
     

C:\...\projeto> java -jar src -o src_cflow

C:\...\projeto> javac -cp "run.jar" src_cflow\snippet\ExampleProgram.java

C:\...\projeto> cd src_cflow

C:\...\projeto> java -cp ".;..\run.jar" snippet.ExampleProgram

