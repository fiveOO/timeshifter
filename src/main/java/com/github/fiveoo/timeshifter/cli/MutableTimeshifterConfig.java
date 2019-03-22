package com.github.fiveoo.timeshifter.cli;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.validators.PositiveInteger;
import com.github.fiveoo.timeshifter.TimeshifterConfig;

public class MutableTimeshifterConfig
    implements
        TimeshifterConfig
{
    private static final String PARAM_NAME_SKIP_LINES_LONG               = "--inSkipLines";
    private static final String PARAM_NAME_FIELD_CONTAINING_OFFSET_SHORT = "-fo";
    private static final String PARAM_NAME_OUTPUT_LINE_SHORT             = "-ol";

    public static final String            DEFAULT_DATE_TIME_PATTERN   = "yyyy:MM:dd HH:mm:ssXXX";
    public static final DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern( DEFAULT_DATE_TIME_PATTERN );

    /* -- Input parameters -- */
    @Parameter(names = { "-i", "--in" }, description = "Source file. Default: stdin")
    private String inputFileName;

    @Parameter(names = { "-id", "--inDateFormat" }, description = "Format to parse dates in input data.")
    private String inputDateFormat = DEFAULT_DATE_TIME_PATTERN;

    @Parameter(names = { "-is", PARAM_NAME_SKIP_LINES_LONG }, validateWith = PositiveInteger.class,
            description = "Number of lines at the beginning of input to skip (e.g. " + PARAM_NAME_SKIP_LINES_LONG
                    + " 1  for ignoring the header line of a CSV file).")
    private int inputLinesToSkip = 0;

    @Parameter(names = { "-fs", "--fieldToShift" },
            description = "Index of the field containing the date/time to shift. The index is 0-based.")
    private int fieldIdxToShift = 1;

    @Parameter(names = { PARAM_NAME_FIELD_CONTAINING_OFFSET_SHORT, "--fieldContainingOffset" },
            description = "Index of the field containing an offset used to shift. The index is 0-based.")
    private int fieldIdxOfOffset = 2;

    @Parameter(names = { "-zo", "--offset" },
            description = "Fix zone offset (e.g. +02:00) for all lines. If this is set "
                    + PARAM_NAME_FIELD_CONTAINING_OFFSET_SHORT + " will not be evaluated.",
            converter = ZoneOffsetConverter.class)
    private ZoneOffset fixOffset;

    /* -- Output parameters -- */
    @Parameter(names = { "-o", "--out" }, description = "Destination file. Default: stdout")
    private String outputFileName;

    @Parameter(names = { "-od", "--outDateFormat" }, description = "Format of shifted dates in output data.")
    private String outputDateFormat = DEFAULT_DATE_TIME_PATTERN;

    @Parameter(names = { PARAM_NAME_OUTPUT_LINE_SHORT, "--outLineFormat" }, variableArity = true,
            description = "Format of an output line. You can pass several parts of the output line as separate "
                    + "parameters (e.g. \"" + PARAM_NAME_OUTPUT_LINE_SHORT + " abc xyz\" is equivalent to \""
                    + PARAM_NAME_OUTPUT_LINE_SHORT + " abcxyz\"). This way may be "
                    + "useful/more readable if you pass the parameters using an @ file. %n will trigger a line break. "
                    + "Default: same as input line plus two fields at the end: "
                    + "one for shifted date/time including timezone information, "
                    + "one for shifted date/time without timezone information")
    private List<String> outputLineFormat = new ArrayList<>();

    @Parameter(names = { "-oh", "--outHeader" }, variableArity = true,
            description = "Header line to be written to the output before the first record. %n will trigger a line break.")
    private List<String> outputHeaderLine = new ArrayList<>();

    @Parameter(names = { "-of", "--outFooter" }, variableArity = true,
            description = "Footer line to be written to the output after the last record. %n will trigger a line break.")
    private List<String> outputFooterLine = new ArrayList<>();

    @Override
    public String getInputFileName()
    {
        return inputFileName;
    }

    public void setInputFileName( final String inputFileName )
    {
        this.inputFileName = inputFileName;
    }

    public String getInputDateFormat()
    {
        return inputDateFormat;
    }

    public void setInputDateFormat( final String inputDateFormat )
    {
        this.inputDateFormat = inputDateFormat;
    }

    @Override
    public int getInputLinesToSkip()
    {
        return inputLinesToSkip;
    }

    public void setInputLinesToSkip( final int inputLinesToSkip )
    {
        this.inputLinesToSkip = inputLinesToSkip;
    }

    @Override
    public int getFieldIdxToShift()
    {
        return fieldIdxToShift;
    }

    public void setFieldIdxToShift( final int fieldIdxToShift )
    {
        this.fieldIdxToShift = fieldIdxToShift;
    }

    @Override
    public int getFieldIdxOfOffset()
    {
        return fieldIdxOfOffset;
    }

    public void setFieldIdxOfOffset( final int fieldIdxOfOffset )
    {
        this.fieldIdxOfOffset = fieldIdxOfOffset;
    }

    @Override
    public ZoneOffset getFixOffset()
    {
        return fixOffset;
    }

    public void setFixOffset( final ZoneOffset fixOffset )
    {
        this.fixOffset = fixOffset;
    }

    @Override
    public String getOutputFileName()
    {
        return outputFileName;
    }

    public void setOutputFileName( final String outputFileName )
    {
        this.outputFileName = outputFileName;
    }

    public void setOutputDateFormat( final String outputDateFormat )
    {
        this.outputDateFormat = outputDateFormat;
    }

    @Override
    public String getOutputLineFormat()
    {
        if( outputLineFormat.size() == 0 )
        {
            return null;
        }
        return String.join( "", outputLineFormat.toArray( new String[outputLineFormat.size()] ) );
    }

    public void setOutputLineFormat( final String outputLineFormat )
    {
        this.outputLineFormat = new ArrayList<>();
        this.outputLineFormat.add( outputLineFormat );
    }

    @Override
    public String getOutputHeaderLine()
    {
        if( outputHeaderLine.size() == 0 )
        {
            return null;
        }
        return String.join( "", outputHeaderLine.toArray( new String[outputHeaderLine.size()] ) );
    }

    public void setOutputHeaderLine( final String[] outputHeaderLine )
    {
        this.outputHeaderLine = new ArrayList<>();
        if( outputHeaderLine != null )
        {
            this.outputHeaderLine.addAll( Arrays.asList( outputHeaderLine ) );
        }
    }

    @Override
    public String getOutputFooterLine()
    {
        if( outputFooterLine.size() == 0 )
        {
            return null;
        }
        return String.join( "", outputFooterLine.toArray( new String[outputFooterLine.size()] ) );
    }

    public void setOutputFooterLine( final String[] outputFooterLine )
    {
        this.outputFooterLine = new ArrayList<>();
        if( outputFooterLine != null )
        {
            this.outputFooterLine.addAll( Arrays.asList( outputFooterLine ) );
        }
    }

    /*-- Calculated values --*/
    @Override
    public DateTimeFormatter getInputDateFormatter()
    {
        return inputDateFormat == null ? DEFAULT_DATE_TIME_FORMATTER : DateTimeFormatter.ofPattern( inputDateFormat );
    }

    @Override
    public DateTimeFormatter getOutputDateFormatter()
    {
        return outputDateFormat == null ? DEFAULT_DATE_TIME_FORMATTER : DateTimeFormatter.ofPattern( outputDateFormat );
    }

    @Override
    public DateTimeFormatter getOutputDateFormatterLocal()
    {
        final String outputFormatGlobal = outputDateFormat == null ? DEFAULT_DATE_TIME_PATTERN : outputDateFormat;
        final String outputFormatLocal = outputFormatGlobal.replaceAll( "[^'][0VXxZz]+", "" );

        return DateTimeFormatter.ofPattern( outputFormatLocal );
    }
}
