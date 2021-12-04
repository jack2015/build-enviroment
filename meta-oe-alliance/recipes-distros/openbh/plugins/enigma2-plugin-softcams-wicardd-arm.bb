SUMMARY = "Wicardd Softcam for arm Ver1.19"
require conf/license/openpli-gplv2.inc
PACKAGE_ARCH = "${MACHINE_ARCH}"

INHIBIT_PACKAGE_STRIP = "1"
INSANE_SKIP_${PN}_append = " already-stripped"
DEPENDS += "virtual/crypt"
RDEPENDS_${PN} += "libcrypto-compat-0.9.7 libcrypto-compat-1.0.0"

S = "${WORKDIR}"
PV = "1.19"

SRC_URI += " \
	file://softcam.wicardd \
	file://wicardd \
	file://wicardd.conf \
	file://wicardd_sample.conf \
"

FILES_${PN} = "/usr /etc"

do_install() {
	install -d ${D}/usr/bin
	install -m 0755 ${S}/wicardd ${D}/usr/bin/wicardd
	install -d ${D}/etc
	touch ${D}/etc/clist.list
	install -d ${D}/etc/tuxbox/config
	install -m 0644 ${S}/wicardd.conf ${D}/etc/tuxbox/config/wicardd.conf
	install -m 0644 ${S}/wicardd_sample.conf ${D}/etc/tuxbox/config/wicardd_sample.conf
	install -d ${D}/usr/camscript
	install -m 0755 ${S}/softcam.wicardd ${D}/usr/camscript/Ncam_wicardd.sh
}

pkg_prerm_${PN}_prepend() {
	$D${CAMPATH} stop > /dev/null 2>&1
}
