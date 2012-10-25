#!/bin/bash

# Must be run in portfolio/acceptance_test.
# NOTE: This is just a quick hacky beta script so that we can run with
# different seeds, it's not ready to be included in the makefile or any
# checking scripts.

testfiles=`ls *.pass`

for testfile in $testfiles
do
  for i in {1..5}
  do
    java -cp ../../code simulator.framework.Elevator -pf $testfile -head head.txt
  done
done
