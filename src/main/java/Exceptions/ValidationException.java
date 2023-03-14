package Exceptions;

public class ValidationException extends Exception {


    public ValidationException(String message) {
        super(message);
    }

    @Override
    public void print() {
        System.out.println("Validation Exception: " + message);
    }
}
