package com.lotto.domain.numberreciver;

import com.lotto.domain.numberreciver.dto.InputNumbersResultDto;
import lombok.AllArgsConstructor;


import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.lotto.domain.numberreciver.dto.InputNumbersResultDto.*;

//klient podaje 6 liczb
//liczby musza byc w zakresie 1-99
//liczby nie moga sie powtarzac
//klient dostaje informacje o dacie losowania
//klient dostaje informacje o swoim unikalnym identyfikatorze losowania

@AllArgsConstructor
public class NumberReciverFacade {
    private final NumberValidator validator;
    private final NumberReciverRepository repository;
    private final Clock clock;

    public InputNumbersResultDto inputNumbers(Set<Integer> numbersFromUser) {

        Boolean areAllNumbersInRange = validator.areAllNumbersInRange(numbersFromUser);
        if (areAllNumbersInRange) {
            String ticketId = UUID.randomUUID().toString();
            LocalDateTime drawDate = LocalDateTime.now(clock);
            repository.save(new Ticket(ticketId, drawDate, numbersFromUser));
            return builder()
                    .drawDate(drawDate)
                    .ticketId(ticketId)
                    .numbersFromUser(numbersFromUser)
                    .message("success")
                    .build();
        }
        return builder()
                .message("failed")
                .build();
    }
    public List<TicketDto> userNumbers(LocalDateTime date) {
        List<Ticket> allTicketsByDrawDate = repository.findAllTicketsByDrawDate(date);
        return allTicketsByDrawDate
                .stream()
                .map(TicketMapper::mapFromTicket)
                .toList();

    }

}
