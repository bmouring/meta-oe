SUMMARY = "Tool Command Language ToolKit Extension"
HOMEPAGE = "http://tcl.sourceforge.net"
SECTION = "devel/tcltk"

# http://www.tcl.tk/software/tcltk/license.html
LICENSE = "tcl"
LIC_FILES_CHKSUM = "file://../license.terms;md5=c88f99decec11afa967ad33d314f87fe \
    file://../compat/license.terms;md5=c88f99decec11afa967ad33d314f87fe \
    file://../doc/license.terms;md5=c88f99decec11afa967ad33d314f87fe \
    file://../library/license.terms;md5=c88f99decec11afa967ad33d314f87fe \
    file://../macosx/license.terms;md5=c88f99decec11afa967ad33d314f87fe \
    file://../tests/license.terms;md5=c88f99decec11afa967ad33d314f87fe \
    file://../unix/license.terms;md5=c88f99decec11afa967ad33d314f87fe \
    file://../win/license.terms;md5=c88f99decec11afa967ad33d314f87fe \
    file://../xlib/license.terms;md5=c88f99decec11afa967ad33d314f87fe \
"

PNBLACKLIST[tk] = "tk8.6.0/unix/libtk8.6.so: error: undefined reference to 'FcCharSetHasChar'"

DEPENDS = "tcl virtual/libx11 libxt"

SRC_URI = "\
    ${SOURCEFORGE_MIRROR}/tcl/${BPN}${PV}-src.tar.gz \
    file://confsearch.diff;striplevel=2 \
    file://non-linux.diff;striplevel=2 \
    file://tklibrary.diff;striplevel=2 \
    file://tkprivate.diff;striplevel=2 \
    file://fix-xft.diff \
"
SRC_URI[md5sum] = "b883a1a3c489c17413fb602a94bf54e8"
SRC_URI[sha256sum] = "5c708b2b6f658916df59190b27750fa1ea2bc10992108e10f961c0700f058de6"

S = "${WORKDIR}/${BPN}${PV}/unix"

# Short version format: "8.6"
VER = "${@os.path.splitext(d.getVar('PV', True))[0]}"

LDFLAGS += "-Wl,-rpath,${libdir}/tcltk/${PV}/lib"
inherit autotools

EXTRA_OECONF = "\
    --enable-threads \
    --with-x \
    --with-tcl=${STAGING_BINDIR_CROSS} \
    --libdir=${libdir} \
"

do_install_append() {
    ln -sf libtk${VER}.so ${D}${libdir}/libtk${VER}.so.0
    oe_libinstall -so libtk${VER} ${D}${libdir}
    ln -sf wish${VER} ${D}${bindir}/wish

    # Even after passing libdir=${libdir} at config, some incorrect dirs are still generated for the multilib build
    if [ "$libdir" != "/usr/lib" ]; then
        # Move files to correct library directory
        mv ${D}/usr/lib/tk${VER}/* ${D}/${libdir}/tk${VER}/
        # Remove unneeded/incorrect dir ('usr/lib/')
        rm -rf ${D}/usr/lib
    fi
}

PACKAGECONFIG ??= "xft"
PACKAGECONFIG[xft] = "--enable-xft,--disable-xft,xft"
PACKAGECONFIG[xss] = "--enable-xss,--disable-xss,libxscrnsaver libxext"

PACKAGES =+ "${PN}-lib"

FILES_${PN}-lib = "${libdir}/libtk${VER}.so*"
FILES_${PN} += "${libdir}/tk*"

# isn't getting picked up by shlibs code
RDEPENDS_${PN} += "tk-lib"
RDEPENDS_${PN}_class-native = ""

BBCLASSEXTEND = "native"

# Fix the path in sstate
SSTATE_SCAN_FILES += "*Config.sh"
