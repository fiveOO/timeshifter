package com.github.fiveoo.timeshifter;

import java.time.ZoneId;
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

    DateTimeFormatter getInDateShiftFormatter();

    ZoneId getInDateShiftZone();

    int getInDateShiftIdx();

    DateTimeFormatter getInDateOffsetFormatter();

    int getInDateOffsetIdx();

    int getInLinesSkip();

    String getOutputFileName();

    ZoneOffset getOutDateShiftedOffset();

    DateTimeFormatter getOutDateShiftedFormatter();

    DateTimeFormatter getOutDateShiftedFormatterLocal();

    String getOutputLineFormat();

    String getOutHeaderFormat();

    String getOutFooterFormat();
}
