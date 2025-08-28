package com.senai.oficina_teste_swagger.domain.exception;


public class ValidacaoException extends RuntimeException {
    public ValidacaoException(String mensagem) {
        super(mensagem);
    }
}