@echo off
cd %~dp0..
echo  OracleExpImpTool(Java)
@title OracleExpImpTool
setLocal EnableDelayedExpansion
set CLASSPATH="conf
for /f "tokens=* delims=" %%a in ('dir "*.jar" /b') do (
   set CLASSPATH=!CLASSPATH!;%%a
)
set CLASSPATH=!CLASSPATH!"
echo %CLASSPATH%
java -Xmx1424m -Xmn512m -Djava.ext.dirs=lib -Dlog4j.configuration=log4j.properties -cp %CLASSPATH% org.renzx.oracle.exptool.ToolApp
