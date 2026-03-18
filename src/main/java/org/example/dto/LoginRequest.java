package org.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @NotBlank(message = "L'email est obligatoire")
        @Email(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
                message = "Le format de l'email est invalide")
        String email,

        @NotBlank(message = "Le mot de passe est obligatoire")
        String password
) {}
