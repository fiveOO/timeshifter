package com.github.fiveoo.timeshifter.cli;

import static com.github.fiveoo.timeshifter.cli.Constants.JAVADOC_BASE_URL;

import java.time.ZoneId;
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
    private static final String PARAM_NAME_IN_LINES_SKIP_LONG       = "--inLinesSkip";
    private static final String PARAM_NAME_IN_DATE_OFFSET_IDX_SHORT = "-iox";
    private static final String PARAM_NAME_OUTPUT_LINE_SHORT        = "-olf";

    public static final String            DEFAULT_DATE_TIME_PATTERN   = "yyyy:MM:dd HH:mm:ssXXX";
    public static final DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern( DEFAULT_DATE_TIME_PATTERN );

    /* -- Input parameters -- */
    @Parameter(names = { "-i", "--in" }, description = "Source file. Default: stdin")
    private String inputFileName;

    @Parameter(names = { "-isf", "--inDateShiftFormat" }, description = "Format of the date to shift in input data.")
    private String inDateShiftFormat = DEFAULT_DATE_TIME_PATTERN;

    @Parameter(names = { "-isz", "--inDateShiftZone" },
            description = "Timezone of the date to shift in input data if not contained in the field itself. "
                    + "For valid values see " + JAVADOC_BASE_URL + "java/time/ZoneId.html#of-java.lang.String-",
            converter = ZoneIdConverter.class)
    private ZoneId inDateShiftZone;

    @Parameter(names = { "-isx", "--inDateShiftIdx" },
            description = "Index of the field containing the date/time to shift in input data. The index is 0-based.")
    private int inDateShiftIdx = 1;

    @Parameter(names = { "-iof", "--inDateOffsetFormat" },
            description = "Format of the date to take offset from in input data.")
    private String inDateOffsetFormat = DEFAULT_DATE_TIME_PATTERN;

    @Parameter(names = { PARAM_NAME_IN_DATE_OFFSET_IDX_SHORT, "--inDateOffsetIdx" },
            description = "Index of the field to take the offset from to shift. The index is 0-based.")
    private int inDateOffsetIdx = 2;

    @Parameter(names = { "-ils", PARAM_NAME_IN_LINES_SKIP_LONG }, validateWith = PositiveInteger.class,
            description = "Number of lines at the beginning of input to skip (e.g. " + PARAM_NAME_IN_LINES_SKIP_LONG
                    + " 1  for ignoring the header line of a CSV file).")
    private int inLinesSkip = 0;

    /* -- Output parameters -- */
    @Parameter(names = { "-o", "--out" }, description = "Destination file. Default: stdout")
    private String outputFileName;

    @Parameter(names = { "-oso", "--outDateShiftedOffset" },
            description = "Fix zone offset (e.g. +02:00) for all lines. If this is set "
                    + PARAM_NAME_IN_DATE_OFFSET_IDX_SHORT + " will not be evaluated. "
                    + "For valid values see " + JAVADOC_BASE_URL + "java/time/ZoneOffset.html#of-java.lang.String-",
            converter = ZoneOffsetConverter.class)
    private ZoneOffset outDateShiftedOffset;

    @Parameter(names = { "-osf", "--outDateShiftedFormat" }, description = "Format of shifted dates in output data.")
    private String outDateShiftedFormat = DEFAULT_DATE_TIME_PATTERN;

    @Parameter(names = { PARAM_NAME_OUTPUT_LINE_SHORT, "--outLineFormat" }, variableArity = true,
            description = "Format of an output line. You can pass several parts of the output line as separate "
                    + "parameters (e.g. \"" + PARAM_NAME_OUTPUT_LINE_SHORT + " abc xyz\" is equivalent to \""
                    + PARAM_NAME_OUTPUT_LINE_SHORT + " abcxyz\"). This way may be "
                    + "useful/more readable if you pass the parameters using an @ file. %n will trigger a line break. "
                    + "Default: same as input line plus two fields at the end: "
                    + "one for shifted date/time including timezone information, "
                    + "one for shifted date/time without timezone information")
    private List<String> outputLineFormat = new ArrayList<>();

    @Parameter(names = { "-ohf", "--outHeaderFormat" }, variableArity = true,
            description = "Header to be written to the output before the first line of data. %n will trigger a line break.")
    private List<String> outHeaderFormat = new ArrayList<>();

    @Parameter(names = { "-off", "--outFooterFormat" }, variableArity = true,
            description = "Footer to be written to the output after the last line of data. %n will trigger a line break.")
    private List<String> outFooterFormat = new ArrayList<>();

    @Override
    public String getInputFileName()
    {
        return inputFileName;
    }

    public void setInputFileName( final String inputFileName )
    {
        this.inputFileName = inputFileName;
    }

    public String getInDateShiftFormat()
    {
        return inDateShiftFormat;
    }

    public void setInDateShiftFormat( final String inDateShiftFormat )
    {
        this.inDateShiftFormat = inDateShiftFormat;
    }

    public ZoneId getInDateShiftZone()
    {
        return inDateShiftZone;
    }

    public void setInDateShiftZone( final ZoneId inDateShiftZone )
    {
        this.inDateShiftZone = inDateShiftZone;
    }

    @Override
    public int getInDateShiftIdx()
    {
        return inDateShiftIdx;
    }

    public void setInDateShiftIdx( final int inDateShiftIdx )
    {
        this.inDateShiftIdx = inDateShiftIdx;
    }

    public String getInDateOffsetFormat()
    {
        return inDateOffsetFormat;
    }

    public void setInDateOffsetFormat( final String inDateOffsetFormat )
    {
        this.inDateOffsetFormat = inDateOffsetFormat;
    }

    @Override
    public int getInDateOffsetIdx()
    {
        return inDateOffsetIdx;
    }

    public void setInDateOffsetIdx( final int inDateOffsetIdx )
    {
        this.inDateOffsetIdx = inDateOffsetIdx;
    }

    @Override
    public int getInLinesSkip()
    {
        return inLinesSkip;
    }

    public void setInLinesSkip( final int inputLinesToSkip )
    {
        this.inLinesSkip = inputLinesToSkip;
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

    @Override
    public ZoneOffset getOutDateShiftedOffset()
    {
        return outDateShiftedOffset;
    }

    public void setOutDateShiftedOffset( final ZoneOffset outDateShiftedOffset )
    {
        this.outDateShiftedOffset = outDateShiftedOffset;
    }

    public String getOutDateShiftedFormat()
    {
        return outDateShiftedFormat;
    }

    public void setOutDateShiftedFormat( final String outDateShiftedFormat )
    {
        this.outDateShiftedFormat = outDateShiftedFormat;
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
    public String getOutHeaderFormat()
    {
        if( outHeaderFormat.size() == 0 )
        {
            return null;
        }
        return String.join( "", outHeaderFormat.toArray( new String[outHeaderFormat.size()] ) );
    }

    public void setOutHeaderFormat( final String[] outHeaderFormat )
    {
        this.outHeaderFormat = new ArrayList<>();
        if( outHeaderFormat != null )
        {
            this.outHeaderFormat.addAll( Arrays.asList( outHeaderFormat ) );
        }
    }

    @Override
    public String getOutFooterFormat()
    {
        if( outFooterFormat.size() == 0 )
        {
            return null;
        }
        return String.join( "", outFooterFormat.toArray( new String[outFooterFormat.size()] ) );
    }

    public void setOutFooterFormat( final String[] outFooterFormat )
    {
        this.outFooterFormat = new ArrayList<>();
        if( outFooterFormat != null )
        {
            this.outFooterFormat.addAll( Arrays.asList( outFooterFormat ) );
        }
    }

    /*-- Calculated values --*/
    @Override
    public DateTimeFormatter getInDateShiftFormatter()
    {
        return getInDateShiftFormat() == null ? DEFAULT_DATE_TIME_FORMATTER
                : DateTimeFormatter.ofPattern( getInDateShiftFormat() );
    }

    @Override
    public DateTimeFormatter getInDateOffsetFormatter()
    {
        return getInDateOffsetFormat() == null ? DEFAULT_DATE_TIME_FORMATTER
                : DateTimeFormatter.ofPattern( getInDateOffsetFormat() );
    }

    @Override
    public DateTimeFormatter getOutDateShiftedFormatter()
    {
        return getOutDateShiftedFormat() == null ? DEFAULT_DATE_TIME_FORMATTER
                : DateTimeFormatter.ofPattern( getOutDateShiftedFormat() );
    }

    @Override
    public DateTimeFormatter getOutDateShiftedFormatterLocal()
    {
        final String outputFormatGlobal =
                getOutDateShiftedFormat() == null ? DEFAULT_DATE_TIME_PATTERN : getOutDateShiftedFormat();
        final String outputFormatLocal = outputFormatGlobal.replaceAll( "([^'])[0VXxZz]+", "$1" );

        return DateTimeFormatter.ofPattern( outputFormatLocal );
    }
}
