package com.github.fiveoo.timeshifter;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;

import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import com.github.fiveoo.timeshifter.cli.MutableTimeshifterConfig;

public class TimeshifterTest
{
    private MutableTimeshifterConfig config;
    private Timeshifter              cut;

    @Before
    public void setUp()
        throws Exception
    {
        config = new MutableTimeshifterConfig();
        cut = new Timeshifter( config );
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
        assertThat( resultList, hasItem(
                "infoData,2019:03:13 09:14:27Z,2019:03:13 10:10:00+04:30,2019:03:13 13:44:27+04:30,2019:03:13 13:44:27" ) );
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

        assertThat( cut.transformLine( line ), equalTo(
                "infoData,2019:03:13 09:14:27Z,2019:03:13 10:10:00+04:30,2019:03:13 13:44:27+04:30,2019:03:13 13:44:27" ) );
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
}
