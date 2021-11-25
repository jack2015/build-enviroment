SUMMARY = "decoding library for DTS Coherent Acoustics streams"
SECTION = "libs/multimedia"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe"

inherit autotools gitpkgv

SRCREV = "${AUTOREV}"
PV = "0.0.7+git${SRCPV}"
PKGV = "0.0.7+git${GITPKGV}"

SRC_URI = "git://gitlab.com/jack2015/libdca.git;protocol=https;branch=master \
        file://fix-libdts-link-path.patch"

S = "${WORKDIR}/git"
