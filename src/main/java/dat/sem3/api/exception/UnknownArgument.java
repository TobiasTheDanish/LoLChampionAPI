package dat.sem3.api.exception;

public class UnknownArgument extends Exception{
    public UnknownArgument() {
    }

    public UnknownArgument(String message) {
        super(message);
    }

    public UnknownArgument(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownArgument(Throwable cause) {
        super(cause);
    }

    public UnknownArgument(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
