package com.conference.joinusapi.controller;

import com.conference.joinusapi.dto.GuestTicketRequest;
import com.conference.joinusapi.dto.TicketRequest;
import com.conference.joinusapi.dto.TicketResponse;
import com.conference.joinusapi.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<TicketResponse> createTicket(@RequestBody TicketRequest request) {
        return ResponseEntity.ok(ticketService.createTicket(request));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TicketResponse>> getTicketsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(ticketService.getTicketsByUser(userId));
    }

    @PutMapping
    public ResponseEntity<String> updateTicketQuantity(@RequestBody TicketRequest request) {
        ticketService.updateTicketQuantity(request);
        return ResponseEntity.ok("Quantity updated");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteTicket(
            @RequestParam Long ticketId) {
        ticketService.deleteTicket(ticketId);
        return ResponseEntity.ok("Ticket deleted");
    }

    @PostMapping("/guest")
    public ResponseEntity<List<Long>> createTicketsForGuest(@RequestBody GuestTicketRequest request) {
        List<Long> ticketIds = ticketService.createGuestTickets(request);
        return ResponseEntity.ok(ticketIds);
    }

}
