package util.exception;

public class NoRoomTypeAvailableException extends Exception {

    public NoRoomTypeAvailableException() {
    }

    public NoRoomTypeAvailableException(String msg) {
        super(msg);
    }
}
