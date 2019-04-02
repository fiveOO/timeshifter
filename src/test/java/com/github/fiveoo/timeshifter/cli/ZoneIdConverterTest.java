package com.github.fiveoo.timeshifter.cli;

import static org.hamcrest.Matchers.equalTo;

import static org.junit.Assert.assertThat;

import java.time.DateTimeException;
import java.time.ZoneId;

import org.junit.Before;
import org.junit.Test;

public class ZoneIdConverterTest
{
    ZoneIdConverter cut;

    @Before
    public void setUp()
        throws Exception
    {
        cut = new ZoneIdConverter();
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
    public void convertIdStringShouldReturnZoneOffset()
    {
        assertThat( cut.convert( "Europe/Berlin" ), equalTo( ZoneId.of( "Europe/Berlin" ) ) );
    }
}
