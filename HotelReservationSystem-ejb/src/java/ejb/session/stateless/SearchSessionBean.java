/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ReservationEntity;
import entity.RoomEntity;
import entity.RoomTypeEntity;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.NoRoomTypeAvailableException;

/**
 *
 * @author Eugene Chua
 */
@Stateless
public class SearchSessionBean implements SearchSessionBeanRemote, SearchSessionBeanLocal {

    @EJB
    RoomEntitySessionBeanLocal roomEntitySessionBeanLocal;
    @EJB
    RoomTypeEntitySessionBeanLocal roomTypeEntitySessionBeanLocal;
    @EJB
    ReservationEntitySessionBeanLocal reservationEntitySessionBeanLocal;

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    public SearchSessionBean() {
    }

    @Override
    public List<String> searchAvailableRoomTypes(LocalDate checkInDate, LocalDate checkOutDate, int guestNumberOfRooms) throws NoRoomTypeAvailableException {
        //need to convert date to localdate for easy iteration
        //https://mkyong.com/java8/java-8-convert-date-to-localdate-and-localdatetime/

        List<String> availableRoomTypeAndRatePerNight = new ArrayList<>();

        //get list of roomType
        List<RoomTypeEntity> roomTypes = roomTypeEntitySessionBeanLocal.retrieveAllRoomTypes();
        //get list of total available rooms for each room Type
        List<RoomEntity> rooms = roomEntitySessionBeanLocal.retrieveAllRooms();
        //get list of reservations with checkin date => provided checkin date 
        //and checkout date <= provided checkout date
        List<ReservationEntity> dateFilteredreservations = reservationEntitySessionBeanLocal.retrieveAllReservationsByDates(checkInDate, checkOutDate);

        //loop through room types
        for (RoomTypeEntity rt : roomTypes) {
            int totalRoomsAvailable = 0;
            for (RoomEntity r : rooms) {
                if (r.getRoomType().equals(rt) && r.isRoomStatusAvail() && !r.isDisabled()) {
                    totalRoomsAvailable++;
                }
            }
            int periodAvailableRooms = totalRoomsAvailable;
            //loop through period from checkin and date
            for (LocalDate dailyDate = checkInDate; dailyDate.isEqual(checkOutDate); dailyDate.plusDays(1)) {
                int dailyAvailableRooms = totalRoomsAvailable;
                for (ReservationEntity rs : dateFilteredreservations) {
                    if (rs.getRoomType().equals(rt) && !dailyDate.isAfter(rs.getCheckOutDate())) {
                        dailyAvailableRooms -= rs.getNumOfRooms();
                    }
                }

                if (dailyAvailableRooms <= periodAvailableRooms) {
                    periodAvailableRooms = dailyAvailableRooms;
                }
            }
            //if avail rooms > guest rooms
            if (periodAvailableRooms >= guestNumberOfRooms) {
                availableRoomTypeAndRatePerNight.add(rt.getName());
            }
        }
        
        return availableRoomTypeAndRatePerNight;
    }

}
