SUMMARY = "camera-streamer - Yet another camera streamer"
HOMEPAGE = "https://github.com/ayufan/camera-streamer"
LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://${WORKDIR}/gpl-3.0.txt;md5=1ebbd3e34237af26da5dc08a4e440464"

SECTION = "multimedia"
TARGET_CC_ARCH += "${LDFLAGS}"

SRC_URI = "git://github.com/ayufan/camera-streamer.git;protocol=https;branch=master \
           file://fix-error-no-unused-result.patch \
           file://gpl-3.0.txt"

SRCREV = "${AUTOREV}"

DEPENDS = "gstreamer1.0 gstreamer1.0-plugins-base gstreamer1.0-plugins-good nlohmann-json vim-native rpi-libcamera"

IMAGE_INSTALL:append = "camera-streamer nlohmann-json v4l-utils rpi-libcamera rpi-libcamera-apps"

S = "${WORKDIR}/git"

inherit pkgconfig

do_compile() {
    oe_runmake
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${S}/camera-streamer ${D}${bindir}
}

FILES:${PN} += "${bindir}/camera-streamer" 

do_deploy() {
    config_file="${DEPLOY_DIR_IMAGE}/${BOOTFILES_DIR_NAME}/config.txt"
    sed -i 's/imx219/ov5647/g' $config_file
}

addtask deploy before do_build after do_install
