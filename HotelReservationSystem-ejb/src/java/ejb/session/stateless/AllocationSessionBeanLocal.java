/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RoomEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.RoomAllocationNotUpgradedException;
import util.exception.RoomAllocationUpgradedException;

/**
 *
 * @author Eugene Chua
 */
@Local
public interface AllocationSessionBeanLocal {
    
       public void allocateRoom(Long reservationId) throws RoomAllocationUpgradedException, RoomAllocationNotUpgradedException;
    
}
