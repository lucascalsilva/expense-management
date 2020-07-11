package com.mobnova.expense_mgt.config.mapper.converter;

import com.mobnova.expense_mgt.model.SegmentValuePair;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SegmentValuePairsToMapConverter implements Converter<List<SegmentValuePair>, String> {

    @Override
    public String convert(MappingContext<List<SegmentValuePair>, String> mappingContext) {
        return mappingContext.getSource().stream()
                .sorted(Comparator.comparing(o -> o.getSegmentType().getOrder()))
                .map(SegmentValuePair::getSegmentValue)
                .collect(Collectors.joining("-"));
    }
}
