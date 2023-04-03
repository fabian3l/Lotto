package com.lotto.domain.resultchecker;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
public class WinnersRetriever {

    private final static int NUMBERS_WHEN_PLAYER_WON = 3;

    List<Player> retriveWinners(List<Ticket> allTicketsByDate, Set<Integer> winningNumbers){
        return allTicketsByDate.stream()
                .map(ticket -> {
                    Set<Integer> hitNumbers = calculateHits(winningNumbers, ticket);
                    return buildPlayer(hitNumbers, ticket);
                })
                .toList();
    }

    private Set<Integer> calculateHits(Set<Integer> winningNumbers, Ticket ticket) {
        return ticket.numbers().stream()
                .filter(winningNumbers::contains)
                .collect(Collectors.toSet());
    }

    private Player buildPlayer(Set<Integer> hitNumbers, Ticket ticket) {
        Player.PlayerBuilder builder = Player.builder();
        if (isWinner(hitNumbers)){
            builder.isWinner(true);
        }
        return builder.hitNumbers(hitNumbers)
                .hash(ticket.hash())
                .drawDate(ticket.drawDate())
                .numbers(ticket.numbers())
                .build();
    }

    private boolean isWinner(Set<Integer> hitNumbers) {
        return hitNumbers.size() >= NUMBERS_WHEN_PLAYER_WON;
    }

}
