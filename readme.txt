payment-tracker
---------------
need java 1.8 or higher

RUN:

java -jar payment-tracker.jar

COMMAND LINE ARGUMENTS:

 -?         help message
 -e         echo, print input data to output
 -f <arg>   payment input file
 -q         quit after read file
 -x <arg>   exchange rates file

BUILD:

mvn clean package
cp target/payment-tracker-0.1-SNAPSHOT-jar-with-dependencies.jar ./payment-tracker.jar

RUN WITH EXAMPLE EXCHANGES:

java -jar payment-tracker.jar -x examples/usd.exchanges

file, with currency and inversion exchange rate to USD,
one record on line, record format:

CZK 0.123456
AUD 0.123456
...

UNKNOWN CURRENCY CODE:

append into file: currency.properties

one record on line, record format:

RM=RMB,999,2
...

property format and information:
http://docs.oracle.com/javase/8/docs/api/java/util/Currency.html


