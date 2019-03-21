package com.github.fiveoo.timeshifter.cli;

import java.time.ZoneOffset;

import com.beust.jcommander.IStringConverter;

public class ZoneOffsetConverter
    implements
        IStringConverter<ZoneOffset>
{
    @Override
    public ZoneOffset convert( final String value )
    {
        return ZoneOffset.of( value );
    }
}
