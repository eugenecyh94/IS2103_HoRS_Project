/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import java.time.LocalDate;
import java.util.List;
import javax.ejb.Local;
import util.exception.NoRoomTypeAvailableException;
import util.exception.RoomTypeCannotBeFoundException;

/**
 *
 * @author Eugene Chua
 */
@Local
public interface SearchSessionBeanLocal {

    public List<String> searchAvailableRoomTypesWalkIn(LocalDate checkInDate, LocalDate checkOutDate, int guestNumberOfRooms) throws NoRoomTypeAvailableException, RoomTypeCannotBeFoundException;

    public List<String> searchAvailableRoomTypesOnline(LocalDate checkInDate, LocalDate checkOutDate, int guestNumberOfRooms) throws NoRoomTypeAvailableException, RoomTypeCannotBeFoundException;

}
