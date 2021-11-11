package util.exception;

public class RoomAllocationNotUpgradedException extends Exception{

    public RoomAllocationNotUpgradedException() {
    }

    public RoomAllocationNotUpgradedException(String msg) {
        super(msg);
    }
}
