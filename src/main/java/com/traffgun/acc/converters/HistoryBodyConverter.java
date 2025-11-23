package com.traffgun.acc.converters;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.traffgun.acc.model.history.*;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;

@Converter()
@RequiredArgsConstructor
public class HistoryBodyConverter implements AttributeConverter<HistoryBody, String> {

    private final ObjectMapper mapper;

    @Override
    public String convertToDatabaseColumn(HistoryBody attribute) {
        try {
            return mapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public HistoryBody convertToEntityAttribute(String dbData) {
        try {
            JsonNode json = mapper.readTree(dbData);
            String type = json.get("type").asText();
            HistoryBodyType bodyType = HistoryBodyType.valueOf(type);

            Class<?> cl = switch (bodyType) {
                case USER_CREATED -> UserCreatedHistoryBody.class;
                case USER_PASSWORD_CHANGED -> UserPasswordChangedHistoryBody.class;
                case OPERATION_CREATED -> OperationCreatedHistoryBody.class;
                case OPERATION_DELETED -> OperationDeletedHistoryBody.class;
                case OPERATION_UPDATED -> OperationUpdatedHistoryBody.class;
            };

            return (HistoryBody) mapper.readValue(dbData, cl);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
