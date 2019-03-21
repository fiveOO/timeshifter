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

TODO