java -jar cflow.jar %1
javac -cp "cflow.jar" cflow\%2 
::location of main (.java)
java -cp "cflow;cflow.jar" %3  
::location of main (.class)
