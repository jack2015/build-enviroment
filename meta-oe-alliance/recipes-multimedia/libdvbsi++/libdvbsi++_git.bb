SUMMARY = "C++ parsing library for Service Information (SI) in DVB systems"
AUTHOR = "Andreas Oberritter"
SECTION = "libs/multimedia"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=a6f89e2100d9b6cdffcea4f398e37343"

SRCREV = "${AUTOREV}"
PV = "0.3.9+git${SRCPV}"
PKGV = "0.3.9+git${GITPKGV}"

SRC_URI := "git://github.com/OpenVisionE2/libdvbsi.git"

S = "${WORKDIR}/git"

inherit autotools pkgconfig gitpkgv
