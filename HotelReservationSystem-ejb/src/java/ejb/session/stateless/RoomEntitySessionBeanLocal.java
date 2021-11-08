package ejb.session.stateless;

import entity.RoomEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.RoomCannotBeFoundException;

@Local
public interface RoomEntitySessionBeanLocal {

    public RoomEntity createNewRoom(RoomEntity room, Long roomTypeID);

    public RoomEntity retrieveRoomById(Long roomId) throws RoomCannotBeFoundException;

    public RoomEntity retrieveRoomByRoomNumber(String roomNumber) throws RoomCannotBeFoundException;

    public void updateRoomDetails(RoomEntity updatedRoom);

    public void deleteRoombyID(Long roomID) throws RoomCannotBeFoundException;

    public List<RoomEntity> retrieveAllRooms();

    public List<RoomEntity> retrieveAllRoomsByRoomType(Long roomTypeId) throws RoomCannotBeFoundException;
    
}
