package main.exceptions;

public class IllegalCommandStateException extends RuntimeException{
    public IllegalCommandStateException(String msg) {
        super(msg);
    }
}