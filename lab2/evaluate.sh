#!/bin/bash
CLASSPATH=../../../target/classes
WORKING_DIRECTORY=src/main/java
TEST_EXAMPLES=../../test/resources/test_examples

cd $WORKING_DIRECTORY

find $TEST_EXAMPLES/{f2backup,integration} -mindepth 1 -maxdepth 1 -type d | while read line
do
    echo "Evaluating $line"
    echo " - generating language grammar rules"

    java -classpath $CLASSPATH GSA < $line/test.san || exit 1

    echo " - running program through syntax analysis"

    java -classpath $CLASSPATH analizator.SA < $line/test.in > $line/actual_output || exit 1

    echo " - comparing results"

    diff --side-by-side --ignore-blank-lines $line/actual_output $line/test.out || exit 1
done
