package com.conference.joinusapi.repository;

import com.conference.joinusapi.model.Commande;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommandeRepository extends JpaRepository<Commande, Long> {
    List<Commande> findByUserId(Long userId);
}
