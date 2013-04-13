SUMMARY = "Weston, a Wayland compositor"
DESCRIPTION = "Weston is the reference implementation of a Wayland compositor"
HOMEPAGE = "http://wayland.freedesktop.org"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=275efac2559a224527bd4fd593d38466 \
                    file://src/compositor.c;endline=23;md5=aa98a8db03480fe7d500d0b1f4b8850c"

SRC_URI = "http://wayland.freedesktop.org/releases/${BPN}-${PV}.tar.xz \
           file://weston.png \
           file://weston.desktop"
SRC_URI[md5sum] = "4a4ae0d8c7191af97c98a9035d7e4ed8"
SRC_URI[sha256sum] = "4dc132e276e013d4435886a79a90b348f063f1cd9077f158954d35d706025f53"

inherit autotools pkgconfig

DEPENDS = "libxkbcommon gdk-pixbuf pixman cairo glib-2.0 udev mtdev jpeg"
DEPENDS += "wayland mesa virtual/egl"

EXTRA_OECONF  = "--disable-android-compositor --enable-setuid-install"
EXTRA_OECONF += "--disable-tablet-shell --disable-xwayland"
EXTRA_OECONF += "--enable-simple-clients --enable-clients --disable-simple-egl-clients"
EXTRA_OECONF += "--enable-fbdev-compositor"

PACKAGECONFIG ??= "${@base_contains('DISTRO_FEATURES', 'wayland', 'kms wayland', '', d)} \
                   ${@base_contains('DISTRO_FEATURES', 'x11', 'x11', '', d)} \
                   ${@base_contains('DISTRO_FEATURES', 'opengles2', 'gles', '', d)} \
                  "
# Weston on KMS
PACKAGECONFIG[kms] = "--enable-drm-compositor --enable-weston-launch,--disable-drm-compositor --disable-weston-launch,drm udev mesa libpam"
# Weston on Wayland (nested Weston)
PACKAGECONFIG[wayland] = "--enable-wayland-compositor,--disable-wayland-compositor,mesa"
# Weston on X11
PACKAGECONFIG[x11] = "--enable-x11-compositor,--disable-x11-compositor,virtual/libx11 libxcb libxcb libxcursor cairo"

PACKAGECONFIG[gles] = "--with-cairo-glesv2,,virtual/libgles2"

do_install_append() {
	# Weston doesn't need the .la files to load modules, so wipe them
	rm -f ${D}/${libdir}/weston/*.la

	for feature in ${DISTRO_FEATURES}; do
		# If X11, ship a desktop file to launch it
		if [ "$feature" = "x11" ]; then
			install -d ${D}${datadir}/applications
			install ${WORKDIR}/weston.desktop ${D}${datadir}/applications

			install -d ${D}${datadir}/icons/hicolor/48x48/apps
			install ${WORKDIR}/weston.png ${D}${datadir}/icons/hicolor/48x48/apps
                fi
	done
}

FILES_${PN} += "${datadir}/applications ${datadir}/icons"

RDEPENDS_${PN} += "xkeyboard-config"
RRECOMMENDS_${PN} = "liberation-fonts"
