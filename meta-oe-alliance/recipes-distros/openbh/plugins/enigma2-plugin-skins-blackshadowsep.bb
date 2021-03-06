SUMMARY = "Enigma2 Skin BlackShadowSE_P"
MAINTAINER = "BlackHole team"
SECTION = "base"
PRIORITY = "required"
LICENSE = "proprietary"
inherit allarch

require conf/license/license-gplv2.inc

inherit gitpkgv

SRCREV = "${AUTOREV}"

PV = "3.0+git${SRCPV}"
PKGV = "3.0+git${GITPKGV}"
VER ="3.0"
PR = "r2"

SRC_URI="git://github.com/jack2015/OBH-skins.git;protocol=https"
SRC_URI_dm800se="git://github.com/jack2015/OBH-skins.git;protocol=https;branch=RC"

S = "${WORKDIR}/git/BlackShadowSE_P"

FILES_${PN} = "/usr"

do_install() {
    cp -rp ${S}/usr ${D}/
    rm -rf ${D}/usr/share/fonts
    chmod -R a+rX ${D}/usr
}
