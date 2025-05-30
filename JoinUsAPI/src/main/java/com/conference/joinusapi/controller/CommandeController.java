package com.conference.joinusapi.controller;

import com.conference.joinusapi.dto.CommandeRequest;
import com.conference.joinusapi.dto.CommandeSummaryResponse;
import com.conference.joinusapi.model.Commande;
import com.conference.joinusapi.service.CommandeService;
import com.conference.joinusapi.service.PdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/commandes")
@RequiredArgsConstructor
public class CommandeController {

    private final CommandeService commandeService;

    private final PdfService pdfService;

    @PostMapping
    public ResponseEntity<byte[]> createCommande(@RequestBody CommandeRequest request) {
        Map<String, Object> result = commandeService.createCommande(request);

        byte[] pdf = (byte[]) result.get("pdf");
        Long id = (Long) result.get("id");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(
                ContentDisposition.inline()
                        .filename("tickets-" + id + ".pdf")
                        .build()
        );
        headers.add("X-Commande-Id", id.toString());

        return new ResponseEntity<>(pdf, headers, HttpStatus.CREATED);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadCommande(@PathVariable Long id) {
        Commande commande = commandeService.getCommandeById(id);
        byte[] pdfBytes = pdfService.generatePdfWithTickets(commande.getTickets());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment().filename("commande.pdf").build());

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CommandeSummaryResponse>> getCommandeSummariesByUserId(@PathVariable Long userId) {
        List<CommandeSummaryResponse> summaries = commandeService.getCommandeSummariesByUserId(userId);
        return ResponseEntity.ok(summaries);
    }

}
