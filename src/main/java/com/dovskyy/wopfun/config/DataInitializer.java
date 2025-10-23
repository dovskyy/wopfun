package com.dovskyy.wopfun.config;

import com.dovskyy.wopfun.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;

    @Override
    public void run(String... args) {
        // Sprawdź czy istnieje już jakikolwiek użytkownik
        if (userService.getAllUsers().isEmpty()) {
            log.info("Baza danych jest pusta. Tworzenie domyślnego użytkownika admina...");

            try {
                userService.createUser("admin", "admin@wopfun.local", "admin123");
                log.info("✓ Utworzono domyślnego użytkownika:");
                log.info("  Username: admin");
                log.info("  Password: admin123");
                log.info("  WAŻNE: Zmień hasło po pierwszym logowaniu!");
            } catch (Exception e) {
                log.error("✗ Błąd podczas tworzenia domyślnego użytkownika: {}", e.getMessage());
            }
        } else {
            log.info("Użytkownicy już istnieją w bazie danych. Pomijam inicjalizację.");
        }
    }
}
