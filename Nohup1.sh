#!/bin/bash
flag=`echo $HOST | cut -c7`
runnable=`pgrep java`
if [ "$flag" == "0" ]; then
	pos=8
else
	pos=7,8
fi
	base=`echo $HOST | cut -c$pos`
	echo $base
offset=229600
arg1=`expr $base \* 3 - 3`
arg2=`expr $base \* 3 - 2`
arg3=`expr $base \* 3 - 1`
arg4=`expr $offset + $base \* 800 - 1`
arg5=`expr $offset + $base \* 800 - 800`
#nohup java -jar -Xmx800m Crawler.jar `expr \`echo $HOST | cut -c8\` \* 3 - 3` > outputDir/`expr \`echo $HOST | cut -c8\` \* 3 - 3`.log 2>&1 &
#nohup java -jar -Xmx800m Crawler.jar `expr \`echo $HOST | cut -c8\` \* 3 - 3` > outputDir/`expr \`echo $HOST | cut -c8\` \* 3 - 2`.log 2>&1 &
#nohup java -jar -Xmx800m Crawler.jar `expr \`echo $HOST | cut -c8\` \* 3 - 3` > outputDir/`expr \`echo $HOST | cut -c8\` \* 3 - 1`.log 2>&1 &

nohup java -jar -Xmx1480m Crawler.jar $arg5 $arg4 >& outputDir/$arg5.log &
#if [ -z "$runnable" ]; then
#	nohup java -jar -Xmx800m Crawler.jar $base > outputDir/$base.log 2>&1 &
#	echo $HOST"'s job list"
#	jobs
#fi#nohup java -jar -Xmx800m Crawler.jar $arg2 > outputDir/$arg2.log 2>&1 &
#nohup java -jar -Xmx800m Crawler.jar $arg3 > outputDir/$arg3.log 2>&1 &

#echo $arg1,$arg2,$arg3
jobs