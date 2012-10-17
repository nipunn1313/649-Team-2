#! /bin/bash

# This script must be run from the code/ directory

outfile=$1

if [ "$outfile" == "" ]
then
  exit 1
fi

read -d '' beginning <<"EOF"
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
  <meta content="text/html;charset=ISO-8859-1" http-equiv="Content-Type">
  <title>18649 Elevatorcontrol Package</title>
</head>
<body>
<h1 style="text-align: center;">18649 - Elevator Control Package<br>
</h1>
<hr style="width: 100%; height: 2px;">18649 &lt;Fall 2012&gt;<br>
Group 2 - Nipunn Koorapati (nkoorapa), Jacob Olson (jholson),
Ben Clark (brclark), Nick Mazurek (nmazurek)<br>
<hr style="width: 100%; height: 2px;">This page lists and describes the
java files in your elevatorcontrol package.&nbsp; Update this document
to reflect your implementation and add your java files to the
elevatorcontrol folder.<br>
<span style="font-weight: bold;"><br>
Note:&nbsp; In order for your portfolio to be complete, the code files
in the elevatorcontrol folder must compiled and run if copied into the
elevatorcontrol package (overwriting any duplicates) of a clean version
of the latest required simulator release.</span><br>
EOF

read -d '' ending <<"EOF"
<br>
</body>
</html>
EOF

errorcolor=$(echo -e "\033[0;31m")
nocolor=$(echo -e "\033[0m")
errorstring=${errorcolor}ERROR:${nocolor}

comment='^;.*'
emptyline='^$'

while read line
do
  if [[ $line =~ $comment || $line =~ $emptyline ]]
  then
    continue
  fi

  file=`echo $line | cut -d' ' -f1`;
  type=`echo $line | cut -d' ' -f2`;
  description=`echo $line | cut -d' ' -f'3-'`;

  element="<li><a href=\"$file\">$file</a> - $description</li>";

  if [[ ! -f "../portfolio/elevatorcontrol/$file" ]]
  then
    echo "$errorstring `basename $file` is mentioned in descriptions.txt, but was"\
      "never modified."
    continue
  fi

  files+=" $file";

  if [ "$type" == "Controller" ]
  then
    controllers+="$element";
  fi
  if [ "$type" == "Translator" ]
  then
    translators+="$element";
  fi
  if [ "$type" == "Utility" ]
  then
    utilities+="$element";
  fi
done < descriptions.txt

function hasNoDescription() {
  if [ -z "$1" ]
  then
    return
  fi

  for localfile in $files
  do
    if [ "$localfile" == "$1" ]
    then
      return 1
    fi
  done

  return 0
}

for file in `ls ../portfolio/elevatorcontrol/*.java`
do
  if hasNoDescription `basename $file`
  then
    echo "$errorstring `basename $file` has no entry in code/descriptions.txt"
    exiting=1
  fi
done

if [ $exiting ]
then
  exit 1
fi

echo "$beginning" > $outfile

echo "<h2>Controller files</h2><ul>" >> $outfile
echo "$controllers" >> $outfile
echo "</ul>" >> $outfile

echo "<h2>Translator files</h2><ul>" >> $outfile
echo "$translators" >> $outfile
echo "</ul>" >> $outfile

echo "<h2>Utility files</h2><ul>" >> $outfile
echo "$utilities" >> $outfile
echo "</ul>" >> $outfile

echo "$ending" >> $outfile
