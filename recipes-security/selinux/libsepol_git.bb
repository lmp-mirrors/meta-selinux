PR = "r1"
PV = "2.1.4+git${SRCPV}"

include selinux_git.inc
include ${BPN}.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=a6f89e2100d9b6cdffcea4f398e37343"

SRC_URI += "file://libsepol-Change-ranlib-for-cross-compiling.patch"
