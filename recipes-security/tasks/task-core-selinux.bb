DESCRIPTION = "SELinux task for Poky"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
PR = "r0"

PACKAGES = "\
    task-core-selinux \
    "

ALLOW_EMPTY = "1"

RDEPENDS_task-core-selinux = " \
	ustr \
	libsepol \
	libsepol-bin \
	libselinux \
	libselinux-bin \
	libsemanage \
	checkpolicy \
	sepolgen \
	policycoreutils \
	policycoreutils-sandbox \
	policycoreutils-python \
	setools \
	setools-console \
	selinux-config \
	refpolicy-standard \
	refpolicy-mls \
	"
