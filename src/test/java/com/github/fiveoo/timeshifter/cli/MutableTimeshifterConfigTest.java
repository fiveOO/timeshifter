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
    public void getInDateShiftIdxWOSetShouldReturnDefaultValue()
    {
        assertThat( cut.getInDateShiftIdx(), is( 1 ) );
    }

    @Test
    public void getInDateOffsetIdxWOSetShouldReturnDefaultValue()
    {
        assertThat( cut.getInDateOffsetIdx(), is( 2 ) );
    }

    @Test
    public void getInDateShiftFormatterWOSetFormatShouldReturnDefaultFormatter()
    {
        final DateTimeFormatter formatter = cut.getInDateShiftFormatter();

        assertThat( formatter, notNullValue() );
        assertThat( formatter.format( TEST_NOW ), equalTo( "2019:03:22 13:50:42+01:00" ) );
    }

    @Test
    public void getInDateShiftFormatterWithSetFormatNullShouldReturnDefaultFormatter()
    {
        cut.setInDateShiftFormat( null );
        final DateTimeFormatter formatter = cut.getInDateShiftFormatter();

        assertThat( formatter, notNullValue() );
        assertThat( formatter.format( TEST_NOW ), equalTo( "2019:03:22 13:50:42+01:00" ) );
    }

    @Test
    public void getInDateShiftFormatterWithSetFormatShouldReturnCustomFormatter()
    {
        cut.setInDateShiftFormat( "dd-MM-yyyy XXX ss-mm-HH" );
        final DateTimeFormatter formatter = cut.getInDateShiftFormatter();

        assertThat( formatter, notNullValue() );
        assertThat( formatter.format( TEST_NOW ), equalTo( "22-03-2019 +01:00 42-50-13" ) );
    }

    @Test
    public void getInDateOffsetFormatterWOSetFormatShouldReturnDefaultFormatter()
    {
        final DateTimeFormatter formatter = cut.getInDateOffsetFormatter();

        assertThat( formatter, notNullValue() );
        assertThat( formatter.format( TEST_NOW ), equalTo( "2019:03:22 13:50:42+01:00" ) );
    }

    @Test
    public void getInDateOffsetFormatterWithSetFormatNullShouldReturnDefaultFormatter()
    {
        cut.setInDateOffsetFormat( null );
        final DateTimeFormatter formatter = cut.getInDateOffsetFormatter();

        assertThat( formatter, notNullValue() );
        assertThat( formatter.format( TEST_NOW ), equalTo( "2019:03:22 13:50:42+01:00" ) );
    }

    @Test
    public void getInDateOffsetFormatterWithSetFormatShouldReturnCustomFormatter()
    {
        cut.setInDateOffsetFormat( "dd-MM-yyyy XXX ss-mm-HH" );
        final DateTimeFormatter formatter = cut.getInDateOffsetFormatter();

        assertThat( formatter, notNullValue() );
        assertThat( formatter.format( TEST_NOW ), equalTo( "22-03-2019 +01:00 42-50-13" ) );
    }

    @Test
    public void getOutputDateFormatterWOSetFormatShouldReturnDefaultFormatter()
    {
        final DateTimeFormatter formatter = cut.getOutDateShiftedFormatter();

        assertThat( formatter, notNullValue() );
        assertThat( formatter.format( TEST_NOW ), equalTo( "2019:03:22 13:50:42+01:00" ) );
    }

    @Test
    public void getOutDateShiftedFormatterWithSetFormatNullShouldReturnDefaultFormatter()
    {
        cut.setOutDateShiftedFormat( null );
        final DateTimeFormatter formatter = cut.getOutDateShiftedFormatter();

        assertThat( formatter, notNullValue() );
        assertThat( formatter.format( TEST_NOW ), equalTo( "2019:03:22 13:50:42+01:00" ) );
    }

    @Test
    public void getOutDateShiftedFormatterWithSetFormatShouldReturnCustomFormatter()
    {
        cut.setOutDateShiftedFormat( "dd-MM-yyyy XXX ss-mm-HH" );
        final DateTimeFormatter formatter = cut.getOutDateShiftedFormatter();

        assertThat( formatter, notNullValue() );
        assertThat( formatter.format( TEST_NOW ), equalTo( "22-03-2019 +01:00 42-50-13" ) );
    }

    @Test
    public void getOutputDateFormatterLocalWOSetFormatShouldReturnDefaultFormatterWOTimezone()
    {
        final DateTimeFormatter formatter = cut.getOutDateShiftedFormatterLocal();

        assertThat( formatter, notNullValue() );
        assertThat( formatter.format( TEST_NOW ), equalTo( "2019:03:22 13:50:42" ) );
    }

    @Test
    public void getOutDateShiftedFormatterLocalWithSetFormatNullShouldReturnDefaultFormatterWOTimezone()
    {
        cut.setOutDateShiftedFormat( null );
        final DateTimeFormatter formatter = cut.getOutDateShiftedFormatterLocal();

        assertThat( formatter, notNullValue() );
        assertThat( formatter.format( TEST_NOW ), equalTo( "2019:03:22 13:50:42" ) );
    }

    @Test
    public void getOutDateShiftedFormatterLocalWithSetFormatShouldReturnCustomFormatterWOTimezone()
    {
        cut.setOutDateShiftedFormat( "dd-MM-yyyy XXX ss-mm-HH" );
        final DateTimeFormatter formatter = cut.getOutDateShiftedFormatterLocal();

        assertThat( formatter, notNullValue() );
        assertThat( formatter.format( TEST_NOW ), equalTo( "22-03-2019  42-50-13" ) );
    }

    @Test
    public void getOutHeaderFormatWOSetShouldReturnNull()
    {
        assertThat( cut.getOutHeaderFormat(), nullValue() );
    }

    @Test
    public void getOutHeaderFormatWithNullShouldReturnNull()
    {
        cut.setOutHeaderFormat( null );

        assertThat( cut.getOutHeaderFormat(), nullValue() );
    }

    @Test
    public void getOutHeaderFormatWithElementsShouldConcatElements()
    {
        cut.setOutHeaderFormat( new String[] { "h1", "h2", "h3" } );

        assertThat( cut.getOutHeaderFormat(), equalTo( "h1h2h3" ) );
    }

    @Test
    public void getOutFooterFormatWOSetShouldReturnNull()
    {
        assertThat( cut.getOutFooterFormat(), nullValue() );
    }

    @Test
    public void getOutFooterFormatWithNullShouldReturnNull()
    {
        cut.setOutFooterFormat( null );

        assertThat( cut.getOutFooterFormat(), nullValue() );
    }

    @Test
    public void getOutFooterFormatShouldConcatElements()
    {
        cut.setOutFooterFormat( new String[] { "f1", "f2", "f3" } );

        assertThat( cut.getOutFooterFormat(), equalTo( "f1f2f3" ) );
    }
}
