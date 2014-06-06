java -jar cflow.jar %1
if not errorlevel 0 (e  xit /b %errorlevel%)
javac -cp "cflow.jar" cflow\%2 cflow\%3 cflow\%4
::location of main (.java)
java -cp "cflow;cflow.jar" %5  
::location of main (.class)
