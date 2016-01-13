:: run.bat
:: 
:: starts league of legends spectator client
::
@ECHO OFF
ECHO.                 running from jar
java -jar "target\uberjar\curler-0.1.0-SNAPSHOT-standalone.jar" %league_api_key% $1