package util.exception;

public class RoomTypeCannotBeDeletedException extends Exception {

    public RoomTypeCannotBeDeletedException() {
    }

    public RoomTypeCannotBeDeletedException(String msg) {
        super(msg);
    }
}
