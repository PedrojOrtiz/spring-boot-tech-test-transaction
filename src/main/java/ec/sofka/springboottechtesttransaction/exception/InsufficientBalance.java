package ec.sofka.springboottechtesttransaction.exception;

public class InsufficientBalance extends RuntimeException{

    public InsufficientBalance(String message){
        super(message);
    }

}
