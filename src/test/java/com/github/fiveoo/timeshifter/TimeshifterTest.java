package com.github.fiveoo.timeshifter;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;

import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.github.fiveoo.timeshifter.cli.MutableTimeshifterConfig;

public class TimeshifterTest
{
    private MutableTimeshifterConfig config;
    private Timeshifter              cut;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp()
        throws Exception
    {
        config = new MutableTimeshifterConfig();
        cut = new Timeshifter( config );
    }

    @Test
    public void shiftCsvShouldProcessValidExampleFile()
        throws IOException
    {
        final StringWriter writer = new StringWriter();

        cut.shiftCsv( new InputStreamReader( getClass().getResourceAsStream( "exampleInputValid.txt" ),
                StandardCharsets.UTF_8 ), writer );

        assertThat( writer.toString(),
                equalTo( "NewImages/DSC00034.jpg,2019:03:09 17:58:00Z,2019:03:09 18:57:30+01:00,2019:03:09 18:58:00+01:00,2019:03:09 18:58:0"
                        + System.lineSeparator()
                        + "NewImages/DSC00035.jpg,2019:03:10 13:59:36Z,2019:03:10 14:59:06+01:00,2019:03:10 14:59:36+01:00,2019:03:10 14:59:36"
                        + System.lineSeparator() ) );
    }

    @Test
    public void createShiftedTimeShouldShiftAllCasesCorrectly()
    {
        assertThat( cut.createShiftedTime( "2019:03:13 09:14:27Z", "2019:03:13 10:10:00Z" ), equalTo( OffsetDateTime
                .parse( "2019:03:13 09:14:27Z", MutableTimeshifterConfig.DEFAULT_DATE_TIME_FORMATTER ) ) );

        assertThat( cut.createShiftedTime( "2019:03:13 09:14:27Z", "2019:03:13 10:10:00+00:00" ),
                equalTo( OffsetDateTime.parse( "2019:03:13 09:14:27Z",
                        MutableTimeshifterConfig.DEFAULT_DATE_TIME_FORMATTER ) ) );

        assertThat( cut.createShiftedTime( "2019:03:13 09:14:27Z", "2019:03:13 10:10:00+04:30" ),
                equalTo( OffsetDateTime.parse( "2019:03:13 13:44:27+04:30",
                        MutableTimeshifterConfig.DEFAULT_DATE_TIME_FORMATTER ) ) );

        assertThat( cut.createShiftedTime( "2019:03:13 09:14:27Z", "2019:03:13 10:10:00-04:30" ),
                equalTo( OffsetDateTime.parse( "2019:03:13 04:44:27-04:30",
                        MutableTimeshifterConfig.DEFAULT_DATE_TIME_FORMATTER ) ) );

        assertThat( cut.createShiftedTime( "2019:03:13 09:14:27+01:00", "2019:03:13 10:10:00-04:30" ),
                equalTo( OffsetDateTime.parse( "2019:03:13 03:44:27-04:30",
                        MutableTimeshifterConfig.DEFAULT_DATE_TIME_FORMATTER ) ) );
    }

    @Test
    public void transformLinesShouldSkipNotParseableLines()
        throws IOException
    {
        final String[] lineOk = new String[] { "infoData", "2019:03:13 09:14:27Z", "2019:03:13 10:10:00+04:30" };
        final String[] lineNok = new String[] { "infoData", "-not parseable date-", "2019:03:13 10:10:00+04:30" };

        final List<String> resultList =
                cut.transformLines( Arrays.asList( lineNok, lineOk, lineNok ).stream() ).collect( Collectors.toList() );

        assertThat( resultList, hasSize( 1 ) );
        assertThat( resultList,
                hasItem( "infoData,2019:03:13 09:14:27Z,2019:03:13 10:10:00+04:30,2019:03:13 13:44:27+04:30,2019:03:13 13:44:27"
                        + System.lineSeparator() ) );
    }

    @Test
    public void transformLineShouldNotThrowExceptionIfNotParseable()
        throws IOException
    {
        final String[] line = new String[] { "infoData", "-not parseable date-", "2019:03:13 10:10:00+04:30" };

        assertThat( cut.transformLine( line ), equalTo( "" ) );
    }

    @Test
    public void transformLineShouldUseDefaultFormat()
        throws IOException
    {
        final String[] line = new String[] { "infoData", "2019:03:13 09:14:27Z", "2019:03:13 10:10:00+04:30" };

        assertThat( cut.transformLine( line ),
                equalTo( "infoData,2019:03:13 09:14:27Z,2019:03:13 10:10:00+04:30,2019:03:13 13:44:27+04:30,2019:03:13 13:44:27"
                        + System.lineSeparator() ) );
    }

