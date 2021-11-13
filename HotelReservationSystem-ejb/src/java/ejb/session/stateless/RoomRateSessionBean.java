package ejb.session.stateless;

import entity.RoomRateEntity;
import entity.RoomTypeEntity;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.RateTypeEnum;
import util.exception.RoomCannotBeFoundException;
import util.exception.RoomRateCannotBeFoundException;
import util.exception.RoomTypeCannotBeFoundException;

@Stateless
public class RoomRateSessionBean implements RoomRateSessionBeanRemote, RoomRateSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    public RoomRateSessionBean() {
    }

    @Override
    public RoomRateEntity createNewRoomRate(RoomRateEntity newRoomRate) throws RoomTypeCannotBeFoundException{

        if(em.find(RoomTypeEntity.class, newRoomRate.getRoomType().getRoomTypeId()) == null){
            throw new RoomTypeCannotBeFoundException("Room Type entered for rate is invalid!");
        }
        
        em.persist(newRoomRate);
        em.flush();

        return newRoomRate;

    }

    @Override
    public RoomRateEntity retrieveRoomRateById(Long roomRateId) throws RoomRateCannotBeFoundException{

  
        RoomRateEntity roomRate = em.find(RoomRateEntity.class, roomRateId);
  
        if(roomRate == null){
        throw new RoomRateCannotBeFoundException("Room Rate does not exists for the entered ID!");
        }
        
        return roomRate;

    }

    @Override
    public List<RoomRateEntity> retrieveRoomRateByRoomType(Long roomTypeId) throws RoomCannotBeFoundException {

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

        RoomRateEntity updatedRoomRate = em.find(RoomRateEntity.class, roomRate.getRoomRateId());
        
        updatedRoomRate.setRateName(roomRate.getRateName());
        updatedRoomRate.setRoomType(roomRate.getRoomType());
        updatedRoomRate.setRateType(roomRate.getRateType());
        updatedRoomRate.setRate(roomRate.getRate());
        updatedRoomRate.setStartDate(roomRate.getStartDate());
        updatedRoomRate.setEndDate(roomRate.getEndDate());
        
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

    @Override
    public RoomRateEntity selectDailyRoomRate(LocalDate dailyDate, Long roomTypeId, boolean online) throws RoomRateCannotBeFoundException {

        RoomTypeEntity roomType = em.find(RoomTypeEntity.class, roomTypeId);

        Query query = em.createQuery("SELECT rr from RoomRateEntity rr WHERE rr.roomType = :inRoomType");
        query.setParameter("inRoomType", roomType);
        
        List<RoomRateEntity> roomRates = query.getResultList();
        
        if(roomRates.isEmpty()){
            throw new RoomRateCannotBeFoundException("No daily room rate found!");
        }
        
        RoomRateEntity selectedRoomRate = new RoomRateEntity();

        for (RoomRateEntity rr : roomRates) {
            rr.getRoomType();
            if (rr.getRateType() == RateTypeEnum.PROMOTION) {
                if (!(dailyDate.isBefore(rr.getStartDate()) || dailyDate.isAfter(rr.getEndDate()))) {
                    selectedRoomRate = rr;
                }
            } else if (rr.getRateType() == RateTypeEnum.PEAK) {
                if (!(dailyDate.isBefore(rr.getStartDate()) || dailyDate.isAfter(rr.getEndDate()))
                        && selectedRoomRate.getRateType() != RateTypeEnum.PROMOTION) {
                    selectedRoomRate = rr;
                }
            } else if (online && (rr.getRateType() == RateTypeEnum.NORMAL)
                    && (selectedRoomRate.getRateType() != RateTypeEnum.PROMOTION)
                    && (selectedRoomRate.getRateType() != RateTypeEnum.PEAK)) {
                selectedRoomRate = rr;
            } else if (!online && (rr.getRateType() == RateTypeEnum.PUBLISHED)
                    && (selectedRoomRate.getRateType() != RateTypeEnum.PROMOTION)
                    && (selectedRoomRate.getRateType() != RateTypeEnum.PEAK)) {
                selectedRoomRate = rr;
            }
        }

        return selectedRoomRate;
    }
}
