package com.conference.joinusapi.service;

import com.conference.joinusapi.model.Ticket;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.time.format.DateTimeFormatter;


import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

@Service
public class PdfService {

    public byte[] generatePdfWithTickets(List<Ticket> tickets) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);
            DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            for (Ticket ticket : tickets) {
                document.add(new Paragraph("Your ticket on JoinUs "));

                if (ticket.getEvent() != null) {
                    document.add(new Paragraph("Event: " + ticket.getEvent().getName()));
                    document.add(new Paragraph("Date: " + ticket.getEvent().getDateTime().format(DATE_TIME_FORMATTER)));
                } else {
                    document.add(new Paragraph("Event: N/A"));
                }

                if (ticket.getCommande() != null && !ticket.getCommande().isRegisteredUser()) {
                    document.add(new Paragraph("Buyer: " + ticket.getCommande().getFullName()));
                } else if (ticket.getUser() != null) {
                    document.add(new Paragraph("Name: " + ticket.getUser().getFullName()));
                } else {
                    document.add(new Paragraph("User: Guest"));
                }

                document.add(new Paragraph("Quantity: " + ticket.getQuantity()));
                document.add(new Paragraph(" "));

                String qrContent = buildQrContent(ticket);
                BufferedImage qrImage = generateQrCodeImage(qrContent);
                ByteArrayOutputStream imageOutput = new ByteArrayOutputStream();
                ImageIO.write(qrImage, "PNG", imageOutput);

                Image qr = new Image(ImageDataFactory.create(imageOutput.toByteArray()));
                document.add(qr);

                document.add(new Paragraph("\n\n"));
            }


            document.close();
            return baos.toByteArray();

        } catch (IOException | WriterException e) {
            throw new RuntimeException("Error while generating PDF", e);
        }
    }

    private BufferedImage generateQrCodeImage(String text) throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 150, 150);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    private String buildQrContent(Ticket ticket) {
        StringBuilder content = new StringBuilder();
        DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        content.append("Ticket for: ")
                .append(ticket.getEvent() != null ? ticket.getEvent().getName() : "Unknown Event")
                .append("\n");

        if (ticket.getEvent() != null) {
            content.append("Date: ")
                    .append(ticket.getEvent().getDateTime().format(DATE_TIME_FORMATTER))
                    .append("\n");
        }

        content.append("Ticket ID: ").append(ticket.getId()).append("\n");

        if (ticket.getCommande() != null && !ticket.getCommande().isRegisteredUser()) {
            content.append("Buyer: ").append(ticket.getCommande().getFullName());
        } else if (ticket.getUser() != null) {
            content.append("Buyer: ").append(ticket.getUser().getFullName());
        }

        return content.toString();
    }

}
