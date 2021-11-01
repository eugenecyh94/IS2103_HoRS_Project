/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomTypeEntity;
import java.util.List;
import javax.ejb.Remote;
import util.enumeration.BedSizeEnum;
import util.enumeration.RoomAmenitiesEnum;
import util.exception.RoomCannotBeDeletedException;

/**
 *
 * @author Eugene Chua
 */
@Remote
public interface RoomTypeEntitySessionBeanRemote {

    public RoomTypeEntity createNewRoomType(String name, String description, int capacity, int totalRooms, String roomSize, BedSizeEnum bedsize, List<RoomAmenitiesEnum> roomAmenities);

    public RoomTypeEntity retrieveRoomType(String name);

    public void updateRoomTypeDetails(String name, String newName, String description, int capacity, int totalRooms, String roomSize, BedSizeEnum bedSize, List<RoomAmenitiesEnum> roomAmenities);

    public void deleteRoomType(String name) throws RoomCannotBeDeletedException;

    public List<RoomTypeEntity> retrieveAllRoomTypes();
    
}
