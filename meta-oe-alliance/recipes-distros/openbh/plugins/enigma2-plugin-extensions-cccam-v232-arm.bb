SUMMARY = "CCcam Softcam for arm Ver2.3.2"
require conf/license/openpli-gplv2.inc
PACKAGE_ARCH = "${MACHINE_ARCH}"

INHIBIT_PACKAGE_STRIP = "1"
INSANE_SKIP_${PN}_append = " already-stripped"
DEPENDS += "virtual/crypt"
RDEPENDS_${PN} += "libxcrypt-compat"

S = "${WORKDIR}"
PV = "2.3.2"

SRC_URI += " \
	file://CCcam232 \
	file://CCcam.cfg \
	file://CCcam.xml \
	file://softcam.CCcam232 \
"

FILES_${PN} = "/usr /etc"

do_install() {
	install -d ${D}/usr/bin
	install -m 0755 ${S}/CCcam232 ${D}/usr/bin/CCcam232
	install -d ${D}/etc
	install -m 0644 ${S}/CCcam.cfg ${D}/etc/CCcam.cfg
	install -d ${D}/etc/ppanels
	install -m 0644 ${WORKDIR}/CCcam.xml ${D}/etc/ppanels/CCcam.xml
	install -d ${D}/usr/camscript
	install -m 0755 ${S}/softcam.CCcam232 ${D}/usr/camscript/Ncam_CCcam232.sh
}

CAMPATH = "/usr/camscript/Ncam_CCcam232.sh"

pkg_prerm_${PN}_prepend() {
	$D${CAMPATH} stop > /dev/null 2>&1
}
