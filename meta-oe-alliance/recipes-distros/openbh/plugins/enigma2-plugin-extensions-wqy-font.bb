SUMMARY = "Add support for chinese language"
require conf/license/license-gplv2.inc
PACKAGE_ARCH = "${MACHINE_ARCH}"

RDEPENDS_${PN} = "enigma2-plugin-font-wqy-microhei"
PV = "1.0"
PR = "r0"
PACKAGES = "${PN}"
ALLOW_EMPTY_${PN} = "1"

# We only need the packaging tasks - disable the rest
do_fetch[noexec] = "1"
do_unpack[noexec] = "1"
do_patch[noexec] = "1"
do_configure[noexec] = "1"
do_compile[noexec] = "1"
do_install[noexec] = "1"
