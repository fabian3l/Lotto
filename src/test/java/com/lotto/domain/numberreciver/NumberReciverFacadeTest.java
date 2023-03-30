package com.lotto.domain.numberreciver;

import com.lotto.domain.AdjustableClock;
import com.lotto.domain.numberreciver.dto.InputNumbersResultDto;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


class NumberReciverFacadeTest {

    AdjustableClock clock = new AdjustableClock(LocalDateTime.of(2023, 03,15,20,0,0).toInstant(ZoneOffset.UTC), ZoneId.systemDefault());

    NumberReciverFacade numberReciverFacade = new NumberReciverFacade(
            new NumberValidator(),
            new NumberReciverRepositoryTestImpl(),
            clock
    );

    @Test
    public void should_return_success_when_user_gave_six_numbers() {
        //given
        Set<Integer> numbersFromUser = Set.of(1,2,3,4,5,6);
        //when
        InputNumbersResultDto result = numberReciverFacade.inputNumbers(numbersFromUser);

        //then
        assertThat(result.message()).isEqualTo("success");
    }
    @Test
    public void should_return_save_to_database_when_user_gave_six_numbers() {
        //given
        Set<Integer> numbersFromUser = Set.of(1,2,3,4,5,6);
        InputNumbersResultDto result = numberReciverFacade.inputNumbers(numbersFromUser);
        LocalDateTime drawDate = LocalDateTime.of(2023, 03,15,21,0,0);
        //when
        List<TicketDto> ticketDtos = numberReciverFacade.userNumbers(drawDate);

        //then
        assertThat(ticketDtos).contains(
                TicketDto.builder()
                        .ticketId(result.ticketId())
                        .drawDate(drawDate)
                        .numbersFromUser(result.numbersFromUser())
                        .build()
        );
    }
    @Test
    public void should_return_failed_when_user_gave_less_than_six_numbers(){
        //given
        Set<Integer> numbersFromUser = Set.of(1,2,3,4,5);
        //when
        InputNumbersResultDto result = numberReciverFacade.inputNumbers(numbersFromUser);
        //then
        assertThat(result.message()).isEqualTo("failed");
    }
    @Test
    public void should_return_failed_when_user_gave_more_than_six_numbers(){
        //given
        Set<Integer> numbersFromUser = Set.of(1,2,3,4,5,6, 7);
        //when
        InputNumbersResultDto result = numberReciverFacade.inputNumbers(numbersFromUser);
        //then
        assertThat(result.message()).isEqualTo("failed");
    }
    @Test
    public void should_return_failed_when_user_gave_at_least_one_number_out_of_range(){
        //given
        Set<Integer> numbersFromUser = Set.of(1,2000,3,4,5,6);
        //when
        InputNumbersResultDto result = numberReciverFacade.inputNumbers(numbersFromUser);
        //then
        assertThat(result.message()).isEqualTo("failed");

    }

}