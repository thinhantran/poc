package com.conference.joinusapi.repository;

import com.conference.joinusapi.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByNameAndDateTimeBetween(String name, LocalDateTime start, LocalDateTime end);
    List<Event> findByNameContainingIgnoreCase(String name);


}