package util.exception;

public class NoRoomAllocationException extends Exception{

    public NoRoomAllocationException() {
    }

    public NoRoomAllocationException(String msg) {
        super(msg);
    }
}
