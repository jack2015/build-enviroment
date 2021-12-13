SUMMARY = "OpenBH Base"
MAINTAINER = "OpenBH"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

ALLOW_EMPTY_${PN} = "1"
PACKAGES = "${PN}"

PV = "${IMAGE_VERSION}"
PR = "r7"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

DEPENDS = "openbh-version-info"

RDEPENDS_${PN} = "\
    ${@bb.utils.contains("MACHINE_FEATURES", "smallflash", "", "\
    curl \
    hddtemp \
    openvpn \
    inadyn-mt \
    rtmpdump \
    dvbsnoop \
    openssh-sftp-server \
    mc \
    ", d)} \
    blackhole-base \
    blackholesocker \
    ca-certificates \
    ntfs-3g \
    oe-alliance-base \
    openbh-bootlogo \
    openbh-enigma2 \
    openbh-spinner \
    openbh-version-info \
    python-imaging \
    python-service-identity \
    ${@bb.utils.contains_any("MACHINE", "dm800se dm500hd", "", "autofs smbclient", d)} \
    ${@bb.utils.contains("TUNE_FEATURES", "armv", "glibc-compat", "", d)} \
    "
