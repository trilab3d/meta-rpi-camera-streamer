SUMMARY = "Linux libcamera framework"
SECTION = "libs"

LICENSE = "LGPLv2.1+"
LIC_FILES_CHKSUM = "file://${WORKDIR}/git/COPYING.rst;md5=02897d565cfdaf7d40b59327e1ba8b07"

SRC_URI = "git://github.com/raspberrypi/libcamera.git;protocol=https;branch=hdr2"
#SRC_URI = "file://libcamera.tar"

# v0.0.4
#SRCREV = "6cf637eb253a68edebe59505bea55435fafb00cd"
# v0.0.5
#SRCREV = "fb44403f1c5571549ac128c21daee9761eb9249c"

#SRCREV = "4a23664b2d82136db4346cd6bde38eca0f43ccfb"

# v0.2.0+rpt
#SRCREV = "075b54d5229d0894109e7cbb4bb890bc48bb37e8"

# v0.2.0
#SRCREV = "89227a428a82e724548399d35c98ea89566f9045"

# v0.1.0
SRCREV = "668a5e674aed65b8982b449b4bed58ff7e3e1413"

# v0.1.0+rpt20231116
#SRCREV = "99b177b3e5a50093f664eacd6b09576d17a7e64d"

# v0.1.0+rpt20231122
#SRCREV = "563cd78e1c9858769f7e4cc2628e2515836fd6e7"

PE = "0"

S = "${WORKDIR}/git"
#S = "${WORKDIR}/libcamera"

DEPENDS = "python3-pyyaml-native python3-jinja2-native python3-ply-native python3-jinja2-native udev gnutls chrpath-native libevent libyaml"
DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'qt', 'qtbase qtbase-native', '', d)}"

PACKAGES =+ "${PN}-gst"

PACKAGECONFIG ??= ""
PACKAGECONFIG[gst] = "-Dgstreamer=enabled,-Dgstreamer=disabled,gstreamer1.0 gstreamer1.0-plugins-base"

EXTRA_OEMESON = " \
    -Dpipelines=uvcvideo,simple,vimc,rpi/vc4 \
    -Dipas=vimc,rpi/vc4 \
    -Dv4l2=true \
    -Dcam=enabled \
    -Dlc-compliance=disabled \
    -Dtest=false \
    -Ddocumentation=disabled \
"

RDEPENDS:${PN} = "${@bb.utils.contains('DISTRO_FEATURES', 'wayland qt', 'qtwayland', '', d)}"

inherit meson pkgconfig python3native

do_configure:prepend() {
    chmod 777 -R ${WORKDIR}
    sed -i -e 's|py_compile=True,||' ${S}/utils/ipc/mojo/public/tools/mojom/mojom/generate/template_expander.py
}

do_install:append() {
    chrpath -d ${D}${libdir}/libcamera.so
    chrpath -d ${D}${libexecdir}/libcamera/v4l2-compat.so
}

addtask do_recalculate_ipa_signatures_package after do_package before do_packagedata
do_recalculate_ipa_signatures_package() {
    local modules
    for module in $(find ${PKGD}/usr/lib/libcamera -name "*.so.sign"); do
        module="${module%.sign}"
        if [ -f "${module}" ] ; then
            modules="${modules} ${module}"
        fi
    done

    ${S}/src/ipa/ipa-sign-install.sh ${B}/src/ipa-priv-key.pem "${modules}"
}

FILES:${PN} += " ${libdir}/"
FILES:${PN} += " ${libexecdir}/libcamera/"
FILES:${PN} += " ${datadir}/"
FILES:${PN}-gst = "${libdir}/gstreamer-1.0"

# libcamera-v4l2 explicitly sets _FILE_OFFSET_BITS=32 to get access to
# both 32 and 64 bit file APIs.
GLIBC_64BIT_TIME_FLAGS = ""
