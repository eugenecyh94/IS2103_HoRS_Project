package ejb.session.stateless;

import entity.RoomRateEntity;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Remote;
import util.exception.RoomCannotBeFoundException;
import util.exception.RoomRateCannotBeFoundException;
import util.exception.RoomTypeCannotBeFoundException;

@Remote
public interface RoomRateSessionBeanRemote {

    public RoomRateEntity createNewRoomRate(RoomRateEntity newRoomRate) throws RoomTypeCannotBeFoundException;

    public RoomRateEntity retrieveRoomRateById(Long roomRateId) throws RoomRateCannotBeFoundException;

    public List<RoomRateEntity> retrieveRoomRateByRoomType(Long roomTypeId) throws RoomCannotBeFoundException;

    public void updateRoomRate(RoomRateEntity roomRate);

    public void deleteRoomRate(Long roomRateId);

    public List<RoomRateEntity> retrieveAllRoomRates();

    public RoomRateEntity selectDailyRoomRate(LocalDate dailyDate, Long roomTypeId, boolean online) throws RoomRateCannotBeFoundException;
    
}
