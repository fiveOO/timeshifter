package com.github.fiveoo.timeshifter.cli;

import static org.hamcrest.Matchers.equalTo;

import static org.junit.Assert.assertThat;

import java.time.DateTimeException;
import java.time.ZoneOffset;

import org.junit.Before;
import org.junit.Test;

public class ZoneOffsetConverterTest
{
    ZoneOffsetConverter cut;

    @Before
    public void setUp()
        throws Exception
    {
        cut = new ZoneOffsetConverter();
    }

    @Test(expected = NullPointerException.class)
    public void convertNullShouldThrowNPE()
    {
        cut.convert( null );
    }

    @Test(expected = DateTimeException.class)
    public void convertEmptyStringShouldThrowDatimeException()
    {
        assertThat( cut.convert( "" ), equalTo( null ) );
    }

    @Test
    public void convertOffsetStringShouldReturnZoneOffset()
    {
        assertThat( cut.convert( "+01:00" ), equalTo( ZoneOffset.ofHours( 1 ) ) );
    }
}
