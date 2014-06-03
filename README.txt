Caso generico

java -jar cflow.jar src -o scr_transformado

javac -cp "<DIRETORIO_DO_JAR\NOME_DO_JAR.jar>" scr_transformado\<MAIN_FILE.java>

cd src_transformado

java -cp ".;<DIRETORIO_DO_JAR\NOME_DO_JAR.jar>" <package.main.mainclass>


///---------------------------------------------------------------
Caso especifico:

:projeto
        :cflow.jar
	:bin 
		:snippet
			:ExampleProgram.java
	:src
    	    	:snippet
	           	:ExampleProgram.java
	:cflow
		:src
			:snippet
				:ExampleProgram.java
     



C:\...\projeto> java -jar run.jar src [-o src_cflow]

C:\...\projeto> javac -cp "run.jar" src_cflow\snippet\ExampleProgram.java

C:\...\projeto> java -cp "src_cflow;run.jar" snippet.ExampleProgram

