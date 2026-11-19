package br.com.reusebairro.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ErroResponse(int status, String erro, String mensagem, List<String> detalhes, LocalDateTime timestamp) {
}
