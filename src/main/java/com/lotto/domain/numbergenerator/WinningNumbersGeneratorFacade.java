package com.lotto.domain.numbergenerator;

import com.lotto.domain.numbergenerator.dto.WinningNumbersDto;
import com.lotto.domain.numberreceiver.NumberReceiverFacade;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
public class WinningNumbersGeneratorFacade {

    private final RandomNumberGenerable winningNumberGenerator;
    private final WinningNumberValidator winningNumberValidator;
    private final WinningNumbersRepository winningNumbersRepository;
    private final NumberReceiverFacade numberReceiverFacade;

    public WinningNumbersDto generateWinningNumbers(){
        LocalDateTime nextDrawDate = numberReceiverFacade.retrieveNextDrawDate();
        Set<Integer> winningNumbers = winningNumberGenerator.generateSixRandomNumbers();
        winningNumberValidator.validate(winningNumbers);
        winningNumbersRepository.save(WinningNumbers.builder()
                .winningNumbers(winningNumbers)
                .date(nextDrawDate)
                .build());
        return WinningNumbersDto.builder()
                .winningNumbers(winningNumbers)
                .build();
    }
    public WinningNumbersDto retriveWinningNumberByDate(LocalDateTime date) {
        WinningNumbers numberByDate = winningNumbersRepository.findNumbersByDate(date)
                .orElseThrow(() -> new WinningNumbersNotFoundException("Not found"));
        return WinningNumbersDto.builder()
                .winningNumbers(numberByDate.winningNumbers())
                .date(numberByDate.date())
                .build();
    }
    public boolean areWinningNumbersGeneratedByDate() {
        LocalDateTime nextDrawDate = numberReceiverFacade.retrieveNextDrawDate();
        return winningNumbersRepository.existsByDate(nextDrawDate);
    }

}
