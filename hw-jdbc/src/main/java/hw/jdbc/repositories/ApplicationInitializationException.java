package hw.jdbc.repositories;

public class ApplicationInitializationException extends RuntimeException {
    public ApplicationInitializationException(){
        super();
    }
    public ApplicationInitializationException(String message) {
        super(message);
    }
}
