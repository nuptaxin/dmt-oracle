@echo off
cd %~dp0..
echo  dmt-oracle(Java)
@title dmt-oracle
setLocal EnableDelayedExpansion
set CLASSPATH="conf
for /f "tokens=* delims=" %%a in ('dir "*.jar" /b') do (
   set CLASSPATH=!CLASSPATH!;%%a
)
set CLASSPATH=!CLASSPATH!"
echo %CLASSPATH%
java -Xmx512m -Xmn256m -Djava.ext.dirs=lib -Dlog4j.configuration=log4j.properties -cp %CLASSPATH% org.renix.dmt.oracle.DataMigrationTool
