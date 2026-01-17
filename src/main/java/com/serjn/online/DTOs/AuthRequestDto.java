package com.serjn.online.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthRequestDto(
    @Email @NotBlank String mail,
    @NotBlank @Size(min = 10, max = 40) String password
) {}
