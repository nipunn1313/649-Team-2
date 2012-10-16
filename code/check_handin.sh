#!/bin/bash

if [[ `which dos2unix` == "" ]]
then
  echo "You must install the 'dos2unix' utility"
  exit 1
fi

errorcolor=$(echo -e "\033[0;31m")
nocolor=$(echo -e "\033[0m")
errorstring=${errorcolor}ERROR:${nocolor}

# Set some paths
rootpath=`git rev-parse --show-toplevel`
simfile="$rootpath/code/simulator-4.6.tar.gz"
simsite="http://www.ece.cmu.edu/~ece649/project/codebase/simulator-4.6.tar.gz"
portfoliopath="$rootpath/portfolio"
temppath="$rootpath/code/temp"
codepath="`pwd`"

# Make the temporary directory
mkdir -p "$temppath"
rm -rf "$temppath/*"

# Download the simulator tarball if necessary
if [[ ! -f "$simfile" ]]
then
  echo "Simulator tarball not found, downloading now"
  wget -q $simsite
  echo "Download complete"
fi

if [[ ! -f "$simfile" ]]
then
  echo "$errorstring Unable to download simulator tarball"
  rm -rf "$temppath"
  exit 1
fi

# Extract the vanilla simulator code into the directory
tar -xzf "$simfile" -C "$temppath"

# Copy in all of the modified files and compile
make portfolio
if [[ $? -ne 0 ]]
then
  echo "$errorstring 'make portfolio' failed"
  rm -rf "$temppath"
  exit 1
fi
cp "$portfoliopath"/elevatorcontrol/*.java \
  "$temppath/code/simulator/elevatorcontrol/"
cd "$temppath/code"
echo "Compiling code..."
make
if [[ $? -eq 0 ]]
then
  echo "Compiled successfully"
  echo ""
else
  echo "$errorstring 'make' failed"
  cd "$codepath"
  rm -rf "$temppath"
  exit 1
fi

# Verify unit tests
echo "Verifying unit tests..."
cd "$portfoliopath/unit_test"
$codepath/run_units.sh "$temppath/code" "$codepath"
echo "Unit tests verified"
echo ""

# Verify integration tests
echo "Verifying integration tests"
cd "$portfoliopath/integration_test"
$codepath/run_integrations.sh "$temppath/code" "$codepath"

# Clean up after ourselves
cd "$codepath"
rm -rf "$temppath"
