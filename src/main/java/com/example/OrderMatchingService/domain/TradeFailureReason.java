package com.example.OrderMatchingService.domain;

public enum TradeFailureReason {

    BUYER_ACCOUNT_NOT_FOUND,
    SELLER_ACCOUNT_NOT_FOUND,
    BUYER_INSUFFICIENT_FUNDS,
    SELLER_INSUFFICIENT_SHARES,
    UNKNOWN_ERROR,
    EMPTY_FAILURE_REASON
}
