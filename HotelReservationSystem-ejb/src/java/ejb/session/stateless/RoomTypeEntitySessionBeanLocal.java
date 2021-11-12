/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomTypeEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.RoomTypeCannotBeDeletedException;
import util.exception.RoomTypeCannotBeFoundException;

/**
 *
 * @author Eugene Chua
 */
@Local
public interface RoomTypeEntitySessionBeanLocal {
    
    public RoomTypeEntity createNewRoomType(RoomTypeEntity newRoomType);

    public RoomTypeEntity retrieveRoomTypeById(Long roomTypeId) throws RoomTypeCannotBeFoundException;

    public RoomTypeEntity retrieveRoomTypeByName(String name) throws RoomTypeCannotBeFoundException;

    public void updateRoomTypeDetails(RoomTypeEntity updatedRoomType);

    public void deleteRoomTypebyID(Long roomTypeID) throws RoomTypeCannotBeDeletedException;

    public List<RoomTypeEntity> retrieveAllRoomTypes();
    
}
