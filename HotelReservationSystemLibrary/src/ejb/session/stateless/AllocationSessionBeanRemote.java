/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomEntity;
import java.util.List;
import javax.ejb.Remote;
import util.exception.NoRoomAllocationException;
import util.exception.RoomAllocationUpgradedException;

/**
 *
 * @author Eugene Chua
 */
@Remote
public interface AllocationSessionBeanRemote {

    public void allocateRoom(Long reservationId) throws RoomAllocationUpgradedException, NoRoomAllocationException;
    
}
