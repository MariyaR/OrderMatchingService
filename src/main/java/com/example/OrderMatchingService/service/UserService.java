package com.example.OrderMatchingService.service;

import com.example.OrderMatchingService.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

  private final AccountRepository accountRepository;

  /**
   * Find internal account ID (userId) by Keycloak username.
   *
   * @param keycloakUsername the username from Keycloak token
   * @return UUID of the internal account
   * @throws NoSuchElementException if no account is found
   */
  public UUID getUserIdByKeycloakUsername(String keycloakUsername) {
    return accountRepository.findByKeycloakUsername(keycloakUsername)
      .orElseThrow(() -> new NoSuchElementException(
        "Account not found for Keycloak username: " + keycloakUsername))
      .getId();
  }

  public String getKeyCloackUserNameById (UUID id) {
    return accountRepository.findKeycloakUsernameByAccountId(id).orElseThrow(() -> new RuntimeException("Username not found"));
  }
}
