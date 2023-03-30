package com.lotto.domain.numberreciver;

import java.time.LocalDateTime;
import java.util.List;


public interface NumberReciverRepository{
    Ticket save(Ticket ticket);

    List<Ticket> findAllTicketsByDrawDate(LocalDateTime date);
}
