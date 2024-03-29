package ejb.session.stateless;

import entity.ReservationEntity;
import entity.RoomEntity;
import entity.RoomTypeEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.NoRoomAllocationException;
import util.exception.RoomCannotBeDeletedException;
import util.exception.RoomCannotBeFoundException;

@Stateless
public class RoomEntitySessionBean implements RoomEntitySessionBeanRemote, RoomEntitySessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    public RoomEntitySessionBean() {
    }

    @Override //commmented methods are in case we want to only associate the room ty[e with rooms in container
    public RoomEntity createNewRoom(RoomEntity room, Long roomTypeId) {

        RoomEntity newRoom = room;
        RoomTypeEntity roomType = em.find(RoomTypeEntity.class, roomTypeId);

        newRoom.setRoomType(roomType);
        roomType.getRooms().add(newRoom);

        em.persist(newRoom);
        em.flush();

        return newRoom;

    }

    @Override
    public RoomEntity retrieveRoomById(Long roomId) throws RoomCannotBeFoundException {

        RoomEntity room = em.find(RoomEntity.class, roomId);

        if (room == null) {
            throw new RoomCannotBeFoundException("Room does not exists for the entered ID!");
        }

        return room;
    }

    @Override
    public RoomEntity retrieveRoomByRoomNumber(String roomNumber) throws RoomCannotBeFoundException {

        Query query = em.createQuery("SELECT r from RoomEntity r WHERE r.roomNumber = :inRoomNumber");
        query.setParameter("inRoomNumber", roomNumber);
        try {
            RoomEntity room = (RoomEntity) query.getSingleResult();
            room.getRoomType();
            return room;
        } catch (NoResultException ex) {
            throw new RoomCannotBeFoundException("Room does not exists for the entered Room Number!");
        }
    }
    
    @Override
    public List<RoomEntity> retrieveRoomsByReservationId(Long reservationId) throws NoRoomAllocationException {        
        
        ReservationEntity rs = em.find(ReservationEntity.class, reservationId);
        
        Query query = em.createQuery("SELECT r FROM RoomEntity r WHERE r.currentReservation = :inReservation").setParameter("inReservation", rs);
        
        if(query.getResultList().isEmpty()) { 
            throw new NoRoomAllocationException("No rooms are allocation for this reservation ID " + reservationId);
        } else {
            return query.getResultList();
        }
        
    }

    @Override
    public void updateRoomDetails(RoomEntity updatedRoom) {

        RoomEntity servUpdatedRoom = em.find(RoomEntity.class, updatedRoom.getRoomId());

        servUpdatedRoom.setRoomNumber(updatedRoom.getRoomNumber());
        servUpdatedRoom.setRoomType(updatedRoom.getRoomType());
        servUpdatedRoom.setDisabled(updatedRoom.isDisabled());

    }

    @Override
    public void deleteRoombyID(Long roomID) throws RoomCannotBeDeletedException, RoomCannotBeFoundException {

        RoomEntity roomToBeDeleted = em.find(RoomEntity.class, roomID);

        if (roomToBeDeleted == null) {
            throw new RoomCannotBeFoundException("Room does not exists for the entered Room Number");
        } else if (roomToBeDeleted.isRoomAllocated()) {
            throw new RoomCannotBeDeletedException("Room is currently in used, cannot be deleted!");
        } else {
            RoomTypeEntity roomType = roomToBeDeleted.getRoomType();
            roomType.getRooms().remove(roomToBeDeleted);
        }

        em.remove(roomToBeDeleted);
    }

    @Override
    public List<RoomEntity> retrieveAllRooms() {

        Query query = em.createQuery("SELECT r FROM RoomEntity r");
        List<RoomEntity> rooms = query.getResultList();

        for (RoomEntity r : rooms) {
            r.getRoomType();
        }

        return rooms;
    }

    @Override
    public List<RoomEntity> retrieveAllAvailableRooms() {

        Query query = em.createQuery("SELECT r FROM RoomEntity r WHERE r.roomStatusAvail = TRUE");
        List<RoomEntity> rooms = query.getResultList();

        for (RoomEntity r : rooms) {
            r.getRoomType();
        }

        return rooms;
    }

    @Override
    public List<RoomEntity> retrieveAllRoomsByRoomType(Long roomTypeId) throws RoomCannotBeFoundException {

        RoomTypeEntity roomType = em.find(RoomTypeEntity.class, roomTypeId);

        Query query = em.createQuery("SELECT r from RoomEntity r WHERE r.RoomType := inRoomType");
        query.setParameter("inRoomType", roomType);
        List<RoomEntity> rooms = query.getResultList();

        if (rooms.isEmpty()) {
            throw new RoomCannotBeFoundException("Rooms does not exist for Room Type ID entered!");
        }

        return rooms;

    }
}
