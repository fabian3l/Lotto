package com.lotto.domain.numberreciver;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

//encja do bazy
@Builder
record Ticket(String hash, LocalDateTime drawDate, Set<Integer> numbers){

}
