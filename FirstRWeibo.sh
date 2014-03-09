#!/bin/tcsh
# Program:
#			Launch the csl2wk01 - csl2wk40 workstation and run the QuoraGrab.jar
#History
#2014/01/05					Wang Xinyu			First Release
echo -e "`date --rfc-2822`\nBegin to SSH the CS lab 2 workstations"
UserName="xwangau"
for(( i=1;i<=9;i=i+1))
do
	arg=$i*3-3
	ssh $UserName@csl2wk0$i	'cd /csproject/wil1/wangxinyu/WeiboSoc;sh Nohup1.sh'
#	bash -c "nohup java -jar -Xmx800m QuoraGrab.jar expr `echo $HOST | cut -c8` \* 3 - 3 > & &";
#	bash -c "nohup java -jar -Xmx800m QuoraGrab.jar $arg+1 > outputDir/`echo $HOST | cut -c8`.log 2>&1 &";
#	bash -c "nohup java -jar -Xmx800m QuoraGrab.jar $arg+2 > outputDir/`echo $HOST | cut -c8`.log 2>&1 &";'
	echo -e "csl2wk0"$i" built up"
done
for(( i=10;i<=41;i=i+1 ))
do
	arg=$i*3-3
	ssh $UserName@csl2wk$i 'cd /csproject/wil1/wangxinyu/WeiboSoc;sh Nohup1.sh'
#	bash -c "num=${num:6}";
#	bash -c "echo $num";
#	bash -c "nohup java -jar -Xmx800m QuoraGrab.jar $arg > outputDir/;'$arg'.log 2>&1 &";
#	bash -c "nohup java -jar -Xmx800m QuoraGrab.jar $arg+1 > outputDir/'$arg+1'.log 2>&1 &";
#	bash -c "nohup java -jar -Xmx800m QuoraGrab.jar $arg+2 > outputDir/'$arg+2'.log 2>&1 &";'
	echo -e "csl2wk"$i" built up"
done

echo -e "My work is done, please trace the *.log file in \n/csproject/wil1/wangxinyu/WeiboSoc/outputDir/"