package com.lotto.domain.numberreciver.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record NumberReceiverResponseDto(
        TicketDto ticketDto,
        String message) {
}
