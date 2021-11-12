package util.exception;

public class RoomRateCannotBeFoundException extends Exception{

    public RoomRateCannotBeFoundException() {
    }

    public RoomRateCannotBeFoundException(String msg) {
        super(msg);
    }
}
