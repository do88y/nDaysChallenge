package challenge.nDaysChallenge.exception;

public class NotEnoughRoomException extends RuntimeException {

    public NotEnoughRoomException() {
        super();
    }

    public NotEnoughRoomException(String message) {
        super(message);
    }

    public NotEnoughRoomException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEnoughRoomException(Throwable cause) {
        super(cause);
    }

}
