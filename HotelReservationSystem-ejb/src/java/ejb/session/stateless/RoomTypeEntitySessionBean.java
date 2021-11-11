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
            throw new RoomTypeCannotBeFoundException("Room Type does not exists for the entered ID!");
        }

        roomType.getRoomAmenities().size();
        roomType.getRooms().size();
        roomType.getReservations().size();

        return roomType;

    }

    @Override
    public RoomTypeEntity retrieveRoomTypeByName(String name) throws RoomTypeCannotBeFoundException {

        Query query = em.createQuery("SELECT rt from RoomTypeEntity rt WHERE rt.name := inRoomTypeName ");
        query.setParameter("inRoomTypeName", name);
        try {
            RoomTypeEntity roomType = (RoomTypeEntity) query.getSingleResult();
            roomType.getRoomAmenities().size();
            roomType.getRooms().size();
            roomType.getReservations().size();
            return roomType;
        } catch (NoResultException ex) {
            throw new RoomTypeCannotBeFoundException("Room Type does not exists for the entered name!");
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
    public List<RoomTypeEntity> retrieveAllRoomTypes() throws RoomTypeCannotBeFoundException {

        Query query = em.createQuery("SELECT r FROM RoomTypeEntity r");
        //query.setParameter(":value", Boolean.FALSE);
        //List<RoomTypeEntity> roomTypes = query.getResultList();

        /*if (roomTypes != null) {
                for (RoomTypeEntity rt : roomTypes) {
                    System.out.println("Error reading");
                    rt.getRoomAmenities().size();
                    rt.getRooms().size();
                    rt.getReservations().size();
                    System.out.println("No Error Reading");
                }
            } else {
                throw new RoomTypeCannotBeFoundException("No Room Types");
            }*/
        return query.getResultList();

    }

    public void persist(Object object) {
        em.persist(object);
    }

}
