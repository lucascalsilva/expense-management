package com.mobnova.expense_mgt.config.mapper.converter;

import com.mobnova.expense_mgt.model.SegmentValuePair;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SegmentValuePairsToMapConverter implements Converter<List<SegmentValuePair>, Map<String, String>> {

    @Override
    public Map<String, String> convert(MappingContext<List<SegmentValuePair>, Map<String, String>> mappingContext) {
        return mappingContext.getSource().stream().collect(Collectors
                .toMap(k -> k.getSegmentType().getCode(), v -> v.getSegmentValue()));
    }
}
