#!/bin/bash
exit 0 # TODO: remove this line when project is ready
CLASSPATH=../../../target/classes
WORKING_DIRECTORY=src/main/java
TEST_EXAMPLES=../../test/resources/test_examples

cd $WORKING_DIRECTORY

ls $TEST_EXAMPLES | while read line
do
    echo "Evaluating $line"
    echo " - generating language grammar rules"

    java -classpath $CLASSPATH GSA < $TEST_EXAMPLES/$line/test.san || exit 1

    echo " - running program through syntax analysis"

    java -classpath $CLASSPATH analizator.SA < $TEST_EXAMPLES/$line/test.in > $TEST_EXAMPLES/$line/actual_output || exit 1

    echo " - comparing results"

    diff --side-by-side --ignore-blank-lines $TEST_EXAMPLES/$line/actual_output $TEST_EXAMPLES/$line/test.out || exit 1
done
