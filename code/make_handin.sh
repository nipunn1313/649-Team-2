#!/bin/bash

make clean &> /dev/null
make portfolio &> /dev/null
if [[ $? -ne 0 ]]
then
  echo "'make portfolio' failed"
  exit 1
fi

curpath=`pwd`

cd ../portfolio

tar -czvf $curpath/portfolio.tar.gz ./*

if [[ $? -ne 0 ]]
then
  exit 1
fi

echo "'portfolio.tar.gz' created"

exit 0
