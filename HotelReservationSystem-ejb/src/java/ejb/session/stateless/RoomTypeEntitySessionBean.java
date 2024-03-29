package ejb.session.stateless;

import entity.ReservationEntity;
import entity.RoomEntity;
import entity.RoomRateEntity;
import entity.RoomTypeEntity;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.RoomTypeCannotBeDeletedException;
import util.exception.RoomTypeCannotBeFoundException;

@Stateless
public class RoomTypeEntitySessionBean implements RoomTypeEntitySessionBeanRemote, RoomTypeEntitySessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    public RoomTypeEntitySessionBean() {

    }

    @Override
    public RoomTypeEntity createNewRoomType(RoomTypeEntity newRoomType) {

        em.persist(newRoomType);
        em.flush();

        return newRoomType;

    }

    @Override
    public RoomTypeEntity retrieveRoomTypeById(Long roomTypeId) throws RoomTypeCannotBeFoundException {

        RoomTypeEntity roomType = em.find(RoomTypeEntity.class, roomTypeId);

        if (roomType == null) {
            throw new RoomTypeCannotBeFoundException("Room Type does not exists for the entered ID!");
        }

        roomType.getRoomAmenities().size();
        roomType.getRooms().size();

        return roomType;

    }

    @Override
    public RoomTypeEntity retrieveRoomTypeByName(String name) throws RoomTypeCannotBeFoundException {

        Query query = em.createQuery("SELECT rt FROM RoomTypeEntity rt WHERE rt.name = :inRoomTypeName ");
        query.setParameter("inRoomTypeName", name);
        try {
            RoomTypeEntity roomType = (RoomTypeEntity) query.getSingleResult();
            roomType.getRoomAmenities().size();
            roomType.getRooms().size();
            return roomType;
        } catch (NoResultException ex) {
            throw new RoomTypeCannotBeFoundException("Room Type does not exists for the entered name!");
        }

    }

    @Override
    public void updateRoomTypeDetails(RoomTypeEntity updatedRoomType) {

        RoomTypeEntity servUpdatedRoomType = em.find(RoomTypeEntity.class, updatedRoomType.getRoomTypeId());

        servUpdatedRoomType.setName(updatedRoomType.getName());
        servUpdatedRoomType.setDescription(updatedRoomType.getDescription());
        servUpdatedRoomType.setCapacity(updatedRoomType.getCapacity());
        servUpdatedRoomType.setRoomAmenities(updatedRoomType.getRoomAmenities());
        servUpdatedRoomType.setBedSize(updatedRoomType.getBedSize());
        servUpdatedRoomType.setNextHigherRoomType(updatedRoomType.getNextHigherRoomType());

    }

    @Override
    public void deleteRoomTypebyID(Long roomTypeID) throws RoomTypeCannotBeDeletedException {
        RoomTypeEntity roomTypeToBeDeleted = em.find(RoomTypeEntity.class, roomTypeID);

        //query reservations
        Query query = em.createQuery("SELECT rs FROM ReservationEntity rs WHERE rs.roomType = :inRoomType")
                .setParameter("inRoomType", roomTypeToBeDeleted);
        List<ReservationEntity> reservations = query.getResultList();
        Query query1 = em.createQuery("SELECT r FROM RoomEntity r WHERE r.roomType = :inRoomType")
                .setParameter("inRoomType", roomTypeToBeDeleted);
        List<RoomEntity> rooms = query1.getResultList();
        Query query2 = em.createQuery("SELECT rr FROM RoomRateEntity rr WHERE rr.roomType = :inRoomType")
                .setParameter("inRoomType", roomTypeToBeDeleted);
        List<RoomRateEntity> roomRates = query2.getResultList();

        if (reservations.isEmpty() && rooms.isEmpty() && roomRates.isEmpty()) {
            em.remove(roomTypeToBeDeleted);
            System.out.println("Test Message: Room Deleted");
        } else {
            System.out.println("Test Message: Room Disabled");
            roomTypeToBeDeleted.setRoomTypeEnabled(false);
            throw new RoomTypeCannotBeDeletedException("Room is Disabled");
        }

    }

    @Override
    public List<RoomTypeEntity> retrieveAllRoomTypes() {

        Query query = em.createQuery("SELECT r FROM RoomTypeEntity r WHERE r.roomTypeEnabled = TRUE");

        return query.getResultList();

    }

}
