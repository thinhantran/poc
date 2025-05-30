package com.conference.joinusapi.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean registeredUser;
    private Long userId;

    private String fullName;
    private String email;
    private String phone;

    @OneToMany
    private List<Ticket> tickets;

    private LocalDateTime createdAt;

}
