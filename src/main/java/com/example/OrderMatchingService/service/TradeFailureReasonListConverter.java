package com.example.OrderMatchingService.service;

import com.example.OrderMatchingService.domain.TradeFailureReason;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class TradeFailureReasonListConverter implements AttributeConverter<List<TradeFailureReason>, String> {

    private static final String DELIMITER = ",";

    @Override
    public String convertToDatabaseColumn(List<TradeFailureReason> attribute) {
        return attribute == null ? "" : attribute.stream()
                .map(Enum::name)
                .collect(Collectors.joining(DELIMITER));
    }

    @Override
    public List<TradeFailureReason> convertToEntityAttribute(String dbData) {
        return dbData == null || dbData.isEmpty() ?
                List.of() :
                Arrays.stream(dbData.split(DELIMITER))
                        .map(TradeFailureReason::valueOf)
                        .collect(Collectors.toList());
    }
}

