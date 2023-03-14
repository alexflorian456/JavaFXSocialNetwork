package Exceptions;

public class ServiceException extends Exception{
    public ServiceException(String message) {
        super(message);
    }

    @Override
    public void print() {
        System.out.println("Service Exception: "+message);
    }
}
