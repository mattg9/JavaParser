cd "%~dp0"

rd /S /Q build
md build

javac -source 11 -target 11 -sourcepath src -cp ".;lib\*" -d build src\RecordMerger.java

del /q cantest.jar
jar cvf cantest.jar -C build\ .

del /q veeva_solution.zip
jar cvfM veeva_solution.zip .

@pause

