package com.github.fiveoo.timeshifter.cli;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;

import static org.junit.Assert.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.github.robtimus.filesystems.memory.MemoryFileSystemProvider;

public class TimeshifterCliTest
{
    private Path           dataFolder;
    private Path           exampleValidPath;
    private TimeshifterCli cut;

    @Before
    public void setup()
        throws IOException
    {
        MemoryFileSystemProvider.clear();

        dataFolder = Paths.get( URI.create( "memory:/timeshifterData" ) );
        Files.createDirectories( dataFolder );

        exampleValidPath = dataFolder.resolve( "exampleInputValid.txt" );
        Files.copy( getClass().getResourceAsStream( "exampleInputValid.txt" ), exampleValidPath,
                StandardCopyOption.REPLACE_EXISTING );

        cut = new TimeshifterCli();
    }

    @Test
    public void runApplIsProcessingInputFileToOutputFile()
        throws Exception
    {
        final Path outPath = dataFolder.resolve( "out.txt" );
        final int parseResult = cut.parseParameters( null, "-i", "\"" + exampleValidPath.toUri().toString() + "\"",
                "-o", "\"" + outPath.toUri().toString() + "\"" );

        assertThat( parseResult, equalTo( 0 ) );

        cut.runAppl( null, null );

        final List<String> outLines = Files.readAllLines( outPath, StandardCharsets.UTF_8 );
        assertThat( outLines, hasSize( 2 ) );
        assertThat( outLines, hasItem(
                "NewImages/DSC00034.jpg,2019:03:09 17:58:00Z,2019:03:09 18:57:30+01:00,2019:03:09 18:58:00+01:00,2019:03:09 18:58:0" ) );
        assertThat( outLines, hasItem(
                "NewImages/DSC00035.jpg,2019:03:10 13:59:36Z,2019:03:10 14:59:06+01:00,2019:03:10 14:59:36+01:00,2019:03:10 14:59:36" ) );
    }

    @Test
    public void runApplIsProcessingInputStreamToOutputStream()
        throws Exception
    {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (final PrintStream baos = new PrintStream( out ))
        {
            final int parseResult = cut.parseParameters( null );
            assertThat( parseResult, equalTo( 0 ) );

            cut.runAppl( getClass().getResourceAsStream( "exampleInputValid.txt" ), baos );
        }

        final String output = new String( out.toByteArray() );

        assertThat( output,
                equalTo( "NewImages/DSC00034.jpg,2019:03:09 17:58:00Z,2019:03:09 18:57:30+01:00,2019:03:09 18:58:00+01:00,2019:03:09 18:58:0"
                        + System.lineSeparator()
                        + "NewImages/DSC00035.jpg,2019:03:10 13:59:36Z,2019:03:10 14:59:06+01:00,2019:03:10 14:59:36+01:00,2019:03:10 14:59:36"
                        + System.lineSeparator() ) );
    }

    @Test
    public void parseParametersIsShowingHelp()
        throws Exception
    {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (final PrintStream baos = new PrintStream( out ))
        {
            assertThat( cut.parseParameters( baos, "-h" ), equalTo( 1 ) );
        }

        final String output = new String( out.toByteArray() );
        assertThat( output, containsString( "Shows this help" ) );
        assertThat( output.length(), greaterThan( 1500 ) );
    }

    @Test
    public void parseParametersWithUnknownParamIsReportingErrorAndShowingHelp()
        throws Exception
    {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (final PrintStream baos = new PrintStream( out ))
        {
            assertThat( cut.parseParameters( baos, "-hugo" ), equalTo( -1 ) );
        }

        final String output = new String( out.toByteArray() );
        assertThat( output, containsString( "Was passed main parameter '-hugo' but no main parameter" ) );
        assertThat( output, containsString( "Shows this help" ) );
        assertThat( output.length(), greaterThan( 1500 ) );
    }
}
