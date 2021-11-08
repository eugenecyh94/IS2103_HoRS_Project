/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomRateEntity;
import java.util.List;
import javax.ejb.Remote;
import util.exception.RoomCannotBeFoundException;

/**
 *
 * @author Eugene Chua
 */
@Remote
public interface RoomRateSessionBeanRemote {

    public RoomRateEntity createNewRoomRate(RoomRateEntity newRoomRate);

    public RoomRateEntity RetrieveRoomRateById(Long roomRateId);

    public List<RoomRateEntity> RetrieveRoomRateByRoomType(Long roomTypeId) throws RoomCannotBeFoundException;

    public void updateRoomRate(RoomRateEntity roomRate);

    public void deleteRoomRate(Long roomRateId);

    public List<RoomRateEntity> retrieveAllRoomRates();
    
}
