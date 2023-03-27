package com.lotto.domain.numberreciver;

import com.lotto.domain.numberreciver.dto.InputNumbersResultDto;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Set;

import static com.lotto.domain.numberreciver.dto.InputNumbersResultDto.*;

//klient podaje 6 liczb
//liczby musza byc w zakresie 1-99
//liczby nie moga sie powtarzac
//klient dostaje informacje o dacie losowania
//klient dostaje informacje o swoim unikalnym identyfikatorze losowania

@AllArgsConstructor
public class NumberReciverFacade {
    private final NumberValidator validator;
    public InputNumbersResultDto inputNumbers(Set<Integer> numbersFromUser) {

        Boolean areAllNumbersInRange = validator.areAllNumbersInRange(numbersFromUser);
        if (areAllNumbersInRange) {
            return builder()
                    .message("success")
                    .build();
        }
        return builder()
                .message("failed")
                .build();
    }

}
