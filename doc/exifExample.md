# Fixing timestamps in EXIF

This is an example how to use Timeshifter in the process of improving EXIF data of images after
downloading them from a camera.

## Analysis / requirements
### Desired results

- EXIF data
  * all timestamps should match the instant of `GPS:GPSDateStamp`/`GPS:GPSTimeStamp`
    (as I have a GPS tracker that gets the correct timestamp from satellite)
  * all timestamps (that support this) should have a timezone information
- image files
  * should be named based on the timestamp (e.g. `2019-01-01_13-00-00.jpg`)

### Initial situation

After [adding GPS information](addingGPSinformation.md) to my images I have the following situation:
- EXIF data
  * `GPS:GPSDateStamp`/`GPS:GPSTimeStamp` are set in UTC without timezone information
    (`2019:01:01 12:00:00`, that's the correct instant:exclamation:)
  * `XMP-exif:DateTimeDigitized`
    - is set to the camera's timestamp at taking the picture
    - DOES contain the correct timezone information:exclamation:
    - may differ in time (`2019:01:01 13:01:01+01:00`)
  * all other timestamps
  	- are set to the camera's timestamp at taking the picture
    - do NOT contain timezone information (`2019:01:01 13:01:01`)
    - differ in time
- image files
  * are named by the camera (`DSC00034.JPG`)

### Logical steps to go

- Fix timestamps
  1. take timestamp from `GPS:GPSDateStamp`/`GPS:GPSTimeStamp` (`2019:01:01 12:00:00`)
  2. take timezone information (`+01:00`) from `XMP-exif:DateTimeDigitized`
  3. shift the timestamp to this timezone (`2019:01:01 13:00:00+01:00`)
  4. apply the shifted timestamp to every timestamp in EXIF data (including timezone information where possible)
- Fix filenames
  1. rename file to the timestamp taken from an EXIF value WITHOUT timezone information
     (`2019:01:01 13:00:00` -> `2019-01-01_13-00-00.jpg`)

### Support by existing tools

- Fix timestamps
  * [ExifTool](https://sno.phy.queensu.ca/~phil/exiftool/ "ExifTool by Phil Harvey") is a great swiss army knife to
    EXIF data but I have not found a way to do the calculation/shifting described above in a few steps
  * Therefore I have created Timeshifter :wink:
- Fix filenames
  * that's an easy job using [ExifTool](https://sno.phy.queensu.ca/~phil/exiftool/ "ExifTool by Phil Harvey")

## Implementation

### timeshift.cmd

This batch script (Win10) is the only one to be called to start the whole processing described above.
```batch
@echo off
setlocal

rem Folder containing this batch script itself
set C_DIR=%~dp0
rem Folder containing images
set NEW_IMAGES_DIR=%C_DIR%NewImages
rem Folder containing tools like exiftool, timeshifter, ...
set TOOLS_DIR=%C_DIR%tools

%TOOLS_DIR%\exiftool -@ extractGpsAndTz.exiftool %NEW_IMAGES_DIR% | java -jar %TOOLS_DIR%\timeshifter.jar @adjustDates2Gps.timeshifter | %TOOLS_DIR%\exiftool -stay_open True -@ -
%TOOLS_DIR%\exiftool -@ rename2Timestamp.exiftool %NEW_IMAGES_DIR%
```

To keep it short and simple most of the parameters of ExifTool and Timeshifter are stored in parameter files.
They are described in the following.

### extractGpsAndTz.exiftool

This are the ExifTool parameters to extract the GPS timestamp (based on `GPS:GPSDateStamp`/`GPS:GPSTimeStamp`) and
`XMP-exif:DateTimeDigitized` from an image.
```
-n
-gpsdatetime
-datetimedigitized
-csv
-q
```
This will write a CSV with one line per image file in `NewImages` to stdout like the following:
```
SourceFile,GPSDateTime,DateTimeDigitized
NewImages/DSC00034.jpg,2019:03:09 17:58:00Z,2019:03:09 18:57:30+01:00
NewImages/DSC00035.jpg,2019:03:10 13:59:36Z,2019:03:10 14:59:06+01:00
```

### adjustDates2Gps.timeshifter

The CSV from above is piped into Timeshifter using the following parameters (for syntax see [Timeshifter CLI](cli.md)):
```
# skip header line
-ils
1

# output line per image using shifted date/time
# %1$s: file name
# %4$s: shifted date/time incl timezone information
# %5$s: shifted date/time excl timezone information
-olf
-n%n
-m%n
-P%n
-a%n
-overwrite_original%n
-System:FileCreateDate=%4$s%n
-System:FileModifyDate=%4$s%n
-AllDates=%4$s%n
-ExifIFD:CreateDate=%4$s%n
-ExifIFD:DateTimeOriginal=%4$s%n
-IFD0:ModifyDate=%4$s%n
-IFD1:ModifyDate=%4$s%n
-IPTC:TimeCreated=%4$s%n
-XMP-photoshop:DateCreated=%4$s%n
-XMP-exif:DateTime*=%4$s%n
-Sony:SonyDateTime=%5$s%n
%1$s%n
-execute%n

# footer line: stop exiftool from stay_open mode
-off
-stay_open%n
False%n
```

This will write commands for
[ExifTool in stay_open mode (search for -stay_open)](https://sno.phy.queensu.ca/~phil/exiftool/exiftool_pod.html)
to stdout like:
```
-n
-m
-P
-a
-overwrite_original
-System:FileCreateDate=2019:03:09 18:58:00+01:00
-System:FileModifyDate=2019:03:09 18:58:00+01:00
-AllDates=2019:03:09 18:58:00+01:00
-ExifIFD:CreateDate=2019:03:09 18:58:00+01:00
-ExifIFD:DateTimeOriginal=2019:03:09 18:58:00+01:00
-IFD0:ModifyDate=2019:03:09 18:58:00+01:00
-IFD1:ModifyDate=2019:03:09 18:58:00+01:00
-IPTC:TimeCreated=2019:03:09 18:58:00+01:00
-XMP-photoshop:DateCreated=2019:03:09 18:58:00+01:00
-XMP-exif:DateTime*=2019:03:09 18:58:00+01:00
-Sony:SonyDateTime=2019:03:09 18:58:00
NewImages/DSC00034.jpg
-execute
-n
-m
-P
-a
-overwrite_original
-System:FileCreateDate=2019:03:10 14:59:36+01:00
-System:FileModifyDate=2019:03:10 14:59:36+01:00
-AllDates=2019:03:10 14:59:36+01:00
-ExifIFD:CreateDate=2019:03:10 14:59:36+01:00
-ExifIFD:DateTimeOriginal=2019:03:10 14:59:36+01:00
-IFD0:ModifyDate=2019:03:10 14:59:36+01:00
-IFD1:ModifyDate=2019:03:10 14:59:36+01:00
-IPTC:TimeCreated=2019:03:10 14:59:36+01:00
-XMP-photoshop:DateCreated=2019:03:10 14:59:36+01:00
-XMP-exif:DateTime*=2019:03:10 14:59:36+01:00
-Sony:SonyDateTime=2019:03:10 14:59:36
NewImages/DSC00035.jpg
-execute
-stay_open
False
```
This output is piped to ExifTool to be processed in stay_open mode.

This results in having all EXIF data fixed of all files stored in folder `NewImages`.

### rename2Timestamp.exiftool

The second command calls ExifTool to rename the files to the timestamp of `CreateDate`:
```
-d
%Y-%m-%d_%H-%M-%S.%%e
-filename<CreateDate
```

That's all :wink:

## Test environment
- Windows 10
- ExifTool 11.32
- Java 8
- Sony RX100 M3
