package com.mobnova.expense_mgt.config.mapper.converter;

import com.mobnova.expense_mgt.model.SegmentType;
import com.mobnova.expense_mgt.model.SegmentValuePair;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class SegmentValuePairsStringToEntityConverter implements Converter<String, List<SegmentValuePair>> {

    @Override
    public List<SegmentValuePair> convert(MappingContext<String, List<SegmentValuePair>> mappingContext) {
        String[] splitSegments = mappingContext.getSource().split("-");

        List<SegmentValuePair> segmentValuePairs = new ArrayList<SegmentValuePair>();

        IntStream.range(0, splitSegments.length).forEach(value -> {
                    String segmentValue = splitSegments[value];
                    if (!segmentValue.equalsIgnoreCase("0")) {
                        SegmentValuePair segmentValuePair = SegmentValuePair.builder().segmentValue(splitSegments[value])
                                .segmentType(SegmentType.builder().order(value + 1L).build()).build();
                        segmentValuePairs.add(segmentValuePair);
                    }
                }
        );

        return segmentValuePairs;
    }
}
