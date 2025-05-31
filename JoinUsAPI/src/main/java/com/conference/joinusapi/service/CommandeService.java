package com.conference.joinusapi.service;

import com.conference.joinusapi.dto.CommandeRequest;
import com.conference.joinusapi.dto.CommandeSummaryResponse;
import com.conference.joinusapi.dto.TicketResponse;
import com.conference.joinusapi.model.Commande;
import com.conference.joinusapi.model.Ticket;
import com.conference.joinusapi.repository.CommandeRepository;
import com.conference.joinusapi.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommandeService {

    private final CommandeRepository commandeRepository;
    private final TicketRepository ticketRepository;
    private final PdfService pdfService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    //private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


    public Map<String, Object> createCommande(CommandeRequest request) {
        Commande commande = new Commande();
        commande.setRegisteredUser(request.isRegisteredUser());

        if (request.isRegisteredUser()) {
            commande.setUserId(request.getUserId());
        } else {
            commande.setFullName(request.getFullName());
            commande.setEmail(request.getEmail());
            commande.setPhone(request.getPhone());
        }

        List<Ticket> tickets = ticketRepository.findAllById(request.getTicketIds());
        for (Ticket ticket : tickets) {
            if (ticket.isOrder()) {
                throw new IllegalStateException("Ticket with id " + ticket.getId() + " has already been sold");
            }
            ticket.setOrder(true);
            ticket.setCommande(commande);
        }
        commande.setTickets(tickets);
        commande.setCreatedAt(LocalDateTime.now());

        commande = commandeRepository.save(commande);
        ticketRepository.saveAll(tickets);
        byte[] pdf = pdfService.generatePdfWithTickets(tickets);

        request.setId(commande.getId());

        Map<String, Object> response = new HashMap<>();
        response.put("id", commande.getId());
        response.put("pdf", pdf);

        return response;
    }

    public Commande getCommandeById(Long id) {
        return commandeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Commande not found with ID: " + id));
    }

    public List<Commande> getCommandesByUserId(Long userId) {
        return commandeRepository.findByUserId(userId);
    }

    public List<CommandeSummaryResponse> getCommandeSummariesByUserId(Long userId) {
        List<Commande> commandes = commandeRepository.findByUserId(userId);

        return commandes.stream().map(commande -> {
            List<TicketResponse> tickets = commande.getTickets().stream().map(ticket -> {
                var event = ticket.getEvent();

                int quantity = ticket.getQuantity();

                return TicketResponse.builder()
                        .id(ticket.getId())
                        .eventTitle(event.getName())
                        .date(event.getDateTime().format(DATE_FORMATTER))
                        .time(event.getDateTime().format(TIME_FORMATTER))
                        .quantity(quantity)
                        .unitPrice(event.getPrice().doubleValue())
                        .total(quantity * event.getPrice().doubleValue())
                        .image(event.getImageUrl())
                        .build();
            }).toList();

            return CommandeSummaryResponse.builder()
                    .createdAt(String.valueOf(commande.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()))
                    .commandeId(commande.getId())
                    .tickets(tickets)
                    .build();
        }).toList();
    }

}
