SUMMARY = "Base packages require for image."
LICENSE = "MIT"
PACKAGE_ARCH = "${MACHINEBUILD}"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"


inherit packagegroup

ALLOW_EMPTY_${PN} = "1"
PACKAGES = "${PN}"

PV = "${IMAGE_VERSION}"
PR = "r0"

RDEPENDS_${PN} = "\
    oe-alliance-enigma2 \
    oe-alliance-branding \
    ${@bb.utils.contains("MACHINE_FEATURES", "wol", "vuplus-coldboot vuplus-ethwol", "", d)} \
    ${@bb.utils.contains("MACHINE_FEATURES", "wowl", "vuplus-wowl", "", d)} \
    ${@bb.utils.contains("MACHINE_FEATURES", "iniwol", "ini-coldboot ini-ethwol", "", d)} \
    ${@bb.utils.contains("MACHINE_FEATURES", "gbwol", "gigablue-ethwol", "", d)} \
    ${@bb.utils.contains("MACHINE_FEATURES", "gbsoftwol", "gigablue-ethsoftwol", "", d)} \
    ${@bb.utils.contains("MACHINE_FEATURES", "no-nmap", "" , "nmap", d)} \
    ${@bb.utils.contains("MACHINE_FEATURES", "emmc", "dosfstools mtools e2fsprogs-resize2fs partitions-by-name" , "", d)} \
    ${@bb.utils.contains("MACHINE_FEATURES", "fastboot", "dosfstools mtools android-tools" , "", d)} \
    ${@bb.utils.contains("MACHINE_FEATURES", "vubluetooth", "bluetoothsetup-${MACHINE} enigma2-plugin-extensions-witaispeechtotext", "", d)} \
    ${@bb.utils.contains("MACHINE_FEATURES", "gbbluetooth", "bluetoothsetup-${MACHINE}", "", d)} \
    ${@bb.utils.contains("MACHINE_FEATURES", "recovery", "recovery" , "", d)} \
    ${@bb.utils.contains("DEFAULTTUNE", "sh4", "alsa-utils-amixer-conf" , "", d)} \
    ${@bb.utils.contains("TARGET_ARCH", "arm", "${GETEXTRA}", "", d)} \
    ${@bb.utils.contains("TARGET_ARCH", "aarch64", "${GETEXTRA}", "", d)} \
    avahi-daemon \
    bash \
    cronie \
    dropbear \
    early-configure \
    fakelocale \
    libavahi-client \
    libcrypto-compat-0.9.7 \
    libcrypto-compat-1.0.0 \
    libxcrypt-compat \
    llmnrd \
    tzdata \
    ntpdate \
    opkg \
    rc-local \
    sdparm \
    vsftpd \
    volatile-media \
    packagegroup-base \
    packagegroup-core-boot \
    modutils-loadscript \
    e2fsprogs-tune2fs \
    e2fsprogs-e2fsck \
    e2fsprogs-mke2fs \
    ${@bb.utils.contains("MACHINE_FEATURES", "dreamboxv1", "", "p7zip", d)} \
    ${@bb.utils.contains("MACHINE_FEATURES", "smallflash", "", "python-twisted-protocols python-numbers python-argparse", d)} \
    ${@bb.utils.contains("MACHINE_FEATURES", "smallflash", "", "wget util-linux-sfdisk util-linux-blkid util-linux-flock", d)} \
    "

# The following RRECOMMENDS ensure that images on boxes with very limited
# kernel space behave identical to those that have these options built-in
# by including the corresponding kernel modules.
# So far these are xfs and vfat and their dependencies
RRECOMMENDS_${PN} = "\
    ${@bb.utils.contains("MACHINE_FEATURES", "smallflash", "", " \
    kernel-module-xfs \
    kernel-module-exportfs \
    kernel-module-fat \
    kernel-module-msdos \
    kernel-module-vfat \
    kernel-module-nls-cp437 \
    kernel-module-nls-iso8859-1 \
    kernel-module-nls-iso8859-15 \
    ", d)} \
    "

GETEXTRA = "${@bb.utils.contains('MACHINE', 'cube', '', 'edid-decode', d)}"
