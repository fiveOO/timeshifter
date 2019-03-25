@echo off
setlocal
set TIMESHIFTER=..\..\target\timeshifter-0.0.1-SNAPSHOT.jar

echo.
echo -- Timezone taken from given data
echo.
java -jar %TIMESHIFTER% -i example.csv -is 1

echo.
echo -- Timezone given on command line
echo.
java -jar %TIMESHIFTER% -i example.csv -is 1 -zo +03:00

echo.
echo Change output timestamp format
echo.
java -jar %TIMESHIFTER% -i example.csv -is 1 -od "dd. MMMM yyyy HH:mm:ss XXX"

echo.

java -Duser.country=CA -Duser.language=fr -jar %TIMESHIFTER% -i example.csv -is 1 -od "dd. MMMM yyyy HH:mm:ss XXX"

echo.
echo Change output line format
echo.
java -jar %TIMESHIFTER% -i example.csv -is 1 -oh "<ul>%%n" -ol "<li><a href=""%%1$s"">Image %%1$s</a> taken on %%4$s</li>%%n" -of "</ul>"

echo.

java -jar %TIMESHIFTER% @exampleHtml.param
