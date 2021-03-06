SUMMARY = "Control your receiver with a browser"
# LICENSE = "GPLv2"
# LIC_FILES_CHKSUM = "file://README;firstline=10;lastline=12;md5=26abba37d1c2fcbf96a087ceb8e1db86"
require conf/license/license-gplv2.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

DEPENDS = "python-cheetah-native"

RDEPENDS_${PN} = "\
	aio-grab \
	python-cheetah \
	python-compression \
	python-ipaddress \
	python-json \
	python-misc \
	python-numbers \
	python-pyopenssl \
	python-shell \
	python-twisted-web \
	python-unixadmin \
	oe-alliance-branding \
	"

inherit gitpkgv distutils-openplugins gettext

DISTUTILS_INSTALL_ARGS = "--root=${D} --install-lib=${libdir}/enigma2/python/Plugins"

SRCREV = "${AUTOREV}"
PV = "git${SRCPV}"
PKGV = "${GITPKGVTAG}"

PV_dm800se = "1.3.9+git${SRCPV}"
PKGV_dm800se = "1.3.9+git${GITPKGV}"

SRC_URI = "git://github.com/E2OpenPlugins/e2openplugin-OpenWebif.git;protocol=https;branch=master \
           file://transcoding.py"

SRC_URI_dm800se = "git://github.com/E2OpenPlugins/e2openplugin-OpenWebif.git;protocol=https;branch=NoSix \
           file://transcoding.py"

S="${WORKDIR}/git"

# Just a quick hack to "compile" it
do_compile() {
	cp ${WORKDIR}/transcoding.py ${S}/plugin/controllers/transcoding.py
	rm -rf ${S}/plugin/public/static/remotes >/dev/null 2>&1 || true
	find ${S}/plugin/public/ -empty -type d -delete >/dev/null 2>&1 || true
	find ${S}/plugin/public/images/boxes/ ! -name 'unknown.png' -type f -exec rm -f {} +
	find ${S}/plugin/public/images/remotes/ ! -name 'ow_remote.png' -type f -exec rm -f {} +
	cheetah-compile -R --nobackup ${S}/plugin
	python -O -m compileall ${S}
}

PLUGINPATH = "${libdir}/enigma2/python/Plugins/Extensions/OpenWebif"
do_install_append() {
	install -d ${D}${PLUGINPATH}
	cp -r ${S}/plugin/* ${D}${PLUGINPATH}
	chmod a+rX ${D}${PLUGINPATH}
}

INSANE_SKIP_${PN} += "build-deps"
INSANE_SKIP_${PN}-terminal += "build-deps"
INSANE_SKIP_${PN}-vxg += "build-deps"

FILES_${PN} = "${PLUGINPATH}"
# Required empty packages for build compatibility with distros still using OWIF 0.x.y - 1.0.z
PACKAGES =+ "${PN}-terminal ${PN}-themes ${PN}-webtv ${PN}-vxg"
FILES_${PN}-themes = "${libdir}/enigma2/python/Plugins/Extensions/OpenWebif/public/themes"
FILES_${PN}-webtv = "${libdir}/enigma2/python/Plugins/Extensions/OpenWebif/public/webtv"
FILES_${PN}-vxg = "${libdir}/enigma2/python/Plugins/Extensions/OpenWebif/public/vxg"
RDEPENDS_${PN}-terminal = "${PN} shellinabox"
RDEPENDS_${PN}-themes = "${PN}"
RDEPENDS_${PN}-webtv = "${PN}"
RDEPENDS_${PN}-vxg = "${PN}"
ALLOW_EMPTY_${PN}-terminal = "1"
ALLOW_EMPTY_${PN}-themes = "1"
ALLOW_EMPTY_${PN}-webtv = "1"
ALLOW_EMPTY_${PN}-vxg = "1"

FILES_${PN}-dbg += "\
    /usr/lib/enigma2/python/Plugins/*/*/.debug \
    /usr/lib/enigma2/python/Plugins/*/*/*/.debug \
    /usr/lib/enigma2/python/Plugins/*/*/*/*/.debug \
    /usr/lib/enigma2/python/Plugins/*/*/*/*/*/.debug \
    /usr/lib/enigma2/python/Plugins/*/*/*/*/*/*/.debug \
    "

FILES_${PN}-src = "\
    /usr/lib/enigma2/python/*/*.py \
    /usr/lib/enigma2/python/*/*/*.py \
    /usr/lib/enigma2/python/*/*/*/*.py \
    /usr/lib/enigma2/python/*/*/*/*/*.py \
    /usr/lib/enigma2/python/*/*/*/*/*/*.py \
    /usr/lib/enigma2/python/*/*/*/*/*/*/*.py \
    /usr/lib/enigma2/python/*/*/*/*/*/*/*/*.py \
    /usr/lib/enigma2/python/*/*/*/*/*/*/*/*/*.py \
    /usr/lib/enigma2/python/*/*/*/*/*/*/*/*/*/*.py \
    /usr/lib/enigma2/python/*/*/*/*/*/*/*/*/*/*/*.py \
    "

FILES_${PN}-src += "${PLUGINPATH}/controllers/views/*.tmpl ${PLUGINPATH}/controllers/views/*/*.tmpl ${PLUGINPATH}/controllers/views/*/*/*.tmpl"

python populate_packages_prepend() {
    enigma2_plugindir = bb.data.expand('${libdir}/enigma2/python/Plugins', d)
    do_split_packages(d, enigma2_plugindir, '^(\w+/\w+)/.*\.py$', 'enigma2-plugin-%s-src', 'Enigma2 Plugin: %s', recursive=True, match_path=True, prepend=True)
    do_split_packages(d, enigma2_plugindir, '^(\w+/\w+)/(.*/)?\.debug/.*$', 'enigma2-plugin-%s-dbg', 'Enigma2 Plugin: %s', recursive=True, match_path=True, prepend=True)
}
