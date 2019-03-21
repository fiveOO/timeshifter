package com.github.fiveoo.timeshifter.cli;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.ParametersDelegate;
import com.github.fiveoo.timeshifter.Timeshifter;
import com.github.fiveoo.timeshifter.TimeshifterConfig;

public class TimeshifterCli
{
    @Parameter(names = { "-h", "--help" }, description = "Shows this help", help = true)
    private boolean help;

    @ParametersDelegate
    private final TimeshifterConfig config = new MutableTimeshifterConfig();

    public static void main( final String[] args )
    {
        final TimeshifterCli timeshifterCli = new TimeshifterCli();
        final int parseResult = timeshifterCli.parseParameters( args );

        if( parseResult != 0 )
        {
            // exit with error code (< 0) or help (=0)
            System.exit( Math.min( parseResult, 0 ) );
        }

        try
        {
            timeshifterCli.runAppl();
        }
        catch( final Exception e )
        {
            e.printStackTrace( System.err );
            System.exit( -5 );
        }
    }

    private void runAppl()
        throws IOException
    {
        final Timeshifter shifter = new Timeshifter( config );

        Reader reader;
        if( config.getInputFileName() == null )
        {
            reader = new InputStreamReader( System.in, StandardCharsets.UTF_8 );
        }
        else
        {
            reader = Files.newBufferedReader( Paths.get( config.getInputFileName() ), StandardCharsets.UTF_8 );
        }
        Writer writer;
        if( config.getOutputFileName() == null )
        {
            writer = new OutputStreamWriter( System.out, StandardCharsets.UTF_8 );
        }
        else
        {
            writer = Files.newBufferedWriter( Paths.get( config.getOutputFileName() ), StandardCharsets.UTF_8 );
        }

        try (Reader r = reader; Writer w = writer;)
        {
            shifter.shift( r, w );
        }
    }

    /**
     * Parses the given parameters.
     *
     * @param args command line parameters
     *
     * @return &lt;0 in case of an error; 0 if correct; &gt;0 if help was called
     */
    private int parseParameters( final String[] args )
    {
        final JCommander parser = new JCommander( this );
        parser.setColumnSize( 80 );
        parser.setProgramName( "java -jar timeshifter-<version>.jar" );
        try
        {
            parser.parse( args );

            if( help )
            {
                System.err.println( usage( parser, null ) );

                return 1;
            }
        }
        catch( final ParameterException e )
        {
            System.err.println( usage( parser, new StringBuilder().append( e.getLocalizedMessage() )
                    .append( System.lineSeparator() ).append( System.lineSeparator() ) ) );

            return -1;
        }

        return 0;
    }

    private StringBuilder usage( final JCommander parser, final StringBuilder usage )
    {
        StringBuilder result = usage;
        if( result == null )
        {
            result = new StringBuilder();
        }
        result.append( "Calculates dates shifted by a timezone offset" );
        result.append( System.lineSeparator() ).append( System.lineSeparator() );
        parser.usage( result );
        result.append( System.lineSeparator() ).append( "Error codes:" );
        result.append( System.lineSeparator() ).append( "  < 0: some error occurred; see error message" );
        result.append( System.lineSeparator() ).append( "  = 0: everything's fine" );

        return result;
    }
}
