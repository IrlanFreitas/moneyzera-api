package com.otp.moneyzeraapi.exception;

public class ErroAutenticacao extends RuntimeException{
    public ErroAutenticacao(String message) {
        super(message);
    }
}
