package com.lotto.domain.numberreciver;

import com.lotto.domain.numberreciver.dto.InputNumbersResultDto;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static java.util.Set.of;
import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.*;

class NumberReciverFacadeTest {

    NumberReciverFacade numberReciverFacade = new NumberReciverFacade(
            new NumberValidator()
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