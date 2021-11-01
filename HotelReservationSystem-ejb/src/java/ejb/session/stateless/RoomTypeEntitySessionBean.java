/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomTypeEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.BedSizeEnum;
import util.enumeration.RoomAmenitiesEnum;
import util.exception.RoomCannotBeDeletedException;

/**
 *
 * @author Eugene Chua
 */
@Stateless
public class RoomTypeEntitySessionBean implements RoomTypeEntitySessionBeanRemote, RoomTypeEntitySessionBeanLocal {
    
    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;
    
    public RoomTypeEntitySessionBean() {
    }

    @Override
    public RoomTypeEntity createNewRoomType(String name, String description, int capacity, int totalRooms, String roomSize, BedSizeEnum bedsize, List<RoomAmenitiesEnum> roomAmenities) {

        RoomTypeEntity newRoomType = new RoomTypeEntity(name, description, capacity, totalRooms, roomSize, bedsize, roomAmenities);

        em.persist(newRoomType);
        em.flush();

        return newRoomType;

    }

    @Override
    public RoomTypeEntity retrieveRoomType(String name) {

        Query query = em.createQuery("SELECT rt from RoomTypeEntity rt WHERE rt.name = " + "'" + name + "'");
        RoomTypeEntity roomType = (RoomTypeEntity) query.getSingleResult();

        return roomType;

    }

    @Override
    public void updateRoomTypeDetails(String name, String newName, String description, int capacity, int totalRooms, String roomSize, BedSizeEnum bedSize, List<RoomAmenitiesEnum> roomAmenities) {

        Query query = em.createQuery("SELECT rt from RoomTypeEntity rt WHERE rt.name = " + "'" + name + "'");
        RoomTypeEntity roomType = (RoomTypeEntity) query.getSingleResult();

        //assumption is on the menu options will be printed for user to edit whatever attribute they want
        //any attribute that is not edited will be returned as original attribute result.
        roomType.setName(newName);
        roomType.setDescription(description);
        roomType.setCapacity(capacity);
        roomType.setTotalRooms(totalRooms);
        roomType.setRoomSize(roomSize);
        roomType.setBedSize(bedSize);
        roomType.setRoomAmenities(roomAmenities);
    }

    @Override
    public void deleteRoomType(String name) throws RoomCannotBeDeletedException {
        Query query = em.createQuery("SELECT rt FROM RoomTypeEntity rt WHERE rt.name = " + "'" + name + "'");
        RoomTypeEntity roomType = (RoomTypeEntity) query.getSingleResult();

        //Assumption is if room type is being used by room that means room rate is also used
        //if room type is not used, room rate will be have room type as well therefore no need to loop and disassociate room type from room rate
        //to check again depending on use case
        if (roomType.getRooms().isEmpty()) {
            em.remove(roomType);
        } else {
            roomType.setRoomEnabled(false);
            throw new RoomCannotBeDeletedException("Room type is being used by other rooms, cannot be deleted!\nRoom type is disabled.");
        }
    }

    @Override
    public List<RoomTypeEntity> retrieveAllRoomTypes() {

        Query query = em.createQuery("SELECT rt FROM RoomTypeEntity rt");
        List<RoomTypeEntity> roomTypes = query.getResultList();

        //assumption is to display just room type name, capacity, size, total rooms
        for (RoomTypeEntity rt : roomTypes) {
            rt.getName();
            rt.getCapacity();
            rt.getRoomSize();
            rt.getTotalRooms();
        }

        return roomTypes;
    }
   
}
