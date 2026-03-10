package com.serjn.online.model.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDto(
    @Email @NotBlank String mail,
    @NotBlank @Size(min = 10, max = 40) String password
) {}
