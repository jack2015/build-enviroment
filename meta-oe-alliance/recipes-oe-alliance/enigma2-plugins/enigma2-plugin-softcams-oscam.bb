SUMMARY = "Oscam Softcam for ${MACHINE}"
require conf/license/openpli-gplv2.inc
PACKAGE_ARCH = "${MACHINE_ARCH}"
CAMNAME = "oscam"
DEPENDS = "libusb openssl"

PV = "11703"
PR = "r798"

INHIBIT_PACKAGE_STRIP = "1"
INSANE_SKIP_${PN} += "already-stripped"

inherit cmake gitpkgv

SRC_URI = "git://github.com/jack2015/oscam-patched.git;protocol=${GIT_PROTOCOL};branch=master"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/git"
B = "${S}"

SRC_URI += " \
    file://oscam.conf \
    file://softcam.${CAMNAME} \
    "

EXTRA_OECMAKE += "\
    -DOSCAM_SYSTEM_NAME=Tuxbox \
    -DWEBIF=1 \
    -DWITH_STAPI=0 \
    -DHAVE_LIBUSB=1 \
    -DSTATIC_LIBUSB=1 \
    -DWITH_SSL=1 \
    -DCLOCKFIX=1 \
    -DCW_CYCLE_CHECK=1 \
    -DCS_CACHEEX=1 \
    -DMODULE_CONSTCW=1 \
    -DLCDSUPPORT=1 \
    "

do_install() {
    install -d ${D}${sysconfdir}/tuxbox/config
    install -m 0644 ${WORKDIR}/oscam.conf ${D}${sysconfdir}/tuxbox/config
    install -d ${D}${bindir}
    install -m 0755 ${B}/${CAMNAME} ${D}${bindir}
    install -d ${D}/etc/init.d
    install -m 0755 ${WORKDIR}/softcam.${CAMNAME} ${D}/etc/init.d/softcam.${CAMNAME}
}

do_install_append_dm800se() {
    upx --best --ultra-brute ${D}/usr/bin/${CAMNAME}
}

do_install_append_dm500hd() {
    upx --best --ultra-brute ${D}/usr/bin/${CAMNAME}
}

CONFFILES = "/etc/tuxbox/config/oscam.conf"
FILES_${PN} = "/usr /etc"

CAMPATH = "/etc/init.d/softcam.${CAMNAME}"
CAMLINK = "/etc/init.d/softcam"
# If no cam selected yet, install and start this cam (and don't start it on the build host).
pkg_postinst_${PN}() {
	if [ ! -e "$D${CAMLINK}" ] || [ "/etc/init.d/softcam.None" = "`readlink -f $D${CAMLINK}`" ] || [ "softcam.None" = "`readlink -f $D${CAMLINK}`" ]
	then
		ln -sf "softcam.${CAMNAME}" "$D${CAMLINK}"
		$D${CAMPATH} restart > /dev/null 2>&1
	else
		$D${CAMLINK} stop > /dev/null 2>&1
		ln -sf "softcam.${CAMNAME}" "$D${CAMLINK}"
		$D${CAMPATH} restart > /dev/null 2>&1
	fi
}

# Stop this cam (if running), and move softlink to None if we're the current cam
pkg_prerm_${PN}_prepend() {
	if  [ "/etc/init.d/softcam.${CAMNAME}" = "`readlink -f $D${CAMLINK}`" ] || [ "softcam.${CAMNAME}" = "`readlink -f $D${CAMLINK}`" ]
	then
		$D${CAMPATH} stop > /dev/null 2>&1
		ln -sf "softcam.None" "$D${CAMLINK}"
	fi
}
