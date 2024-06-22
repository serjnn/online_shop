package com.serjn.online.repositories;

import com.serjn.online.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client,Long> {
    Optional<Client> findByMail(String mail);
}
