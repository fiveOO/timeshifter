package com.github.fiveoo.timeshifter.cli;

import java.time.ZoneId;

import com.beust.jcommander.IStringConverter;

public class ZoneIdConverter
    implements
        IStringConverter<ZoneId>
{
    @Override
    public ZoneId convert( final String value )
    {
        return ZoneId.of( value );
    }
}
