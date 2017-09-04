payment-tracker
---------------

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
mv target/payment-tracker-0.1-SNAPSHOT-jar-with-dependencies.jar ./payment-tracker.jar

RUN WITH EXAMPLE EXCHANGES:

java -jar payment-tracker.jar -x examples/usd.exchanges

one record on line, record format:
CZK 0.123456
AUD 0.123456
...

UNKNOWN CURRENCY CODE:

append into: currency.properties

one record on line, rcord format:
RM=RMB,999,2
...

property format and information:
http://docs.oracle.com/javase/8/docs/api/java/util/Currency.html


