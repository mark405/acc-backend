package com.traffgun.acc.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TicketType {
    TECH_GOAL("Tech Goal"),
    ADVERTISER_REQUEST("Запити рекламодавцям"),
    OFFERS_REQUEST("Запити на оффери"),
    TASK("Task");

    private final String label;
}