    @Test
    public void transformLineShouldUseCustomFormat()
        throws IOException
    {
        final String[] line = new String[] { "infoData", "2019:03:13 09:14:27Z", "2019:03:13 10:10:00+04:30" };

        config.setOutputLineFormat( "hugo %4$s egon \"%1$s\"" );

        assertThat( cut.transformLine( line ), equalTo( "hugo 2019:03:13 13:44:27+04:30 egon \"infoData\"" ) );
    }

    @Test
    public void createShiftedTimeWithWrongShiftFieldIdxShouldThrowException()
        throws IOException
    {
        thrown.expect( TimeshifterException.class );
        thrown.expectMessage( "11 fields are required but" );

        config.setFieldIdxToShift( 10 );

        final String[] values = new String[] { "infoData", "2019:03:13 09:14:27Z" };

        cut.createShiftedTime( values );
    }

    @Test
    public void createShiftedTimeWithWrongOffsetFieldIdxShouldThrowException()
        throws IOException
    {
        thrown.expect( TimeshifterException.class );
        thrown.expectMessage( "9 fields are required but" );

        config.setFieldIdxOfOffset( 8 );

        final String[] values = new String[] { "infoData", "2019:03:13 09:14:27Z" };

        cut.createShiftedTime( values );
    }

    @Test
    public void createShiftedTimeShouldUseFixZoneOffsetWithoutOffsetTime()
        throws IOException
    {
        config.setFixOffset( ZoneOffset.ofHoursMinutes( 2, 20 ) );

        final String[] values = new String[] { "infoData", "2019:03:13 09:14:27Z" };

        assertThat( cut.createShiftedTime( values ), equalTo( OffsetDateTime.parse( "2019:03:13 11:34:27+02:20",
                MutableTimeshifterConfig.DEFAULT_DATE_TIME_FORMATTER ) ) );
    }

    @Test
    public void createShiftedTimeShouldUseFixZoneOffsetWithOffsetTime()
        throws IOException
    {
        config.setFixOffset( ZoneOffset.ofHoursMinutes( 2, 20 ) );

        final String[] values = new String[] { "infoData", "2019:03:13 09:14:27Z", "2019:03:13 10:10:00+04:30" };

        assertThat( cut.createShiftedTime( values ), equalTo( OffsetDateTime.parse( "2019:03:13 11:34:27+02:20",
                MutableTimeshifterConfig.DEFAULT_DATE_TIME_FORMATTER ) ) );
    }

    @Test
    public void sanitizeFieldWithNullShouldReturnEmptyString()
    {
        assertThat( cut.sanitizeField( null ), equalTo( "" ) );
    }

    @Test
    public void sanitizeFieldWithEmptyStringShouldReturnEmptyString()
    {
        assertThat( cut.sanitizeField( "" ), equalTo( "" ) );
    }

    @Test
    public void sanitizeFieldWithTrailingBlanksShouldCutBlanks()
    {
        assertThat( cut.sanitizeField( "some text  " ), equalTo( "some text" ) );
    }

    @Test
    public void sanitizeFieldWithLeadingBlanksShouldCutBlanks()
    {
        assertThat( cut.sanitizeField( "  some text" ), equalTo( "some text" ) );
    }

    @Test
    public void formatFixPatternWithNullShouldReturnEmptyString()
    {
        assertThat( cut.formatFixPattern( null ), equalTo( "" ) );
    }

    @Test
    public void formatFixPatternWithEmptyStringShouldReturnEmptyString()
    {
        assertThat( cut.formatFixPattern( "" ), equalTo( "" ) );
    }

    @Test
    public void formatFixPatternShouldNotChangeInputWithoutFormattingInstructions()
    {
        final String testTest = "The quick brown fox jumps over the lazy dog";
        assertThat( cut.formatFixPattern( testTest ), equalTo( testTest ) );
    }

    @Test
    public void formatFixPatternShouldInsertLineBreakWithLineBreakInstruction()
    {
        assertThat( cut.formatFixPattern( "The quick brown fox %njumps over the lazy dog" ),
                equalTo( "The quick brown fox " + System.lineSeparator() + "jumps over the lazy dog" ) );
    }

    @Test
    public void writeShouldWriteLineToWriter()
    {
        final StringWriter writer = new StringWriter();

        cut.write( "The quick brown fox jumps over the lazy dog", writer );

        assertThat( writer.toString(), equalTo( "The quick brown fox jumps over the lazy dog" ) );
    }

    @Test
    public void writeWithIOExceptionShouldThrowTimeshifterException()
    {
        thrown.expect( TimeshifterException.class );

        final Writer writer = new Writer()
        {
            @Override
            public void write( final char[] cbuf, final int off, final int len )
                throws IOException
            {
                throw new IOException();
            }

            @Override
            public void flush()
                throws IOException
            {
            }

            @Override
            public void close()
                throws IOException
            {
            }
        };

        cut.write( "The quick brown fox jumps over the lazy dog", writer );
    }
}
