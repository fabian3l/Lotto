package com.lotto.domain.numberreciver;

import com.lotto.domain.numberreciver.dto.NumberReceiverResponseDto;
import com.lotto.domain.numberreciver.dto.TicketDto;
import lombok.AllArgsConstructor;


import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.lotto.domain.numberreciver.dto.NumberReceiverResponseDto.*;

//klient podaje 6 liczb
//liczby musza byc w zakresie 1-99
//liczby nie moga sie powtarzac
//klient dostaje informacje o dacie losowania
//klient dostaje informacje o swoim unikalnym identyfikatorze losowania

@AllArgsConstructor
public class NumberReciverFacade {
    private final NumberValidator numberValidator;
    private final DrawDateGenerator drawDateGenerator;
    private final HashGenerable hashGenerator;
    private final TicketRepository ticketRepository;

    public NumberReceiverResponseDto inputNumbers(Set<Integer> numbersFromUser) {

        List<ValidationResult> validationResultList = numberValidator.validate(numbersFromUser);
        if (!validationResultList.isEmpty()) {
            String resultMessage = numberValidator.createResultMessage();
            return new NumberReceiverResponseDto(null, resultMessage);
        }
        LocalDateTime drawDate = drawDateGenerator.getNextDrawDate();
        String hash = hashGenerator.getHash();
        TicketDto generatedTicket = TicketDto.builder()
                .hash(hash)
                .numbers(numbersFromUser)
                .drawDate(drawDate)
                .build();
        Ticket savedTicket = Ticket.builder()
                .hash(hash)
                .numbers(generatedTicket.numbers())
                .drawDate(generatedTicket.drawDate())
                .build();
        ticketRepository.save(savedTicket);
        return new NumberReceiverResponseDto(generatedTicket, ValidationResult.INPUT_SUCCESS.info);

    }
    public List<TicketDto> retriveAllTicketsByNextDrawDate() {
        LocalDateTime nextDrawDate = drawDateGenerator.getNextDrawDate();
        return retriveAllTicketsByNextDrawDate(nextDrawDate);
    }
    public List<TicketDto> retrieveAllTicketsByNextDrawDate(LocalDateTime date) {
        LocalDateTime nextDrawDate = drawDateGenerator.getNextDrawDate();
        if (date.isAfter(nextDrawDate)) {
            return Collections.emptyList();
        }
        return ticketRepository.findAllTicketsByDrawDate(date)
                .stream()
                .filter(ticket -> ticket.drawDate().isEqual(date))
                .map(ticket -> TicketDto.builder()
                        .hash(ticket.hash())
                        .numbers(ticket.numbers())
                        .drawDate(ticket.drawDate())
                        .build())
                .collect(Collectors.toList());
    }
    public LocalDateTime retrieveNextDrawDate() {
        return drawDateGenerator.getNextDrawDate();
    }
    public TicketDto findByHash(String hash) {
        Ticket ticket = ticketRepository.findByHash(hash);
        return TicketDto.builder()
                .hash(ticket.hash())
                .numbers(ticket.numbers())
                .drawDate(ticket.drawDate())
                .build();
    }

}
