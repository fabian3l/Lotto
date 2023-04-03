package com.lotto.domain.resultchecker;

import com.lotto.domain.numbergenerator.WinningNumbersGeneratorFacade;
import com.lotto.domain.numbergenerator.dto.WinningNumbersDto;
import com.lotto.domain.numberreceiver.NumberReceiverFacade;
import com.lotto.domain.numberreceiver.dto.TicketDto;
import com.lotto.domain.resultchecker.dto.PlayersDto;
import com.lotto.domain.resultchecker.dto.ResultDto;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
public class ResultCheckerFacade {

    WinningNumbersGeneratorFacade winningNumbersGeneratorFacade;
    NumberReceiverFacade numberReceiverFacade;
    PlayerRepository playerRepository;
    WinnersRetriever winnerGenerator;


    public PlayersDto generateWinners() {
      List<TicketDto> allTicketsByDate = numberReceiverFacade.retrieveAllTicketsByNextDrawDate();
      List<Ticket> tickets = ResultCheckerMapper.mapFromTicketDto(allTicketsByDate);
      WinningNumbersDto winningNumbersDto = winningNumbersGeneratorFacade.generateWinningNumbers();
      Set<Integer> winningNumbers = winningNumbersDto.getWinningNumbers();
      if (winningNumbers == null || winningNumbers.isEmpty()) {
          return PlayersDto.builder()
                  .message("Winners failed to retrieve")
                  .build();
      }
      List<Player> players = winnerGenerator.retriveWinners(tickets, winningNumbers);
      playerRepository.saveAll(players);
      return PlayersDto.builder()
              .results(ResultCheckerMapper.mapPlayerToResult(players))
              .message("Winners succeeded to retrieve")
              .build();
    }
    public ResultDto findByHash(String hash) {
        Player player = playerRepository.findById(hash).orElseThrow(() -> new RuntimeException("Not found"));
        return ResultDto.builder()
                .hash(player.hash())
                .numbers(player.numbers())
                .hitNumbers(player.hitNumbers())
                .drawDate(player.drawDate())
                .isWinner(player.isWinner())
                .build();
    }

}
