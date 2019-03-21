package com.github.fiveoo.timeshifter;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import com.github.fiveoo.timeshifter.cli.MutableTimeshifterConfig;

/**
 * Configuration of a {@link Timeshifter}.
 *
 * For details on the values the methods should return have a look at the
 * corresponding configuration parameters located in
 * {@link MutableTimeshifterConfig}.
 */
public interface TimeshifterConfig
{
    String getInputFileName();

    int getInputLinesToSkip();

    int getFieldIdxToShift();

    int getFieldIdxOfOffset();

    ZoneOffset getFixOffset();

    String getOutputFileName();

    String getOutputLineFormat();

    String getOutputHeaderLine();

    String getOutputFooterLine();

    DateTimeFormatter getInputDateFormatter();

    DateTimeFormatter getOutputDateFormatter();

    DateTimeFormatter getOutputDateFormatterLocal();
}
