#!/bin/bash

# Must be run in portfolio/acceptance_test.
# NOTE: This is just a quick hacky beta script so that we can run with
# different seeds, it's not ready to be included in the makefile or any
# checking scripts.

errorcolor=$(echo -e "\033[0;31m")
nocolor=$(echo -e "\033[0m")
errorstring=${errorcolor}Failed!${nocolor}

date=`date +%m-%d`
file="results-$date.csv"
log='test, seed, stranded, emergencies,'
if [ ! -f $file ]; then
  echo $log >> $file
fi

testfiles=`ls *.pass`
name=
seed=
stranded=
emergency=
mkdir -p logs
for testfile in $testfiles
do
    name=`echo $testfile | cut -d . -f 1`
    for i in {0..10}
    do
      seed=$RANDOM
      echo -n "running test $name with seed $seed..."
      java -cp ../../code simulator.framework.Elevator -pf normal_final.pass -head head.txt -seed $seed -b 200 -fs 5 -rt 2h &> "logs/$name-$seed.log"
      grep -i "stranded: 0" logs/$name-$seed.log > /dev/null
      if [ $? -eq 0 ]
      then
        echo "Passed!"
      else
        echo "$errorstring"
      fi
      stranded=`grep -i "stranded" logs/$name-$seed.log`
      emergency=`grep -i "emergency" logs/$name-$seed.log`
      echo "$name,$seed,$stranded,$emergency" >> $file
    done
done

mkdir -p stats-$date
mv elevator*.stats stats-$date/
