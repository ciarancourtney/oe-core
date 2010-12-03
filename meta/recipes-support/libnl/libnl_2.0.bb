DESCRIPTION = "libnl is a library for applications dealing with netlink sockets."
HOMEPAGE = "http://www.infradead.org/~tgr/libnl/"
SECTION = "libs/network"
PRIORITY = "optional"

LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=2b41e13261a330ee784153ecbb6a82bc"

DEPENDS = "flex-native bison-native"
PR = "r1"

SRC_URI= "http://www.infradead.org/~tgr/libnl/files/${PN}-${PV}.tar.gz \
          file://fix-makefile.patch \
         "

inherit autotools pkgconfig

LEAD_SONAME = "libnl.so"
