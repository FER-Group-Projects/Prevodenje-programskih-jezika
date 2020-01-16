#!/bin/bash
CLASSPATH=../../../target/classes
WORKING_DIRECTORY=src/main/java
TEST_EXAMPLES=../../test/resources/test_examples
NODE_DIRECTORY=../../../../node_modules/friscjs/consoleapp

cd $WORKING_DIRECTORY

find $TEST_EXAMPLES/{integration,rest} -mindepth 1 -maxdepth 1 -type d | sort --numeric-sort | while read line
do
    echo "Evaluating $line"
    echo " - generating a.frisc"

    rm --force $CLASSPATH/a.frisc

    java -classpath $CLASSPATH GeneratorKoda < $line/test.in || exit 1

    echo " - evaluating a.frisc"

    timeout 5 node $NODE_DIRECTORY/frisc-console.js $CLASSPATH/a.frisc
done
