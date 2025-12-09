package com.example.OrderMatchingService.repository;

import com.example.OrderMatchingService.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {

  Optional<Account> findByKeycloakUsername(String username);

  @Query("SELECT a.keycloakUsername FROM Account a WHERE a.id = :id")
  Optional<String> findKeycloakUsernameByAccountId(@Param("id") UUID id);
}
