package util.exception;

public class RoomCannotBeDeletedException extends Exception {

    public RoomCannotBeDeletedException() {
    }

    public RoomCannotBeDeletedException(String msg) {
        super(msg);
    }
}
