package ejb.session.stateless;

import entity.RoomRateEntity;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Local;
import util.exception.RoomCannotBeFoundException;

@Local
public interface RoomRateSessionBeanLocal {

    public RoomRateEntity createNewRoomRate(RoomRateEntity newRoomRate);

    public RoomRateEntity retrieveRoomRateById(Long roomRateId);

    public List<RoomRateEntity> retrieveRoomRateByRoomType(Long roomTypeId) throws RoomCannotBeFoundException;

    public void updateRoomRate(RoomRateEntity roomRate);

    public void deleteRoomRate(Long roomRateId);

    public List<RoomRateEntity> retrieveAllRoomRates();

    public RoomRateEntity selectDailyRoomRate(LocalDate dailyDate, Long roomTypeId, boolean online);
}
