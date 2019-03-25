# Timeshifter CLI

## Help

Calling Timeshifter with option `-h` shows the following short description of the
available options:
```
Usage: java -jar timeshifter-<version>.jar [options]
  Options:
    -fo, --fieldContainingOffset
      Index of the field containing an offset used to shift. The index is
      0-based.
      Default: 2
    -fs, --fieldToShift
      Index of the field containing the date/time to shift. The index is
      0-based.
      Default: 1
    -h, --help
      Shows this help
    -i, --in
      Source file. Default: stdin
    -id, --inDateFormat
      Format to parse dates in input data.
      Default: yyyy:MM:dd HH:mm:ssXXX
    -is, --inSkipLines
      Number of lines at the beginning of input to skip (e.g. --skip 1  for
      ignoring the header line of a CSV file).
      Default: 0
    -zo, --offset
      Fix zone offset (e.g. +02:00) for all lines. If this is set -fo will not
      be evaluated.
    -o, --out
      Destination file. Default: stdout
    -od, --outDateFormat
      Format of shifted dates in output data.
      Default: yyyy:MM:dd HH:mm:ssXXX
    -of, --outFooter
      Footer line to be written to the output after the last record. %n will
      trigger a line break.
      Default: []
    -oh, --outHeader
      Header line to be written to the output before the first record. %n will
      trigger a line break.
      Default: []
    -ol, --outLineFormat
      Format of an output line. You can pass several parts of the output line
      as separate parameters (e.g. "-ol abc xyz" is equivalent to "-ol
      abcxyz"). This way may be useful/more readable if you pass the parameters
      using an @ file. %n will trigger a line break. Default: same as input
      line plus two fields at the end: one for shifted date/time including
      timezone information, one for shifted date/time without timezone
      information
      Default: []

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
- ONE value: `-ol "So Long, and Thanks for All the Fish"` by enclosing it in double quotes (Windows) / single quotes (*nix)
  or as
- a LIST of "words": `-ol So Long, and Thanks for All the Fish` which is taken by the OS as a list of nine options
  as the "words" are separated by spaces.
  In that case each "word" found will be added to the value of `-ol` as long as the "word" does not start with a
  defined option name like `-is`.

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
-ol
So Long, and Thanks for All the Fish
```
is equivalent to
```
-ol
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
