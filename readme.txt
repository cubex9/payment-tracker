Payment tracker
---------------

RUN:

java -jar payment-tracker.jar

RUN WITH EXAMPLE EXCHANGES:

java -jar payment-tracker.jar -x examples/usd.exchanges

COMMAND LINE ARGUMENTS:

 -?         help message
 -e         echo, print input data to output
 -f <arg>   payment input file
 -q         quit after read file
 -x <arg>   exchange rates file

BUILD:

mvn clean package
mv target/payment-tracker-0.1-SNAPSHOT-jar-with-dependencies.jar ./payment-tracker.jar

UNKNOWN CURRENCY CODE:

append into: currency.properties

property format and information: http://docs.oracle.com/javase/8/docs/api/java/util/Currency.html


