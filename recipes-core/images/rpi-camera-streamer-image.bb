# Base this image on core-image-base
include recipes-core/images/core-image-base.bb

COMPATIBLE_MACHINE = "^rpi$"

#IMAGE_INSTALL:append = " camera-streamer nlohmann-json v4l-utils libcamera libcamera-apps"
IMAGE_INSTALL:append = " camera-streamer nlohmann-json v4l-utils libcamera"
