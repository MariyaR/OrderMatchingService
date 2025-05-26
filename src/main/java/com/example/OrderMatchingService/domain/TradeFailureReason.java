package com.example.OrderMatchingService.domain;

import java.util.ArrayList;
import java.util.List;

public enum TradeFailureReason {

    BUYER_ACCOUNT_NOT_FOUND,
    SELLER_ACCOUNT_NOT_FOUND,
    BUYER_INSUFFICIENT_FUNDS,
    SELLER_INSUFFICIENT_SHARES,
    UNKNOWN_ERROR,
    EMPTY_FAILURE_REASON;

    public static List<TradeFailureReason> getEmptyFailureList() {
        List<TradeFailureReason> list = new ArrayList<>();
        list.add(EMPTY_FAILURE_REASON);
        return list;
    }
}
