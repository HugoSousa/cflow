java -jar cflow.jar %1
if not errorlevel 0 (exit /b %errorlevel%)
javac -cp "cflow.jar" cflow\%2 
::location of main (.java)
java -cp "cflow;cflow.jar" %3  
::location of main (.class)
