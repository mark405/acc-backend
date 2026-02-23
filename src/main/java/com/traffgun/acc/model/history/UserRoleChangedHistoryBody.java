package com.traffgun.acc.model.history;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRoleChangedHistoryBody implements HistoryBody{
    @Override
    public HistoryBodyType getType() {
        return HistoryBodyType.USER_ROLE_CHANGED;
    }

    private final String username;

    @JsonCreator
    public UserRoleChangedHistoryBody(@JsonProperty("username") String username) {
        this.username = username;
    }
}
