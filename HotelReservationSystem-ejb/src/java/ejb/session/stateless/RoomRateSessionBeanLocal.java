package ejb.session.stateless;

import entity.RoomRateEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.RoomCannotBeFoundException;

@Local
public interface RoomRateSessionBeanLocal {

    public RoomRateEntity createNewRoomRate(RoomRateEntity newRoomRate);

    public RoomRateEntity RetrieveRoomRateById(Long roomRateId);

    public List<RoomRateEntity> RetrieveRoomRateByRoomType(Long roomTypeId) throws RoomCannotBeFoundException;

    public void updateRoomRate(RoomRateEntity roomRate);

    public void deleteRoomRate(Long roomRateId);

    public List<RoomRateEntity> retrieveAllRoomRates();

}
