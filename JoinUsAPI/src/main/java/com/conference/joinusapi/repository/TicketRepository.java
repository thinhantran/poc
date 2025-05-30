package com.conference.joinusapi.repository;

import com.conference.joinusapi.model.Ticket;
import com.conference.joinusapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByUserAndIsOrderFalse(User user);

}