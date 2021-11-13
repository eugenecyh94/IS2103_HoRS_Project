/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomEntity;
import java.util.List;
import javax.ejb.Remote;
import util.exception.RoomCannotBeDeletedException;
import util.exception.RoomCannotBeFoundException;

/**
 *
 * @author Eugene Chua
 */
@Remote
public interface RoomEntitySessionBeanRemote {

    public RoomEntity createNewRoom(RoomEntity room, Long roomTypeId);

    public RoomEntity retrieveRoomById(Long roomId) throws RoomCannotBeFoundException;

    public RoomEntity retrieveRoomByRoomNumber(String roomNumber) throws RoomCannotBeFoundException;

    public void updateRoomDetails(RoomEntity updatedRoom);

    public void deleteRoombyID(Long roomID) throws RoomCannotBeDeletedException, RoomCannotBeFoundException;

    public List<RoomEntity> retrieveAllRooms();

    public List<RoomEntity> retrieveAllRoomsByRoomType(Long roomTypeId) throws RoomCannotBeFoundException;

    public List<RoomEntity> retrieveAllAvailableRooms();
    
}
