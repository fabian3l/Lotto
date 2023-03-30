package com.lotto.domain.numberreciver;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
record TicketDto(LocalDateTime drawDate, String ticketId, Set<Integer> numbersFromUser) {
}
