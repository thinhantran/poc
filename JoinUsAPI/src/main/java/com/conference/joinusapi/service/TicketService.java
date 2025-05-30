package com.conference.joinusapi.service;

import com.conference.joinusapi.dto.GuestTicketRequest;
import com.conference.joinusapi.dto.TicketRequest;
import com.conference.joinusapi.dto.TicketResponse;
import com.conference.joinusapi.model.Event;
import com.conference.joinusapi.model.Ticket;
import com.conference.joinusapi.model.User;
import com.conference.joinusapi.repository.EventRepository;
import com.conference.joinusapi.repository.TicketRepository;
import com.conference.joinusapi.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public TicketResponse createTicket(TicketRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new RuntimeException("Event not found"));

        int quantity = request.getQuantity() +1 ;
        double unitPrice = event.getPrice().doubleValue();
        double total = quantity * unitPrice;

        Ticket ticket = Ticket.builder()
                .user(user)
                .event(event)
                .quantity(quantity)
                .build();

        Ticket savedTicket = ticketRepository.save(ticket);

        return TicketResponse.builder()
                .id(savedTicket.getId())
                .eventTitle(event.getName())
                .date(event.getDateTime().toLocalDate().toString())
                .time(event.getDateTime().toLocalTime().toString())
                .quantity(quantity)
                .unitPrice(unitPrice)
                .total(total)
                .image(event.getImageUrl())
                .build();
    }


    public List<TicketResponse> getTicketsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ticketRepository.findByUserAndIsOrderFalse(user)
                .stream()
                .map(ticket -> {
                    Event event = ticket.getEvent();
                    int quantity = ticket.getQuantity();
                    double unitPrice = event.getPrice().doubleValue();
                    double total = quantity * unitPrice;

                    return TicketResponse.builder()
                            .id(ticket.getId())
                            .eventTitle(event.getName())
                            .date(event.getDateTime().toLocalDate().toString())
                            .time(event.getDateTime().toLocalTime().toString())
                            .quantity(quantity)
                            .unitPrice(unitPrice)
                            .total(total)
                            .image(event.getImageUrl())
                            .build();
                })
                .collect(Collectors.toList());
    }


    public void updateTicketQuantity(TicketRequest request) {
        Ticket ticket = ticketRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        ticket.setQuantity(request.getQuantity());
        ticketRepository.save(ticket);
    }

    @Transactional
    public void deleteTicket(Long ticketId) {
        ticketRepository.deleteById(ticketId);
    }

    public List<Long> createGuestTickets(GuestTicketRequest request) {
        List<Long> ticketIds = new ArrayList<>();

        for (GuestTicketRequest.TicketItem item : request.getTickets()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            LocalDateTime startTime = LocalDateTime.parse(item.getDate() + " " + item.getTime(), formatter);
            LocalDateTime endTime = startTime.plusMinutes(1).minusNanos(1);
            Event event = eventRepository.findByNameAndDateTimeBetween(item.getEventTitle(), startTime, endTime)
                    .orElseThrow(() -> new IllegalArgumentException("Event not found with name " + item.getEventTitle() + " at " + startTime));

            Ticket ticket = new Ticket();
            ticket.setEvent(event);
            ticket.setQuantity(item.getQuantity());
            ticket.setUser(null);

            Ticket saved = ticketRepository.save(ticket);
            ticketIds.add(saved.getId());
        }

        return ticketIds;
    }


}
