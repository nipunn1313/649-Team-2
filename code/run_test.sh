#!/bin/bash

# This script has no restrictions on where it can be run, all arguments are
# fully specified

if [[ $# -lt 3 || $# -gt 4 ]]
then
  echo "Usage: ./`basename $0` codepath file1.cf file2.mf [file3.stats]"
  exit 1
fi

# Set some paths
codepath="$1"
cf="$2"
mf="$3"

# Run the test
failures=`java -cp "$codepath" simulator.framework.Elevator \
  -cf "$cf" -mf "$mf" | grep -e "^Failed"`

# Find the last generated stats file, and either rename or remove it
# TODO This is wrong, it needs to iterate through numbers until it finds the
# file that doesn't exist.
statsfile=`ls injection-$mf-*.stats | sort | tail -1`
if [[ $# -eq 4 ]]
then
  mv "$statsfile" "$4"
else
  rm "$statsfile"
fi

# Exit on failure
if [[ ! $failures == "Failed:  0" ]]
then
  echo "ERROR: test ($cf, $mf) failed"
  exit 1
fi

exit 0
