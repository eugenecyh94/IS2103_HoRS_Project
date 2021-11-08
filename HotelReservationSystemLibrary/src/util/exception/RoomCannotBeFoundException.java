package util.exception;

public class RoomCannotBeFoundException extends Exception {

    public RoomCannotBeFoundException() {
    }

    public RoomCannotBeFoundException(String msg) {
        super(msg);
    }
}
