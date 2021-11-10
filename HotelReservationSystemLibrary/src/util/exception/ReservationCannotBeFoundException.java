package util.exception;

public class ReservationCannotBeFoundException extends Exception{

    public ReservationCannotBeFoundException() {
    }

    public ReservationCannotBeFoundException(String msg) {
        super(msg);
    }
}
