SUMMARY = "Oscam Softcam for ${MACHINE}"
require conf/license/openpli-gplv2.inc
PACKAGE_ARCH = "${MACHINE_ARCH}"
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
    file://BhCamConf \
    file://oscam.conf \
    file://Ncam_oscam.sh \
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
    install -d ${D}${sysconfdir}
    install -m 0644 ${WORKDIR}/BhCamConf ${D}${sysconfdir}
    install -d ${D}${sysconfdir}/tuxbox/config
    install -m 0644 ${WORKDIR}/oscam.conf ${D}${sysconfdir}/tuxbox/config
    install -d ${D}${bindir}
    install -m 0755 ${B}/oscam ${D}${bindir}
    install -d ${D}/usr/camscript
    install -m 0755 ${WORKDIR}/Ncam_oscam.sh ${D}/usr/camscript
}

do_install_append_dm800se() {
    upx --best --ultra-brute ${D}/usr/bin/oscam
}

do_install_append_dm500hd() {
    upx --best --ultra-brute ${D}/usr/bin/oscam
}

CONFFILES = "/etc/tuxbox/config/oscam.conf"
FILES_${PN} = "/usr /etc"


