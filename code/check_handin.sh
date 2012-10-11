#!/bin/bash

if [[ `which dos2unix` == "" ]]
then
  echo "You must install the 'dos2unix' utility"
  exit 1
fi

simfile="`pwd`/simulator-4.6.tar.gz"
simsite="http://www.ece.cmu.edu/~ece649/project/codebase/simulator-4.6.tar.gz"
portfoliopath="`pwd`/../portfolio"
tempdir="temp"
codepath="`pwd`"

# Make the temporary directory
mkdir -p "$tempdir"
rm -rf "$tempdir/*"

tempdir="`pwd`/$tempdir"

# Download the simulator tarball if necessary
if [[ ! -f "$simfile" ]]
then
  echo "Simulator tarball not found, downloading now"
  wget -q $simsite
  echo "Download complete"
fi

if [[ ! -f "$simfile" ]]
then
  echo "Unable to download simulator tarball"
  rm -rf "$tempdir"
  exit 1
fi

# Extract the vanilla simulator code into the directory
tar -xzf "$simfile" -C "$tempdir"

# Copy in all of the modified files and compile
make portfolio &> /dev/null
if [[ $? -ne 0 ]]
then
  echo "'make portfolio' failed"
  rm -rf "$tempdir"
  exit 1
fi
cp "$portfoliopath"/elevatorcontrol/*.java "$tempdir/code/simulator/elevatorcontrol/"
cd "$tempdir/code"
echo "Compiling code..."
make -j 4 &> /dev/null
if [[ $? -eq 0 ]]
then
  echo "Compiled successfully"
else
  echo "Make exited with errors"
  cd "$codepath"
  rm -rf "$tempdir"
  exit 1
fi

# Verify unit tests
echo "Verifying unit tests"
cd "$portfoliopath/unit_test"
$codepath/test_verify.sh unit_tests.txt &> /dev/null
if [[ $? -ne 0 ]]
then
  echo "test_verify.sh unit_tests.txt exited with errors"
fi
cat unit_tests.txt | grep -v -e "^;" | while read controller cf mf; do
  if [[ "$controller" == "" ]]; then
    continue
  fi
  for i in {1..10}
  do
    failures=`java -cp "$tempdir/code" simulator.framework.Elevator -cf "$cf" -mf "$mf" | grep -e "^Failed"`
    if [[ ! $failures == "Failed:  0" ]]
    then
      echo "ERROR: $controller failed unit test $cf $mf"
    fi
  done
done
rm -f injection*

# Verify integration tests
echo "Verifying integration tests"
cd "$portfoliopath/integration_test"
$codepath/test_verify.sh integration_tests.txt &> /dev/null
if [[ $? -ne 0 ]]
then
  echo "test_verify.sh integration_tests.txt exited with errors"
fi
cat integration_tests.txt | grep -v -e "^;" | while read controller cf mf; do
  if [[ "$controller" == "" ]]; then
    continue
  fi
  for i in {1..10}
  do
    failures=`java -cp "$tempdir/code" simulator.framework.Elevator -cf "$cf" -mf "$mf" | grep -e "^Failed"`
    if [[ ! $failures == "Failed:  0" ]]
    then
      echo "ERROR: $controller failed integration test $cf $mf"
    fi
  done
done
rm -f injection*

cd "$codepath"
rm -rf "$tempdir"
