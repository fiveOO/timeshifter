package com.github.fiveoo.timeshifter.cli;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import static org.junit.Assert.assertThat;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import org.junit.Before;
import org.junit.Test;

public class MutableTimeshifterConfigTest
{
    private static final OffsetDateTime TEST_NOW =
            OffsetDateTime.of( 2019, 03, 22, 13, 50, 42, 47, ZoneOffset.ofHours( 1 ) );

    private MutableTimeshifterConfig cut;

    @Before
    public void setUp()
        throws Exception
    {
        cut = new MutableTimeshifterConfig();
    }

    @Test
    public void getFieldIdxToShiftWOSetShouldReturnDefaultValue()
    {
        assertThat( cut.getFieldIdxToShift(), is( 1 ) );
    }

    @Test
    public void getFieldIdxOfOffsetWOSetShouldReturnDefaultValue()
    {
        assertThat( cut.getFieldIdxOfOffset(), is( 2 ) );
    }

    @Test
    public void getInputDateFormatterWOSetFormatShouldReturnDefaultFormatter()
    {
        final DateTimeFormatter formatter = cut.getInputDateFormatter();

        assertThat( formatter, notNullValue() );
        assertThat( formatter.format( TEST_NOW ), equalTo( "2019:03:22 13:50:42+01:00" ) );
    }

    @Test
    public void getInputDateFormatterWithSetFormatNullShouldReturnDefaultFormatter()
    {
        cut.setInputDateFormat( null );
        final DateTimeFormatter formatter = cut.getInputDateFormatter();

        assertThat( formatter, notNullValue() );
        assertThat( formatter.format( TEST_NOW ), equalTo( "2019:03:22 13:50:42+01:00" ) );
    }

    @Test
    public void getInputDateFormatterWithSetFormatShouldReturnCustomFormatter()
    {
        cut.setInputDateFormat( "dd-MM-yyyy XXX ss-mm-HH" );
        final DateTimeFormatter formatter = cut.getInputDateFormatter();

        assertThat( formatter, notNullValue() );
        assertThat( formatter.format( TEST_NOW ), equalTo( "22-03-2019 +01:00 42-50-13" ) );
    }

    @Test
    public void getOutputDateFormatterWOSetFormatShouldReturnDefaultFormatter()
    {
        final DateTimeFormatter formatter = cut.getOutputDateFormatter();

        assertThat( formatter, notNullValue() );
        assertThat( formatter.format( TEST_NOW ), equalTo( "2019:03:22 13:50:42+01:00" ) );
    }

    @Test
    public void getOutputDateFormatterWithSetFormatNullShouldReturnDefaultFormatter()
    {
        cut.setOutputDateFormat( null );
        final DateTimeFormatter formatter = cut.getOutputDateFormatter();

        assertThat( formatter, notNullValue() );
        assertThat( formatter.format( TEST_NOW ), equalTo( "2019:03:22 13:50:42+01:00" ) );
    }

    @Test
    public void getOutputDateFormatterWithSetFormatShouldReturnCustomFormatter()
    {
        cut.setOutputDateFormat( "dd-MM-yyyy XXX ss-mm-HH" );
        final DateTimeFormatter formatter = cut.getOutputDateFormatter();

        assertThat( formatter, notNullValue() );
        assertThat( formatter.format( TEST_NOW ), equalTo( "22-03-2019 +01:00 42-50-13" ) );
    }

    @Test
    public void getOutputDateFormatterLocalWOSetFormatShouldReturnDefaultFormatterWOTimezone()
    {
        final DateTimeFormatter formatter = cut.getOutputDateFormatterLocal();

        assertThat( formatter, notNullValue() );
        assertThat( formatter.format( TEST_NOW ), equalTo( "2019:03:22 13:50:42" ) );
    }

    @Test
    public void getOutputDateFormatterLocalWithSetFormatNullShouldReturnDefaultFormatterWOTimezone()
    {
        cut.setOutputDateFormat( null );
        final DateTimeFormatter formatter = cut.getOutputDateFormatterLocal();

        assertThat( formatter, notNullValue() );
        assertThat( formatter.format( TEST_NOW ), equalTo( "2019:03:22 13:50:42" ) );
    }

    @Test
    public void getOutputDateFormatterLocalWithSetFormatShouldReturnCustomFormatterWOTimezone()
    {
        cut.setOutputDateFormat( "dd-MM-yyyy XXX ss-mm-HH" );
        final DateTimeFormatter formatter = cut.getOutputDateFormatterLocal();

        assertThat( formatter, notNullValue() );
        assertThat( formatter.format( TEST_NOW ), equalTo( "22-03-2019 42-50-13" ) );
    }

    @Test
    public void getHeaderLineWOSetShouldReturnNull()
    {
        assertThat( cut.getOutputHeaderLine(), nullValue() );
    }

    @Test
    public void getHeaderLineWithNullShouldReturnNull()
    {
        cut.setOutputHeaderLine( null );

        assertThat( cut.getOutputHeaderLine(), nullValue() );
    }

    @Test
    public void getHeaderLineWithElementsShouldConcatElements()
    {
        cut.setOutputHeaderLine( new String[] { "h1", "h2", "h3" } );

        assertThat( cut.getOutputHeaderLine(), equalTo( "h1h2h3" ) );
    }

    @Test
    public void getFooterLineWOSetShouldReturnNull()
    {
        assertThat( cut.getOutputFooterLine(), nullValue() );
    }

    @Test
    public void getFooterLineWithNullShouldReturnNull()
    {
        cut.setOutputFooterLine( null );

        assertThat( cut.getOutputFooterLine(), nullValue() );
    }

    @Test
    public void getFooterLineShouldConcatElements()
    {
        cut.setOutputFooterLine( new String[] { "f1", "f2", "f3" } );

        assertThat( cut.getOutputFooterLine(), equalTo( "f1f2f3" ) );
    }
}
