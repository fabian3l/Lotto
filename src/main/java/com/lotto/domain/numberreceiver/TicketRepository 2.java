package com.lotto.domain.numberreciver;

import java.time.LocalDateTime;
import java.util.Collection;

public interface TicketRepository {

    Collection<Ticket> findAllTicketsByDrawDate(LocalDateTime drawDate);
    Ticket findByHash(String hash);
    Ticket save(Ticket savedTicket);
}
