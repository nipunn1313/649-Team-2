#!/bin/bash

# This script must be run in the unit_test/ folder. The path to the code and
# the path where the results directory is created are fully specified as
# arguments. This script also assumes the existence of run_test.sh and
# test_verify.sh in the same directory as the script.

# TODO Provide option to *not* create a results directory

if [[ $# -ne 2 ]]
then
  echo "Usage: ./`basename $0` codepath resultsbase"
  exit 1
fi

errorcolor=$(echo -e "\033[0;31m")
nocolor=$(echo -e "\033[0m")
errorstring=${errorcolor}ERROR:${nocolor}

codepath="$1"
resultsbase="$2"
scriptpath=`dirname $0`
testrepeat=10

# Iterate until you find a unit_results# folder that doesn't exist
resultsnum=1
while true; do
  if [[ ! -d "$resultsbase/unit_results$resultsnum" ]]
  then
    resultspath="$resultsbase/unit_results$resultsnum"
    mkdir -p "$resultspath"
    break
  fi
  resultsnum=`expr $resultsnum + 1`
done

$scriptpath/test_verify.sh unit_tests.txt &> /dev/null
if [[ $? -ne 0 ]]
then
  echo "$errorstring test_verify.sh exited with errors"
fi
cat unit_tests.txt | grep -v -e "^;" | while read controller cf mf; do
  if [[ "$controller" == "" ]]; then
    continue
  fi
  for i in {1..$testrepeat}
  do
    resultsfile=`echo "$mf" | sed -e "s/\.mf/\.stats/"`
    $scriptpath/run_test.sh "$codepath" "$cf" "$mf" \
      "$resultspath/$resultsfile" &> /dev/null
    testresult=$?

    if [[ $testresult -ne 0 ]]
    then
      echo "$errorstring $controller failed unit test ($cf, $mf)"
      break
    fi
  done
  if [[ $testresult -eq 0 ]]
  then
    echo "$controller passed unit test ($cf, $mf)"
  fi
done

