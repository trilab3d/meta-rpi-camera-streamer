# meta-rpi-camera-streamer

Yocto layer for raspberrypi awesome camera-streamer by ayufan

https://github.com/ayufan/camera-streamer

## Dependencies

  URI: https://github.com/agherzan/meta-raspberrypi
  branch: master

## Adding the meta-rpi-camera-streamer layer to your build

Run 'bitbake-layers add-layer meta-rpi-camera-streamer'


for proper camera detection, the following must be set in /boot/config.txt:

```
# Enable VC4 Graphics
dtoverlay=vc4-kms-v3d
# Enable RaspberryPi Camera(ov5647)
dtoverlay=ov5647
```

Available cameras are: imx219, imx290, imx296, imx477, imx519, imx708, ov5647, ov9281

Sample usage example ov5647:
```
/usr/bin/camera-streamer --camera-path=/base/soc/i2c0mux/i2c@1/ov5647@36 --camera-type=libcamera --camera-format=YUYV --camera-width=1296 --camera-height=972 --http-port=8080
```