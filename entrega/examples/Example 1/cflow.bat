java -jar cflow.jar %1
if not errorlevel 0 (e  xit /b %errorlevel%)
find cflow -name "*.java" -print | xargs javac -cp "cflow.jar"
java -cp "cflow;cflow.jar" %2  
::location of main (.class)
