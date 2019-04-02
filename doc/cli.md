# Timeshifter CLI

## Help

Calling Timeshifter with option `-h` shows the following short description of the
available options:
```
Usage: java -jar timeshifter-<version>.jar [options]
  Options:
    -h, --help
      Shows this help
    -i, --in
      Source file. Default: stdin
    -iof, --inDateOffsetFormat
      Format of the date to take offset from in input data.
      Default: yyyy:MM:dd HH:mm:ssXXX
    -iox, --inDateOffsetIdx
      Index of the field to take the offset from to shift. The index is
      0-based.
      Default: 2
    -isf, --inDateShiftFormat
      Format of the date to shift in input data.
      Default: yyyy:MM:dd HH:mm:ssXXX
    -isx, --inDateShiftIdx
      Index of the field containing the date/time to shift in input data. The
      index is 0-based.
      Default: 1
    -isz, --inDateShiftZone
      Timezone of the date to shift in input data if not contained in the field
      itself. For valid values see https://docs.oracle.com/javase/8/docs/api/java/time/ZoneId.html#of-java.lang.String-
    -ils, --inLinesSkip
      Number of lines at the beginning of input to skip (e.g. --inLinesSkip 1
      for ignoring the header line of a CSV file).
      Default: 0
    -o, --out
      Destination file. Default: stdout
    -osf, --outDateShiftedFormat
      Format of shifted dates in output data.
      Default: yyyy:MM:dd HH:mm:ssXXX
    -oso, --outDateShiftedOffset
      Fix zone offset (e.g. +02:00) for all lines. If this is set -iox will not
      be evaluated. For valid values see https://docs.oracle.com/javase/8/docs/api/java/time/ZoneOffset.html#of-java.lang.String-
    -off, --outFooterFormat
      Footer to be written to the output after the last line of data. %n will
      trigger a line break.
      Default: []
    -ohf, --outHeaderFormat
      Header to be written to the output before the first line of data. %n will
      trigger a line break.
      Default: []
    -olf, --outLineFormat
      Format of an output line. You can pass several parts of the output line
      as separate parameters (e.g. "-olf abc xyz" is equivalent to "-olf
      abcxyz"). This way may be useful/more readable if you pass the parameters
      using an @ file. %n will trigger a line break. Default: same as input
      line plus two fields at the end: one for shifted date/time including
      timezone information, one for shifted date/time without timezone
      information
      Default: []

For help on date/time formats see https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html

Error codes:
  < 0: some error occurred; see error message
  = 0: everything's fine
```

## Details

### Date / time formatting / parsing (input / output)

Date / time formatting is done using the class `DateTimeFormatter`. For information of the possible formatting
pattern / symbols have a look at the
[JavaDoc of DateTimeFormatter](https://docs.oracle.com/javase/8/docs/api/index.html?java/time/format/DateTimeFormatter.html)
section "Patterns for Formatting and Parsing".

### Output line formatting

Formatting of the output line is done using the class `Formatter`. For information of the possible formatting
pattern / symbols have a look at the
[JavaDoc of Formatter](https://docs.oracle.com/javase/8/docs/api/index.html?java/util/Formatter.html).

### outLineFormat / outHeader / outFooter

Values of these options can be given as
- ONE value: `-olf "So Long, and Thanks for All the Fish"` by enclosing it in double quotes (Windows) / single quotes (*nix)
  or as
- a LIST of "words": `-olf So Long, and Thanks for All the Fish` which is taken by the OS as a list of nine options
  as the "words" are separated by spaces and not enclosed in double / single quotes.
  In that case each "word" found will be added to the value of `-olf` as long as the "word" does not start with a
  defined option name like `-ils`.

### Using a parameter file

You could store all parameters in a separate parameter file and pass this to Timeshifter like
`java -jar timeshifter.jar @example.param`.

The requirements are very similar to ExifTools ARGFILEs.
> The file contains one argument per line (NOT one option per line -- some options require additional arguments, and all
> arguments must be placed on separate lines). Blank lines and lines beginning with # are ignored ... White space at the
> start of a line is removed. Normal shell processing of arguments is not performed, which among other things means that
> arguments should not be quoted and spaces are treated as any other character.
>
> -- <cite>Phil Harvey</cite>

```
-olf
So Long, and Thanks for All the Fish
```
is equivalent to
```
-olf
So
Long,
and
Thanks
for
All
the
Fish
```
These two variants correspond to the two ways of passing options described above.
