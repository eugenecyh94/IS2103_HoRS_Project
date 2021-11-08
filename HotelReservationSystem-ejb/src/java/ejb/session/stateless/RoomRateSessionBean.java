package ejb.session.stateless;

import entity.RoomRateEntity;
import entity.RoomTypeEntity;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.RoomCannotBeFoundException;

@Stateless
public class RoomRateSessionBean implements RoomRateSessionBeanRemote, RoomRateSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    public RoomRateSessionBean() {
    }

    @Override
    public RoomRateEntity createNewRoomRate(RoomRateEntity newRoomRate) {

        em.persist(newRoomRate);
        em.flush();

        return newRoomRate;

    }

    @Override
    public RoomRateEntity RetrieveRoomRateById(Long roomRateId) {

        RoomRateEntity roomRate = em.find(RoomRateEntity.class, roomRateId);

        return roomRate;

    }

    @Override
    public List<RoomRateEntity> RetrieveRoomRateByRoomType(Long roomTypeId) throws RoomCannotBeFoundException {

        RoomTypeEntity roomType = em.find(RoomTypeEntity.class, roomTypeId);

        Query query = em.createQuery("SELECT rr from RoomRateEntity rr WHERE rr.RoomType := inRoomType");
        query.setParameter("inRoomType", roomType);
        List<RoomRateEntity> roomRates = query.getResultList();

        if (roomRates.isEmpty()) {
            throw new RoomCannotBeFoundException("Rooms does not exist for Room Type ID entered!");
        }

        return roomRates;
    }

    @Override
    public void updateRoomRate(RoomRateEntity roomRate) {

        em.merge(roomRate);
    }

    @Override
    public void deleteRoomRate(Long roomRateId) {

        RoomRateEntity roomRate = em.find(RoomRateEntity.class, roomRateId);

        em.remove(roomRate);

    }

    @Override
    public List<RoomRateEntity> retrieveAllRoomRates() {

        List<RoomRateEntity> roomRates = new ArrayList<>();

        Query query = em.createQuery("SELECT rr from RoomRateEntity rr");
        roomRates = query.getResultList();

        return roomRates;
    }
    
}
