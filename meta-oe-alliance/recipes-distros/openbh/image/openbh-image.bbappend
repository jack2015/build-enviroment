rmpy() {
	rm -f $1/*.py
	rm -f $1/*.pyc
	for file2 in `ls -A $1`
	do
		if [ -d "$1/$file2" ];then
			if [ $file2 != "OpenMultiboot" ];then
				rmpy "$1/$file2"
			fi
		fi
	done
}

rmpo() {
	for file2 in `ls -A $1`
	do
		if [ $file2 = "en" ]; then
			echo "do nothing"
		elif [ $file2 = "ru" ]; then
			echo "do nothing"
		elif [ $file2 = "it" ]; then
			echo "do nothing"
		elif [ $file2 = "de" ]; then
			echo "do nothing"
		elif [ $file2 = "el" ]; then
			echo "do nothing"
		else
			rm -rf $1/$file2
		fi
	done
}

upxall() {
	upx --best --ultra-brute ${IMAGE_ROOTFS}/sbin/ldconfig || true
	upx --best --ultra-brute ${IMAGE_ROOTFS}/usr/bin/blindscan || true
	upx --best --ultra-brute ${IMAGE_ROOTFS}/usr/bin/bsdcat || true
	upx --best --ultra-brute ${IMAGE_ROOTFS}/usr/bin/dbus-daemon || true
	upx --best --ultra-brute ${IMAGE_ROOTFS}/usr/bin/chage || true
	upx --best --ultra-brute ${IMAGE_ROOTFS}/usr/bin/enigma2 || true
	upx --best --ultra-brute ${IMAGE_ROOTFS}/usr/bin/gpasswd || true
	upx --best --ultra-brute ${IMAGE_ROOTFS}/usr/bin/mpg123 || true
	upx --best --ultra-brute ${IMAGE_ROOTFS}/usr/bin/openssl || true
	upx --best --ultra-brute ${IMAGE_ROOTFS}/usr/bin/sdparm || true
	upx --best --ultra-brute ${IMAGE_ROOTFS}/usr/sbin/alsactl || true
	upx --best --ultra-brute ${IMAGE_ROOTFS}/usr/sbin/avahi-daemon || true
	upx --best --ultra-brute ${IMAGE_ROOTFS}/usr/sbin/dropbearmulti || true
	upx --best --ultra-brute ${IMAGE_ROOTFS}/usr/sbin/ethtool || true
	upx --best --ultra-brute ${IMAGE_ROOTFS}/usr/sbin/exportfs || true
	upx --best --ultra-brute ${IMAGE_ROOTFS}/usr/sbin/groupadd || true
	upx --best --ultra-brute ${IMAGE_ROOTFS}/usr/sbin/groupdel || true
	upx --best --ultra-brute ${IMAGE_ROOTFS}/usr/sbin/groupmod || true
	upx --best --ultra-brute ${IMAGE_ROOTFS}/usr/sbin/newusers || true
	upx --best --ultra-brute ${IMAGE_ROOTFS}/usr/sbin/parted || true
	upx --best --ultra-brute ${IMAGE_ROOTFS}/usr/sbin/rpc.mountd || true
	upx --best --ultra-brute ${IMAGE_ROOTFS}/usr/sbin/rpc.statd || true
	upx --best --ultra-brute ${IMAGE_ROOTFS}/usr/sbin/sm-notify || true
	upx --best --ultra-brute ${IMAGE_ROOTFS}/usr/sbin/useradd || true
	upx --best --ultra-brute ${IMAGE_ROOTFS}/usr/sbin/userdel || true
	upx --best --ultra-brute ${IMAGE_ROOTFS}/usr/sbin/usermod || true
	upx --best --ultra-brute ${IMAGE_ROOTFS}/usr/sbin/vsftpd || true
	upx --best --ultra-brute ${IMAGE_ROOTFS}/usr/sbin/wpa_cli || true
	upx --best --ultra-brute ${IMAGE_ROOTFS}/usr/sbin/wpa_supplicant || true
}

remove_unused_file() {
    echo "src/gz openbh-dm800se https://jack2015.github.io/openbh-feed/dm800se" > ${IMAGE_ROOTFS}/etc/opkg/dm800se-feed.conf
    rm -rf ${IMAGE_ROOTFS}/var/lib/opkg/lists
    rm -rf ${IMAGE_ROOTFS}/usr/lib/python2.7/site-packages/*egg-info*
    rm -rf ${IMAGE_ROOTFS}/usr/share/mime/*
    rm -f ${IMAGE_ROOTFS}/bin/bash.bash
    ln -sf busybox.nosuid ${IMAGE_ROOTFS}/bin/bash
    ln -sf busybox.nosuid ${IMAGE_ROOTFS}/bin/sh
    rm -f ${IMAGE_ROOTFS}/usr/lib/locale/locale-archive
    rmpy ${IMAGE_ROOTFS}/usr/lib/enigma2/python/Plugins
    rmpy ${IMAGE_ROOTFS}/usr/lib/enigma2/python/Components
    rmpy ${IMAGE_ROOTFS}/usr/lib/python2.7
    rmpo ${IMAGE_ROOTFS}/usr/lib/enigma2/python/Plugins/Extensions/AudioSync/locale
    rmpo ${IMAGE_ROOTFS}/usr/lib/enigma2/python/Plugins/Extensions/FanControl2/locale
    rmpo ${IMAGE_ROOTFS}/usr/lib/enigma2/python/Plugins/Extensions/AutoBackup/locale
    rmpo ${IMAGE_ROOTFS}/usr/lib/enigma2/python/Plugins/Extensions/BackupSuite/locale
    rmpo ${IMAGE_ROOTFS}/usr/lib/enigma2/python/Plugins/Extensions/CacheFlush/locale
    rmpo ${IMAGE_ROOTFS}/usr/lib/enigma2/python/Plugins/Extensions/OpenWebif/locale
    rmpo ${IMAGE_ROOTFS}/usr/lib/enigma2/python/Plugins/Extensions/OscamStatus/locale
    rmpo ${IMAGE_ROOTFS}/usr/lib/enigma2/python/Plugins/Extensions/MovieCut/locale
    rmpo ${IMAGE_ROOTFS}/usr/lib/enigma2/python/Plugins/Extensions/EPGImport/locale
    rmpo ${IMAGE_ROOTFS}/usr/lib/enigma2/python/Plugins/Extensions/EPGSearch/locale
    rmpo ${IMAGE_ROOTFS}/usr/lib/enigma2/python/Plugins/Extensions/OpenMultiboot/locale
    rmpo ${IMAGE_ROOTFS}/usr/lib/enigma2/python/Plugins/SystemPlugins/NetworkBrowser/locale
    rmpo ${IMAGE_ROOTFS}/usr/lib/enigma2/python/Plugins/SystemPlugins/ServiceApp/locale
    rmpo ${IMAGE_ROOTFS}/usr/lib/enigma2/python/Plugins/SystemPlugins/SystemTime/locale
    rmpo ${IMAGE_ROOTFS}/usr/lib/enigma2/python/Plugins/SystemPlugins/MountManager/locale
    rmpo ${IMAGE_ROOTFS}/usr/lib/enigma2/python/Plugins/SystemPlugins/Blindscan/locale
    rmpo ${IMAGE_ROOTFS}/usr/lib/enigma2/python/Plugins/SystemPlugins/MPHelp/locale
    rmpo ${IMAGE_ROOTFS}/usr/lib/enigma2/python/Plugins/SystemPlugins/OBH/locale
    upxall
}

ROOTFS_POSTPROCESS_COMMAND_dm800se += "remove_unused_file; "
