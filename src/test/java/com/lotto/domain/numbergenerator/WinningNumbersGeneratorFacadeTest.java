package com.lotto.domain.numbergenerator;

import com.lotto.domain.numbergenerator.dto.WinningNumbersDto;
import com.lotto.domain.numberreceiver.NumberReceiverFacade;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


class WinningNumbersGeneratorFacadeTest {

    private final WinningNumbersRepository winningNumbersRepository = new WinningNumbersRepositoryTestImpl();
    NumberReceiverFacade numberReceiverFacade = mock(NumberReceiverFacade.class);

    @Test
    public void it_should_return_set_of_required_size() {
        //given
        RandomNumberGenerable generator = new RandomGenerator();
        when(numberReceiverFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.now());
        WinningNumbersGeneratorFacade numbersGenerator = new NumberGeneratorConfiguration()
                .createForTest(generator, winningNumbersRepository, numberReceiverFacade);
        //when
        WinningNumbersDto generatedNumbers = numbersGenerator.generateWinningNumbers();
        //then
        assertThat(generatedNumbers.getWinningNumbers().size()).isEqualTo(6);
    }

    @Test
    public void it_should_return_set_od_required_size_within_required_range() {
        //given
        RandomNumberGenerable genertor = new RandomGenerator();
        when(numberReceiverFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.now());
        WinningNumbersGeneratorFacade numbersGenerator = new NumberGeneratorConfiguration()
                .createForTest(genertor, winningNumbersRepository, numberReceiverFacade);
        //when
        WinningNumbersDto generatedNumbers = numbersGenerator.generateWinningNumbers();
        //then
        int lowerBand = 1;
        int upperBand = 99;
        Set<Integer> winningNumbers = generatedNumbers.getWinningNumbers();
        boolean numbersInRange = winningNumbers.stream()
                        .allMatch(number -> number > lowerBand || number < upperBand);
        assertThat(numbersInRange).isTrue();
    }
    @Test
    public void it_should_throw_an_exception_when_number_not_in_range() {
        //given
        Set<Integer> numbersOutOfRange = Set.of(1,2,3,4,5,100);
        RandomNumberGenerable generator = new WinningNumberGeneratorTestImpl(numbersOutOfRange);
        when(numberReceiverFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.now());
        WinningNumbersGeneratorFacade numberGenerator = new NumberGeneratorConfiguration().createForTest(generator, winningNumbersRepository, numberReceiverFacade);
        //when
        //then
        assertThrows(IllegalStateException.class, numberGenerator::generateWinningNumbers, "Numbers out of range");
    }
    @Test
    public void it_should_return_collection_of_unique_values() {
        //given
        RandomNumberGenerable generator = new RandomGenerator();
        when(numberReceiverFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.now());
        WinningNumbersGeneratorFacade numbersGenerator = new NumberGeneratorConfiguration()
                .createForTest(generator, winningNumbersRepository, numberReceiverFacade);
        //when
        WinningNumbersDto generatedNumbers = numbersGenerator.generateWinningNumbers();
        //then
        int generatedNumberSize = new HashSet<>(generatedNumbers.getWinningNumbers()).size();
        assertThat(generatedNumberSize).isEqualTo(6);
    }
    @Test
    public void it_should_return_winning_numbers_by_given_date() {
        //given
        LocalDateTime drawDate = LocalDateTime.of(2022,12,17, 12,0,0);
        Set<Integer> generatedWinningNumbers = Set.of(1,2,3,4,5,6);
        String id = UUID.randomUUID().toString();
        WinningNumbers winningNumbers = WinningNumbers.builder()
                .id(id)
                .winningNumbers(generatedWinningNumbers)
                .date(drawDate)
                .build();
        winningNumbersRepository.save(winningNumbers);
        RandomNumberGenerable generator = new WinningNumberGeneratorTestImpl();
        when(numberReceiverFacade.retrieveNextDrawDate()).thenReturn(drawDate);
        WinningNumbersGeneratorFacade numbersGenerator = new NumberGeneratorConfiguration().createForTest(generator, winningNumbersRepository, numberReceiverFacade);
        //when
        WinningNumbersDto winningNumbersDto = numbersGenerator.retriveWinningNumberByDate(drawDate);
        //then
        WinningNumbersDto expectedWinningNumbersDto = WinningNumbersDto.builder()
                .winningNumbers(generatedWinningNumbers)
                .date(drawDate)
                .build();
        assertThat(expectedWinningNumbersDto).isEqualTo(winningNumbersDto);
    }
    @Test
    public void it_should_throw_an_exception_when_fail_to_retrive_numbers_by_given_date() {
        //given
        LocalDateTime drawDate = LocalDateTime.of(2022,12,17, 12,0,0);
        RandomNumberGenerable generator = new WinningNumberGeneratorTestImpl();
        when(numberReceiverFacade.retrieveNextDrawDate()).thenReturn(drawDate);
        WinningNumbersGeneratorFacade winningNumbersGeneratorFacade = new NumberGeneratorConfiguration().createForTest(generator, winningNumbersRepository, numberReceiverFacade);
        //when
        //then
        assertThrows(WinningNumbersNotFoundException.class, () -> winningNumbersGeneratorFacade.retriveWinningNumberByDate(drawDate), "not found");
    }
    @Test
    public void it_should_return_true_if_numbers_are_generated_by_given_date() {
        //given
        LocalDateTime drawDate = LocalDateTime.of(2022,12,17, 12,0,0);
        Set<Integer> generatedWinningNumbers = Set.of(1,2,3,4,5,6);
        String id = UUID.randomUUID().toString();
        WinningNumbers winningNumbers = WinningNumbers.builder()
                .id(id)
                .winningNumbers(generatedWinningNumbers)
                .date(drawDate)
                .build();
        winningNumbersRepository.save(winningNumbers);
        RandomNumberGenerable generator = new WinningNumberGeneratorTestImpl();
        when(numberReceiverFacade.retrieveNextDrawDate()).thenReturn(drawDate);
        WinningNumbersGeneratorFacade numbersGenerator = new NumberGeneratorConfiguration().createForTest(generator, winningNumbersRepository, numberReceiverFacade);
        //when
        boolean areWinningNumbersGeneratedByDate = numbersGenerator.areWinningNumbersGeneratedByDate();
        assertThat(areWinningNumbersGeneratedByDate);
    }
}