package com.github.fiveoo.timeshifter;

import java.io.IOException;

public final class TimeshifterException
    extends
        RuntimeException
{
    private static final long serialVersionUID = 20190315L;

    private TimeshifterException( final String message )
    {
        super( message );
    }

    private TimeshifterException( final Throwable cause )
    {
        super( cause );
    }

    private TimeshifterException( final String message, final Throwable cause )
    {
        super( message, cause );
    }

    public static TimeshifterException errorWritingOutput( final IOException e )
    {
        return new TimeshifterException( "Could not write result", e );
    }

    public static TimeshifterException tooLessFields( final int currentLength, final int requiredLength )
    {
        return new TimeshifterException( "Could not read input. " + requiredLength + " fields are required but only "
                + currentLength + " are given." );
    }
}
