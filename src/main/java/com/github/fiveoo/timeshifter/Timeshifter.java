package com.github.fiveoo.timeshifter;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Formatter;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

public class Timeshifter
{
    private final TimeshifterConfig config;

    public Timeshifter( final TimeshifterConfig config )
    {
        this.config = config;
    }

    public void shift( final Reader in, final Writer out )
        throws IOException
    {
        try (final CSVReader cvsReader = new CSVReaderBuilder( in )
                .withCSVParser( new CSVParserBuilder().withSeparator( ',' ).withQuoteChar( '"' ).build() )
                .withSkipLines( config.getInputLinesToSkip() ).build())
        {
            shift( StreamSupport.stream( cvsReader.spliterator(), false ).map( this::cleanupFields ), out );
        }
    }

    protected void shift( final Stream<String[]> dataStream, final Writer out )
    {
        try (final Stream<String> streamOfLines = shift( dataStream ))
        {
            streamOfLines.forEach( line -> write( line, out ) );
        }
    }

    protected Stream<String> shift( final Stream<String[]> dataStream )
    {
        final Stream<String> headerLineStream = Stream.of( config.getOutputHeaderLine() ).map( this::formatFixPattern );
        final Stream<String> footerLineStream = Stream.of( config.getOutputFooterLine() ).map( this::formatFixPattern );

        return Stream.concat( Stream.concat( headerLineStream, transformLines( dataStream ) ), footerLineStream );
    }

    protected Stream<String> transformLines( final Stream<String[]> dataStream )
    {
        return dataStream.map( this::transformLine );
    }

    protected String[] cleanupFields( final String[] values )
    {
        for( int i = 0; i < values.length; i++ )
        {
            values[i] = values[i] == null ? "" : values[i].trim();
        }

        return values;
    }

    protected String transformLine( final String[] values )
    {
        try
        {
            return formatOutputLine( values, createShiftedTime( values ) );
        }
        catch( final RuntimeException e )
        {
            logError( "Could not shift time of input data: " + String.join( ",", values ), e );
        }

        return "";
    }

    protected String formatOutputLine( final String[] inputFields, final OffsetDateTime shiftedTime )
    {
        final Object[] outData = Arrays.copyOf( inputFields, inputFields.length + 2 );
        outData[outData.length - 2] = shiftedTime.format( config.getOutputDateFormatter() );
        outData[outData.length - 1] = shiftedTime.format( config.getOutputDateFormatterLocal() );

        String format = config.getOutputLineFormat();
        if( format == null )
        {
            format = createDefaultOutputFormat( outData.length );
        }
        return String.format( format, outData );
    }

    protected String createDefaultOutputFormat( final int length )
    {
        final StringBuilder b = new StringBuilder();
        for( int i = 0; i < length; i++ )
        {
            if( i > 0 )
            {
                b.append( ',' );
            }
            b.append( '%' ).append( i + 1 ).append( "$s" );
        }

        return b.toString();
    }

    protected OffsetDateTime createShiftedTime( final String[] values )
    {
        if( config.getFieldIdxToShift() >= values.length )
        {
            throw TimeshifterException.tooLessFields( values.length, config.getFieldIdxToShift() + 1 );
        }

        if( config.getFixOffset() == null )
        {
            if( config.getFieldIdxOfOffset() >= values.length )
            {
                throw TimeshifterException.tooLessFields( values.length, config.getFieldIdxOfOffset() + 1 );
            }

            return createShiftedTime( values[config.getFieldIdxToShift()], values[config.getFieldIdxOfOffset()] );
        }
        else
        {
            return createShiftedTime( values[config.getFieldIdxToShift()], config.getFixOffset() );
        }
    }

    protected OffsetDateTime createShiftedTime( final String dateTimeToShiftStr,
            final String dateTimeToTakeZoneOffsetStr )
    {
        final ZoneOffset offset =
                OffsetDateTime.parse( dateTimeToTakeZoneOffsetStr, config.getInputDateFormatter() ).getOffset();

        return createShiftedTime( dateTimeToShiftStr, offset );
    }

    protected OffsetDateTime createShiftedTime( final String dateTimeToShiftStr, final ZoneOffset offset )
    {
        return applyOffset( OffsetDateTime.parse( dateTimeToShiftStr, config.getInputDateFormatter() ), offset );
    }

    protected OffsetDateTime applyOffset( final OffsetDateTime dateTime, final ZoneOffset offset )
    {
        return dateTime.withOffsetSameInstant( offset );
    }

    protected String formatFixPattern( final String pattern )
    {
        if( pattern == null || pattern.isEmpty() )
        {
            return "";
        }

        final StringWriter writer = new StringWriter();
        try (Formatter f = new Formatter( writer ))
        {
            f.format( pattern );
        }

        return writer.toString();
    }

    protected void write( final String line, final Writer out )
    {
        try
        {
            out.write( line );
        }
        catch( final IOException e )
        {
            throw TimeshifterException.errorWritingOutput( e );
        }
    }

    private void logError( final String msg, final Exception e )
    {
        System.err.println( msg + "  " + e );
    }
}
