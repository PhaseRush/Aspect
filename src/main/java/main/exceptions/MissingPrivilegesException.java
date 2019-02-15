package main.exceptions;

public class MissingPrivilegesException extends RuntimeException{
    public MissingPrivilegesException(String msg) {
        super(msg);
    }
}
