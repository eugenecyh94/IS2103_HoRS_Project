package ejb.session.stateless;

import entity.ReservationEntity;
import entity.RoomEntity;
import entity.RoomTypeEntity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.NoRoomTypeAvailableException;
import util.exception.RoomTypeCannotBeFoundException;

@Stateless
public class SearchSessionBean implements SearchSessionBeanRemote, SearchSessionBeanLocal {

    @EJB
    RoomEntitySessionBeanLocal roomEntitySessionBeanLocal;
    @EJB
    RoomTypeEntitySessionBeanLocal roomTypeEntitySessionBeanLocal;
    @EJB
    ReservationEntitySessionBeanLocal reservationEntitySessionBeanLocal;
    @EJB
    RoomRateSessionBeanLocal roomRateSessionBeanLocal;

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    public SearchSessionBean() {
    }

@Override
    public List<String> searchAvailableRoomTypesWalkIn(LocalDate checkInDate, LocalDate checkOutDate, int guestNumberOfRooms) throws NoRoomTypeAvailableException, RoomTypeCannotBeFoundException{

        List<String> availableRoomTypeAndRatePerNight = new ArrayList<>();
        List<RoomTypeEntity> roomTypes = roomTypeEntitySessionBeanLocal.retrieveAllRoomTypes();
        List<RoomEntity> rooms = roomEntitySessionBeanLocal.retrieveAllRooms();
        List<ReservationEntity> dateFilteredreservations = reservationEntitySessionBeanLocal.retrieveAllReservationsBySearchDates(checkInDate, checkOutDate);

        //Main logic to search available rooms and respective rate
        for (RoomTypeEntity rt : roomTypes) {
            int totalRoomsAvailable = 0;
            for (RoomEntity r : rooms) {
                if (r.getRoomType().equals(rt) && r.isRoomStatusAvail() && !r.isDisabled()) {
                    totalRoomsAvailable++;
                }
            }
            int periodAvailableRooms = totalRoomsAvailable;
            BigDecimal periodRoomTypeRates = new BigDecimal("0");

            //Looping through every day
            for (LocalDate dailyDate = checkInDate; dailyDate.isEqual(checkOutDate); dailyDate.plusDays(1)) {
                int dailyAvailableRooms = totalRoomsAvailable;
                //parameter for calculate rate is false, as search is done in front counter - refer to roomratesessionbean
                periodRoomTypeRates.add(roomRateSessionBeanLocal.selectDailyRoomRate(dailyDate, rt.getRoomTypeId(), false).getRate()); 
                for (ReservationEntity rs : dateFilteredreservations) {
                    if (rs.getRoomType().equals(rt) && !(dailyDate.isBefore(rs.getCheckInDate()) || dailyDate.isAfter(rs.getCheckOutDate()))) {
                        dailyAvailableRooms -= rs.getNumOfRooms();
                    }
                }

                if (dailyAvailableRooms <= periodAvailableRooms) {
                    periodAvailableRooms = dailyAvailableRooms;
                }
            }

            //if avail rooms > guest required number of rooms
            if ((periodAvailableRooms >= guestNumberOfRooms) && (periodAvailableRooms > 0)) {
                availableRoomTypeAndRatePerNight.add(rt.getName());
                availableRoomTypeAndRatePerNight.add("SGD " + periodRoomTypeRates.toString()); //can change to per night if neccessary;
            }
        }
        
        if(availableRoomTypeAndRatePerNight.isEmpty()) {
            throw new NoRoomTypeAvailableException("No Room Type is available for this period!");
        }

        return availableRoomTypeAndRatePerNight;
    }

    @Override
    public List<String> searchAvailableRoomTypesOnline(LocalDate checkInDate, LocalDate checkOutDate, int guestNumberOfRooms) throws NoRoomTypeAvailableException, RoomTypeCannotBeFoundException {

        List<String> availableRoomTypeAndRatePerNight = new ArrayList<>();
        List<RoomTypeEntity> roomTypes = roomTypeEntitySessionBeanLocal.retrieveAllRoomTypes();
        List<RoomEntity> rooms = roomEntitySessionBeanLocal.retrieveAllRooms();
        List<ReservationEntity> dateFilteredreservations = reservationEntitySessionBeanLocal.retrieveAllReservationsBySearchDates(checkInDate, checkOutDate);

        //Main logic to search available rooms and respective rate
        for (RoomTypeEntity rt : roomTypes) {
            int totalRoomsAvailable = 0;
            for (RoomEntity r : rooms) {
                if (r.getRoomType().equals(rt) && r.isRoomStatusAvail() && !r.isDisabled()) {
                    totalRoomsAvailable++;
                }
            }
            int periodAvailableRooms = totalRoomsAvailable;
            BigDecimal periodRoomTypeRates = new BigDecimal("0");

            //Looping through every day
            for (LocalDate dailyDate = checkInDate; dailyDate.isEqual(checkOutDate); dailyDate.plusDays(1)) {
                int dailyAvailableRooms = totalRoomsAvailable;
                //parameter for calculate rate is true, as search is done online - refer to roomratesessionbean
                periodRoomTypeRates.add(roomRateSessionBeanLocal.selectDailyRoomRate(dailyDate, rt.getRoomTypeId(), true).getRate()); 
                for (ReservationEntity rs : dateFilteredreservations) {
                    if (rs.getRoomType().equals(rt) && !(dailyDate.isBefore(rs.getCheckInDate()) || dailyDate.isAfter(rs.getCheckOutDate()))) {
                        dailyAvailableRooms -= rs.getNumOfRooms();
                    }
                }

                if (dailyAvailableRooms <= periodAvailableRooms) {
                    periodAvailableRooms = dailyAvailableRooms;
                }
            }
   
            //if avail rooms > guest required number of rooms
            if ((periodAvailableRooms >= guestNumberOfRooms) && (periodAvailableRooms > 0)) {
                availableRoomTypeAndRatePerNight.add(rt.getName());
                availableRoomTypeAndRatePerNight.add("SGD " + periodRoomTypeRates.toString()); //can change to per night if neccessary;
            }
        }
        
        if(availableRoomTypeAndRatePerNight.isEmpty()) {
            throw new NoRoomTypeAvailableException("No Room Type is available for this period!");
        }

        return availableRoomTypeAndRatePerNight;
    }
    
}
