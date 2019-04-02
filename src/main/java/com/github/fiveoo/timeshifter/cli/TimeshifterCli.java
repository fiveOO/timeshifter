package com.github.fiveoo.timeshifter.cli;

import static com.github.fiveoo.timeshifter.cli.Constants.JAVADOC_BASE_URL;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
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

    public static void main( final String... args )
    {
        final TimeshifterCli timeshifterCli = new TimeshifterCli();
        final int parseResult = timeshifterCli.parseParameters( System.err, args );

        if( parseResult != 0 )
        {
            // exit with error code (< 0) or help (=0)
            System.exit( Math.min( parseResult, 0 ) );
        }

        try
        {
            timeshifterCli.runAppl( System.in, System.out );
        }
        catch( final Exception e )
        {
            e.printStackTrace( System.err );
            System.exit( -5 );
        }
    }

    void runAppl( final InputStream stdIn, final PrintStream stdOut )
        throws IOException
    {
        final Timeshifter shifter = new Timeshifter( config );

        Reader reader;
        if( config.getInputFileName() == null )
        {
            reader = new InputStreamReader( stdIn, StandardCharsets.UTF_8 );
        }
        else
        {
            reader = Files.newBufferedReader( createPath( config.getInputFileName() ), StandardCharsets.UTF_8 );
        }
        Writer writer;
        if( config.getOutputFileName() == null )
        {
            writer = new OutputStreamWriter( stdOut, StandardCharsets.UTF_8 );
        }
        else
        {
            writer = Files.newBufferedWriter( createPath( config.getOutputFileName() ), StandardCharsets.UTF_8 );
        }

        try (Reader r = reader; Writer w = writer;)
        {
            shifter.shiftCsv( r, w );
        }
    }

    protected Path createPath( final String fileName )
    {
        // check if the file name may be an URI
        return fileName.indexOf( ':' ) > 1 ? Paths.get( URI.create( fileName ) ) : Paths.get( fileName );
    }

    /**
     * Parses the given parameters.
     *
     * @param args command line parameters
     *
     * @return &lt;0 in case of an error; 0 if correct; &gt;0 if help was called
     */
    int parseParameters( final PrintStream out, final String... args )
    {
        final JCommander parser = new JCommander( this );
        parser.setColumnSize( 80 );
        parser.setProgramName( "java -jar timeshifter-<version>.jar" );
        try
        {
            parser.parse( args );

            if( help )
            {
                out.println( usage( parser, null ) );

                return 1;
            }
        }
        catch( final ParameterException e )
        {
            out.println( usage( parser, new StringBuilder().append( e.getLocalizedMessage() )
                    .append( System.lineSeparator() ).append( System.lineSeparator() ) ) );

            return -1;
        }

        return 0;
    }

    StringBuilder usage( final JCommander parser, final StringBuilder usage )
    {
        StringBuilder result = usage;
        if( result == null )
        {
            result = new StringBuilder();
        }
        result.append( "Calculates dates shifted by a timezone offset" );
        result.append( System.lineSeparator() ).append( System.lineSeparator() );
        parser.usage( result );
        result.append( System.lineSeparator() ).append(
                "For help on date/time formats see " + JAVADOC_BASE_URL
                        + "java/time/format/DateTimeFormatter.html" );
        result.append( System.lineSeparator() );
        result.append( System.lineSeparator() ).append( "Error codes:" );
        result.append( System.lineSeparator() ).append( "  < 0: some error occurred; see error message" );
        result.append( System.lineSeparator() ).append( "  = 0: everything's fine" );

        return result;
    }
}
