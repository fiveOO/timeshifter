# Timeshifter

This is a commandline tool for shifting date/time values to given timezones and prepare them
for further processing.

Initial intention was to fix timestamps in EXIF data of images. Therefore Timeshifter is
designed to integrate smoothly with
[ExifTool](https://sno.phy.queensu.ca/~phil/exiftool/ "ExifTool by Phil Harvey").

For a complete example on how I use it see [Fixing timestamps in EXIF](doc/exifExample.md).

All command line parameters are decribed in [Timeshifter CLI](doc/cli.md).

## Short examples
Most of the examples using a data file calles `example.csv`. You could find it at the bottom of this page.
### Timezone taken from given data
#### Input from file

```
java -jar timeshifter.jar -i example.csv -is 1
```
is printing the following to stdout (same as input but with two additional columns containing the shifted date with
and without timezone information)
```
NewImages/DSC00034.jpg,2019:03:09 17:58:00Z,2019:03:09 18:57:30+01:00,2019:03:09 18:58:00+01:00,2019:03:09 18:58:00
NewImages/DSC00035.jpg,2019:03:10 13:59:36Z,2019:03:10 14:59:06+01:00,2019:03:10 14:59:36+01:00,2019:03:10 14:59:36
```

#### Input from stdin

If you have some other application that is producing the data of the input file and is able to write them to stdout
it's possible to redirect the output of this application directly into timeshifter.
```
echo example.csv | java -jar timeshifter.jar -is 1
```

### Timezone given on command line

```
java -jar timeshifter.jar -i example.csv -is 1 -zo +03:00
```
is printing the following to stdout
```
NewImages/DSC00034.jpg,2019:03:09 17:58:00Z,2019:03:09 18:57:30+01:00,2019:03:09 20:58:00+03:00,2019:03:09 20:58:00
NewImages/DSC00035.jpg,2019:03:10 13:59:36Z,2019:03:10 14:59:06+01:00,2019:03:10 16:59:36+03:00,2019:03:10 16:59:36
```

### Change output timestamp format

```
java -jar timeshifter.jar -i example.csv -is 1 -od "dd. MMMM yyyy HH:mm:ss XXX"
```
is printing the following to stdout using default locale (here DE_de)
```
NewImages/DSC00034.jpg,2019:03:09 17:58:00Z,2019:03:09 18:57:30+01:00,09. März 2019 18:58:00 +01:00,09. März 2019 18:58:00
NewImages/DSC00035.jpg,2019:03:10 13:59:36Z,2019:03:10 14:59:06+01:00,10. März 2019 14:59:36 +01:00,10. März 2019 14:59:36
```
With standard Java parameters you could use another locale
```
java -Duser.country=CA -Duser.language=fr -jar timeshifter.jar -i example.csv -is 1 -od "dd. MMMM yyyy HH:mm:ss XXX"
```
```
NewImages/DSC00034.jpg,2019:03:09 17:58:00Z,2019:03:09 18:57:30+01:00,09. mars 2019 18:58:00 +01:00,09. mars 2019 18:58:00
NewImages/DSC00035.jpg,2019:03:10 13:59:36Z,2019:03:10 14:59:06+01:00,10. mars 2019 14:59:36 +01:00,10. mars 2019 14:59:36
```

### Change output line format

#### Using command line parameters

```
java -jar timeshifter.jar -i example.csv -is 1 -oh "<ul>%n" -ol "<li><a href="%1$s">Image %1$s</a> taken on %4$s</li>%n" -of "</ul>"
```
Be aware that you have to double `%` and `"` inside the parameters if you use this command inside a Windows Command Shell script.

```
<ul>
<li><a href="NewImages/DSC00034.jpg">Image NewImages/DSC00034.jpg</a> taken on 2019:03:09 18:58:00+01:00</li>
<li><a href="NewImages/DSC00035.jpg">Image NewImages/DSC00035.jpg</a> taken on 2019:03:10 14:59:36+01:00</li>
</ul>
```

#### Using parameter file

You could store all the parameters in a separate parameter file and pass this to Timeshifter.
```
java -jar timeshifter.jar @exampleHtml.param
```
uses `exampleHtml.param`
```
-i
example.csv

-is
1

-oh
<ul>%n

-ol
<li><a href="%1$s">Image %1$s</a> taken on %4$s</li>%n

-of
</ul>
```
to produce the same output as above.

For a more complex example on this have a look at [Fixing timestamps in EXIF](doc/exifExample.md).
### example.csv

Assume for the examples above that a file `example.csv` exists in the current folder like the following:
```
SourceFile,GPSDateTime,DateTimeDigitized
NewImages/DSC00034.jpg,2019:03:09 17:58:00Z,2019:03:09 18:57:30+01:00
NewImages/DSC00035.jpg,2019:03:10 13:59:36Z,2019:03:10 14:59:06+01:00
```