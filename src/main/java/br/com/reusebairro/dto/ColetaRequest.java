package br.com.reusebairro.dto;

import jakarta.validation.constraints.NotNull;

public record ColetaRequest(@NotNull Long coletorId) {
}
