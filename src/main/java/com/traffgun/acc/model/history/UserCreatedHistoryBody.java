package com.traffgun.acc.model.history;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserCreatedHistoryBody implements HistoryBody{
    @Override
    public HistoryBodyType getType() {
        return HistoryBodyType.USER_CREATED;
    }

    private final String username;

    @JsonCreator
    public UserCreatedHistoryBody(@JsonProperty("username") String username) {
        this.username = username;
    }
}
