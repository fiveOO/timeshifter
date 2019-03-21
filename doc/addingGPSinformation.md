# My way of adding GPS information to images

I'm using a [GPS tracker](https://www.columbus-gps.de/produkte/columbus-v-990-gps-logger)
to record my tracks.

At home I'm converting the track data to GPX and write GPS information to my images
using [GeoSetter](https://www.geosetter.de/en/main-en/).

GeoSetter gives me the possibility to define the time-lag of camera time and GPS time by
selecting one image and one waypoint of the track as the same instant. The calculated
time-lag is used to interpolate the coordinates of the images based on track coordinates
only.

If no GPS track / GPS coordinate is available or the track was not exact enough every
image location could be set / corrected inside GeoSetter.

GeoSetter stores the following information in EXIF data:
- GPS location
- `GPS:GPSDateStamp`/`GPS:GPSTimeStamp` as recorded by the tracker in UTC without timezone
  information
- `XMP-exif:DateTimeDigitized`
  * as recorded by the camera at taking the picture
  * including the correct timezone information based on the GPS location and timestamp
