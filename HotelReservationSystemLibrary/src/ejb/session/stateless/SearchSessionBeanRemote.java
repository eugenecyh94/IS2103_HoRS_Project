/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import java.time.LocalDate;
import java.util.List;
import javax.ejb.Remote;
import util.exception.NoRoomTypeAvailableException;

/**
 *
 * @author Eugene Chua
 */
@Remote
public interface SearchSessionBeanRemote {

    public List<String> searchAvailableRoomTypes(LocalDate checkInDate, LocalDate checkOutDate, int guestNumberOfRooms) throws NoRoomTypeAvailableException;
    
}
