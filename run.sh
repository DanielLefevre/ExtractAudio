#!/bin/sh

java -classpath ./lib/jdom-1.1.2.jar:./bin rtl.extract.Main $1 &&

echo "Copy in the directory Music." &&
cp -v -u files/* -r /c/Users/Daniel/Music/Z\ Comme\ Zemmour/ &&
echo "Copy done."