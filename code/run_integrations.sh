#!/bin/bash

# This script must be run in the integration_test/ folder. The path to the
# code and the path where the results directory is created are fully specified
# as arguments. This script also assumes the existence of run_test.sh and
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

# Iterate until you find a integration_results# folder that doesn't exist
resultsnum=1
while true; do
  if [[ ! -d "$resultsbase/integration_results$resultsnum" ]]
  then
    resultspath="$resultsbase/integration_results$resultsnum"
    mkdir -p "$resultspath"
    break
  fi
  resultsnum=`expr $resultsnum + 1`
done

$scriptpath/test_verify.sh integration_tests.txt &> /dev/null
if [[ $? -ne 0 ]]
then
  echo "$errorstring test_verify.sh exited with errors"
fi
cat integration_tests.txt | grep -v -e "^;" | while read controller cf mf; do
  if [[ "$controller" == "" ]]; then
    continue
  fi
  for i in {1..$testrepeat}
  do
    resultsfile=`echo "$mf" | sed -e "s/\.mf/\.stats/"`
    $scriptpath/run_test.sh "$codepath" "$cf" "$mf" \
      "$resultspath/$resultsfile" &> /dev/null
    testresult=$?

    if [[ $? -ne 0 ]]
    then
      echo "$errorstring $controller failed integration test ($cf, $mf)"
      break
    fi
  done
  if [[ $? -eq 0 ]]
  then
    echo "$controller passed integration test ($cf, $mf)"
  fi
done

