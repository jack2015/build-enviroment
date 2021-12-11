require conf/license/openpli-gplv2.inc
SUMMARY = "mgcamd softcam for mipsel version 1.35a"
PACKAGE_ARCH = "${MACHINE_ARCH}"

INHIBIT_PACKAGE_STRIP = "1"
INSANE_SKIP_${PN}_append = " already-stripped"
DEPENDS += "virtual/crypt"
RDEPENDS_${PN} += "libcrypto-compat-0.9.7 libcrypto-compat-1.0.0 libxcrypt-compat zlib"

PV = "1.35a"

SRC_URI += " \
	file://ignore.list.example \
	file://mgcamd.mipsel \
	file://mg_cfg \
	file://newcamd.list.example \
	file://priority.list.example \
	file://replace.list.example \
	file://softcam.mgcamd135 \
	"

S = "${WORKDIR}"

FILES_${PN} += "/usr /etc"

do_install() {
	install -d ${D}/usr/bin
	install -m 0755 ${S}/mgcamd.mipsel ${D}/usr/bin/mgcamd135
	install -d ${D}/usr/keys
	install -m 0644 ${S}/mg_cfg ${D}/usr/keys/mg_cfg
	install -m 0644 ${S}/ignore.list.example ${D}/usr/keys/ignore.list.example
	install -m 0644 ${S}/newcamd.list.example ${D}/usr/keys/newcamd.list.example
	install -m 0644 ${S}/priority.list.example ${D}/usr/keys/priority.list.example
	install -m 0644 ${S}/replace.list.example ${D}/usr/keys/replace.list.example
	touch ${D}/usr/keys/ignore.list
	touch ${D}/usr/keys/newcamd.list
	touch ${D}/usr/keys/priority.list
	touch ${D}/usr/keys/replace.list
	install -d ${D}/usr/camscript
	install -m 0755 ${S}/softcam.mgcamd135 ${D}/usr/camscript/Ncam_mgcamd135.sh
}

CAMPATH = "/usr/camscript/Ncam_mgcamd135.sh"

pkg_postinst_${PN}() {
	if [ -f "$D/lib/ld-2.30.so" ]; then
		ln -sf "ld-2.30.so" "$D/lib/ld-linux.so.3"
	elif [ -f "$D/lib/ld-2.31.so" ]; then
		ln -sf "ld-2.31.so" "$D/lib/ld-linux.so.3"
	elif [ -f "$D/lib/ld-2.32.so" ]; then
		ln -sf "ld-2.32.so" "$D/lib/ld-linux.so.3"
	elif [ -f "$D/lib/ld-2.33.so" ]; then
		ln -sf "ld-2.33.so" "$D/lib/ld-linux.so.3"
	elif [ -f "$D/lib/ld-2.34.so" ]; then
		ln -sf "ld-2.34.so" "$D/lib/ld-linux.so.3"
	fi
}

pkg_prerm_${PN}_prepend() {
	$D${CAMPATH} stop > /dev/null 2>&1
	rm -f "$D/lib/ld-linux.so.3" > /dev/null 2>&1
}
