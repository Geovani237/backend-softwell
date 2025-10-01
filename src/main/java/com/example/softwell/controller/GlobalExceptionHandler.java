package com.example.softwell.controller;

import com.example.softwell.exception.CooldownException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice // Indica que esta classe lida com exceções globais
public class GlobalExceptionHandler {

    /**
     * Captura a CooldownException e força o retorno do status HTTP 403 (Forbidden).
     * O corpo da resposta será a mensagem da exceção, que o Kotlin irá exibir no pop-up.
     */
    @ExceptionHandler(CooldownException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN) // <--- ESSENCIAL: Isso força o código 403
    @ResponseBody // Retorna a mensagem no corpo da resposta
    public String handleCooldownException(CooldownException ex) {
        return ex.getMessage();
    }
}