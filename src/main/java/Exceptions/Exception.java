package Exceptions;

public abstract class Exception extends Throwable{

    protected String message;

    public Exception(String message) {
        this.message = message;
    }

    public abstract void print();
    public String getMessage(){
        return message;
    }
}
