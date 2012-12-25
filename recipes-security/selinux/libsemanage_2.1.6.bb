SUMMARY = "SELinux binary policy manipulation library"
DESCRIPTION = "libsemanage provides an API for the manipulation of SELinux binary policies. \
It is used by checkpolicy (the policy compiler) and similar tools, as well \
as by programs like load_policy that need to perform specific transformations \
on binary policies such as customizing policy boolean settings."
SECTION = "base"
PR = "r3"
LICENSE = "LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=a6f89e2100d9b6cdffcea4f398e37343"

include selinux_20120216.inc
inherit lib_package

SRC_URI[md5sum] = "b49d75602432d8cfad8a3e5a0a966f07"
SRC_URI[sha256sum] = "64e6849fe50fb463ec0ba24653a26e3452fa4aaa7d7e192213d5c5a7c525aebb"

DEPENDS += "libsepol libselinux ustr bzip2 python bison-native flex-native"

SRC_URI += "file://Fix-segfault-for-standard-policy.patch \
	file://libsemanage-Fix-execve-segfaults-on-Ubuntu.patch \
	file://libsemanage-semanage.conf-for-cross-compile.patch \
	file://libsemanage-fix-path-len-limit.patch"

PACKAGES += "${PN}-python"
FILES_${PN}-python = "${libdir}/python${PYTHON_BASEVERSION}/site-packages/*"
FILES_${PN}-dbg += "${libdir}/python${PYTHON_BASEVERSION}/site-packages/.debug/*"

do_compile_append() {
    oe_runmake pywrap \
            INCLUDEDIR='${STAGING_INCDIR}' \
            LIBDIR='${STAGING_LIBDIR}' \
            PYLIBVER='python${PYTHON_BASEVERSION}' \
            PYINC='-I${STAGING_INCDIR}/$(PYLIBVER)' \
            PYLIB='-L${STAGING_LIBDIR}/$(PYLIBVER) -l$(PYLIBVER)' \
            PYTHONLIBDIR='${PYLIB}'
}

do_install() {
    oe_runmake install \
            DESTDIR="${D}" \
            PREFIX="${D}/${prefix}" \
            INCLUDEDIR="${D}/${includedir}" \
            LIBDIR="${D}/${libdir}" \
            SHLIBDIR="${D}/${libdir}"

    oe_runmake install-pywrap swigify \
            DESTDIR=${D} \
            PYLIBVER='python${PYTHON_BASEVERSION}' \
            PYLIBDIR='${D}/${libdir}/$(PYLIBVER)'
}

BBCLASSEXTEND = "native"
