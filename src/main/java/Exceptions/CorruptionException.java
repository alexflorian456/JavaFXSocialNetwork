package Exceptions;

public class CorruptionException extends Exception{
    public CorruptionException(String message) {
        super(message);
    }

    @Override
    public void print() {
        System.out.println("Corrupt data: "+message);
    }
}
