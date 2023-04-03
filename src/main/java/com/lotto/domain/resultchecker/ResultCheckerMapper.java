package com.lotto.domain.resultchecker;

import com.lotto.domain.numberreceiver.dto.TicketDto;
import com.lotto.domain.resultchecker.dto.ResultDto;

import java.util.List;
import java.util.stream.Collectors;

public class ResultCheckerMapper {

    static List<ResultDto> mapPlayerToResult(List<Player> players) {
        return players.stream()
                .map(player -> ResultDto.builder()
                        .numbers(player.numbers())
                        .drawDate(player.drawDate())
                        .hash(player.hash())
                        .hitNumbers(player.hitNumbers())
                        .isWinner(player.isWinner())
                        .build())
                .collect(Collectors.toList());
    }
    static List<Ticket> mapFromTicketDto(List<TicketDto> allTicketsByDate) {
        return allTicketsByDate.stream()
                .map(ticket -> Ticket.builder()
                        .numbers(ticket.numbers())
                        .hash(ticket.hash())
                        .drawDate(ticket.drawDate())
                        .build())
                .collect(Collectors.toList());
    }
}
