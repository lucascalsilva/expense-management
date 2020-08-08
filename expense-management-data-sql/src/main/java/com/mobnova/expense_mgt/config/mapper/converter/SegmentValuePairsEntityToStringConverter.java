package com.mobnova.expense_mgt.config.mapper.converter;

import com.mobnova.expense_mgt.model.SegmentValuePair;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class SegmentValuePairsEntityToStringConverter implements Converter<List<SegmentValuePair>, String> {

    @Override
    public String convert(MappingContext<List<SegmentValuePair>, String> mappingContext) {
        Long highestOrder = mappingContext.getSource().stream()
                .map(segmentValuePair -> segmentValuePair.getSegmentType().getOrder())
                .max(Comparator.naturalOrder()).orElse(0L);

        return LongStream.rangeClosed(1, highestOrder).mapToObj(order -> {
            return mappingContext.getSource().stream().filter(segmentValuePair -> segmentValuePair.getSegmentType().getOrder() == order)
                    .findFirst().map(SegmentValuePair::getSegmentValue).orElse("0");
        }).collect(Collectors.joining("-"));
    }
}
