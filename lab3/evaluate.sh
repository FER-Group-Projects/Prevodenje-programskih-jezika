#!/bin/bash
CLASSPATH=../../../target/classes
WORKING_DIRECTORY=src/main/java
TEST_EXAMPLES=../../test/resources/test_examples

cd $WORKING_DIRECTORY

find $TEST_EXAMPLES/{tests-1-2012,tests-2-1231} -mindepth 1 -maxdepth 1 -type d | while read line
do
    echo "Evaluating $line"

    echo " - running program through semantic analysis"

    java -classpath $CLASSPATH SemantickiAnalizator < $line/test.in > $line/actual_output || exit 1

    echo " - comparing results"

    diff --side-by-side --ignore-blank-lines $line/actual_output $line/test.out || exit 1
done
