LICENSE = "GPLv2"
SECTION = "x11/gnome/libs"

DEPENDS = "libxml2 libgnomecups glib-2.0 pango libart-lgpl fontconfig popt gnome-common freetype"

LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"

inherit pkgconfig gnomebase

SRC_URI += "file://fix.includes.patch \
            file://freetype.patch \
           "
SRC_URI[archive.md5sum] = "63b05ffb5386e131487c6af30f4c56ac"
SRC_URI[archive.sha256sum] = "1034ec8651051f84d2424e7a1da61c530422cc20ce5b2d9e107e1e46778d9691"

FILES_${PN}-dbg += "\
    ${libdir}/${PN}/${PV}/modules/.debug \
    ${libdir}/${PN}/${PV}/modules/*/.debug \
"
FILES_${PN}-staticdev += "\
    ${libdir}/${PN}/${PV}/modules/*.a \
    ${libdir}/${PN}/${PV}/modules/*/*.a \
"
