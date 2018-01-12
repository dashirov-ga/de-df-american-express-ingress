package ly.generalassemb.de.datafeeds.americanExpress.ingress.model;

public class NotRegisteredException extends Exception {
    public NotRegisteredException(String message) {
        super(message);
    }
    public NotRegisteredException(String message, Throwable cause) {
        super(message, cause);
    }
}