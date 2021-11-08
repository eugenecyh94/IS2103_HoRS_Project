package ejb.session.stateless;

import entity.ReservationEntity;
import entity.RoomTypeEntity;
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
            throw new RoomTypeCannotBeFoundException("Room Type does not exist for the entered ID!");
        }

        roomType.getRoomAmenities().size();
        roomType.getRooms().size();
        roomType.getReservations().size();

        return roomType;

    }

    @Override
    public RoomTypeEntity retrieveRoomTypeByName(String name) throws RoomTypeCannotBeFoundException {

        Query query = em.createQuery("SELECT rt from RoomTypeEntity rt WHERE rt.name = " + "'" + name + "'");
        try {
            RoomTypeEntity roomType = (RoomTypeEntity) query.getSingleResult();
            roomType.getRoomAmenities().size();
            roomType.getRooms().size();
            roomType.getReservations().size();
            return roomType;
        } catch (NoResultException ex) {
            throw new RoomTypeCannotBeFoundException("Room Type does not exist for the entered name!");
        }

    }

    @Override
    public void updateRoomTypeDetails(RoomTypeEntity updatedRoomType) {

        em.merge(updatedRoomType);

        //assumption is on the menu options will be printed for user to edit whatever attribute they want
        //any attribute that is not edited will be returned as original attribute result.
    }

    @Override
    public void deleteRoomTypebyID(Long roomTypeID) throws RoomTypeCannotBeDeletedException {
        RoomTypeEntity roomTypeToBeDeleted = em.find(RoomTypeEntity.class, roomTypeID);
        Query query = em.createQuery("SELECT rs FROM ReservationEntity rs");
        List<ReservationEntity> reservations = query.getResultList();

        for (ReservationEntity r : reservations) {
            if (roomTypeToBeDeleted.equals(r.getRoomType())) {
                r.getRoomType().setRoomTypeEnabled(false);
                throw new RoomTypeCannotBeDeletedException("Room type cannot be deleted! Room type is disabled.");
            } else {
                em.remove(roomTypeToBeDeleted);
            }
        }
    }

    @Override
    public List<RoomTypeEntity> retrieveAllRoomTypes() {

        Query query = em.createQuery("SELECT rt FROM RoomTypeEntity rt WHERE rt.roomTypeEnabled = TRUE");
        List<RoomTypeEntity> roomTypes = query.getResultList();

        for (RoomTypeEntity rt : roomTypes) {
            rt.getRoomAmenities().size();
            rt.getRooms().size();
            rt.getReservations().size();
        }

        return roomTypes;
    }

}
