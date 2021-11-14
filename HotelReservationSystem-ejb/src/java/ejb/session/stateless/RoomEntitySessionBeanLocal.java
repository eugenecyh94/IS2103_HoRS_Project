package ejb.session.stateless;

import entity.RoomEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.NoRoomAllocationException;
import util.exception.RoomCannotBeDeletedException;
import util.exception.RoomCannotBeFoundException;

@Local
public interface RoomEntitySessionBeanLocal {

    public RoomEntity createNewRoom(RoomEntity room, Long roomTypeId);

    public RoomEntity retrieveRoomById(Long roomId) throws RoomCannotBeFoundException;

    public RoomEntity retrieveRoomByRoomNumber(String roomNumber) throws RoomCannotBeFoundException;

    public void updateRoomDetails(RoomEntity updatedRoom);

    public void deleteRoombyID(Long roomID) throws RoomCannotBeDeletedException, RoomCannotBeFoundException;

    public List<RoomEntity> retrieveAllRooms();

    public List<RoomEntity> retrieveAllRoomsByRoomType(Long roomTypeId) throws RoomCannotBeFoundException;

    public List<RoomEntity> retrieveAllAvailableRooms();

    public List<RoomEntity> retrieveRoomsByReservationId(Long reservationId) throws NoRoomAllocationException;

}
