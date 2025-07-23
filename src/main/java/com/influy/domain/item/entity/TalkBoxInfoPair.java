package com.influy.domain.item.entity;

import java.util.Map;

public record TalkBoxInfoPair(Map<Long, Integer> waitingCntMap, Map<Long, Integer> completedCntMap) {
}
