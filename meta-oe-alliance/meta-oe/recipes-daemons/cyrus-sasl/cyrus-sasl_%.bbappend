FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

DEPENDS_remove = "virtual/db"

SRC_URI += "file://fix-build-openssl102q.patch"
