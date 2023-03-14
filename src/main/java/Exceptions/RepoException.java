package Exceptions;

public class RepoException extends Exception{

    public RepoException(String message) {
        super(message);
    }

    @Override
    public void print() {
        System.out.println("UserRepository exception: "+message);
    }
}
