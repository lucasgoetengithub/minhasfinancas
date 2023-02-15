package com.lgoeten.minhasfinancas.exceptions;

public class ErroAutenticacaoException extends RuntimeException {

    public ErroAutenticacaoException(String mensagem) {
        super(mensagem);
    }
}
