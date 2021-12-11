SUMMARY = "OpenBH Feed"
MAINTAINER = "OpenBH"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

ALLOW_EMPTY_${PN} = "1"
PACKAGE_ARCH = "${MACHINE_ARCH}"
PACKAGES = "${PN}"

PV = "${IMAGE_VERSION}"
PR = "r5"

inherit packagegroup

RRECOMMENDS_${PN} = " \
	enigma2-plugin-extensions-wqy-font \
	${@bb.utils.contains("TARGET_ARCH", "mipsel", " \
	enigma2-plugin-extensions-cccam-v209 \
	enigma2-plugin-extensions-cccam-v221 \
	enigma2-plugin-extensions-cccam-v230 \
	enigma2-plugin-extensions-cccam-v232 \
	enigma2-plugin-extensions-cccam-v238 \
	enigma2-plugin-extensions-mgcamd-v135a \
	enigma2-plugin-extensions-mgcamd-v145c \
	", "", d)} \
	${@bb.utils.contains("TARGET_ARCH", "arm", " \
	enigma2-plugin-extensions-cccam-v232-arm \
	enigma2-plugin-extensions-cccam-v238-arm \
	enigma2-plugin-extensions-mgcamd-v135a-arm \
	", "", d)} \
"
