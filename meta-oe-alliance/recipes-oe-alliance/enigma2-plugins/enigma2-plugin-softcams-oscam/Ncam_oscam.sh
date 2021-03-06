#!/bin/sh

CAMNAME="OScam"

remove_tmp () {
	rm -f /tmp/*.info /tmp/*.tmp
}

case "$1" in
	start)
	remove_tmp
	/usr/bin/oscam -b -c /etc/tuxbox/config/
	;;
	stop)
	killall -9 oscam 2> /dev/null
	remove_tmp
	;;
	restart|reload)
	$0 stop
	sleep 1
	$0 start
	;;
	version)
	echo "1.2"
	;;
	info)
	echo "oscam 1.2"
	;;
	*)
	echo "Usage: $0 start|stop|restart"
	exit 1
	;;
esac
exit 0
