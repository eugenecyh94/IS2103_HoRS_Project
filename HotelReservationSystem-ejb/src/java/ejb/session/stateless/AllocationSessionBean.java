/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ReservationEntity;
import entity.RoomEntity;
import entity.RoomTypeEntity;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.RoomAllocationNotUpgradedException;
import util.exception.RoomAllocationUpgradedException;

/**
 *
 * @author Eugene Chua
 */
@Stateless
public class AllocationSessionBean implements AllocationSessionBeanRemote, AllocationSessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    public AllocationSessionBean() {
    }

    @Override
    public List<RoomEntity> allocateRoom(Long reservationId) throws RoomAllocationUpgradedException, RoomAllocationNotUpgradedException {

        ReservationEntity reservation = em.find(ReservationEntity.class, reservationId);
        RoomTypeEntity reservedRoomType = reservation.getRoomType();

        int numOfRoomsRequired = reservation.getNumOfRooms();
        List<RoomEntity> roomsAllocatedToGuest = new ArrayList<>();

        //querying all rooms of the room Type
        Query query = em.createQuery("SELECT r FROM RoomEntity r WHERE r.roomType := inRoomType AND r.roomAllocated = FALSE "
                + "AND r.roomStatusAvail = TRUE AND r.disabled. = FALSE");
        query.setParameter("inRoomType", reservedRoomType);
        List<RoomEntity> roomsAvailableForAllocation = query.getResultList();

        //querying roomtypes of priority higher than reserved room type
        //and ordered based on priority from lowest to highest 
        //to handle upgrades
        Query query1 = em.createQuery("SELECT rt from RoomTypeEntity rt WHERE rt.priority > :inRoomTypePriority ORDER BY rt.priority ASC ")
                .setParameter("inRoomTypePriority", reservedRoomType.getPriority());
        List<RoomTypeEntity> roomTypesSortedByPriority = query1.getResultList();

        //if the original room type for allocation is empty or the number of rooms available are less than required
        if (roomsAvailableForAllocation.isEmpty() || (roomsAvailableForAllocation.size() < numOfRoomsRequired)) {
            //to loop through room types of higher priority and generate the list of available rooms for allocation
            for (RoomTypeEntity rt : roomTypesSortedByPriority) {
                roomsAvailableForAllocation = em.createQuery("SELECT r FROM RoomEntity r WHERE r.roomType := inRoomType AND r.roomAllocated = FALSE "
                        + "AND r.roomStatusAvail = TRUE AND r.disabled. = FALSE")
                        .setParameter("inRoomType", rt).getResultList();
                //if available rooms for allocation is not empty and the number of rooms is >= rooms required, break loop and proceed
                if (!roomsAvailableForAllocation.isEmpty() && (roomsAvailableForAllocation.size() >= reservation.getNumOfRooms())) {
                    break;
                }
            }
            //after the loop, regardless is breaked or completed, the list of available rooms for allocation checked
            //and respective exceptions will be thrown based on successful upgrade or not.
            if (!roomsAvailableForAllocation.isEmpty() && (roomsAvailableForAllocation.size() >= reservation.getNumOfRooms())) {
                for (int i = 0; i < numOfRoomsRequired; i++) {
                    roomsAvailableForAllocation.get(i).setRoomAllocated(true);
                    roomsAvailableForAllocation.get(i).setCurrentReservation(reservation);
                    roomsAllocatedToGuest.add(roomsAvailableForAllocation.get(i));
                }
                throw new RoomAllocationUpgradedException("Reserved Room Type is upgraded to "
                        + roomsAvailableForAllocation.get(0).getRoomType().getName()
                        + "!");
            } else {
                throw new RoomAllocationNotUpgradedException("Reserved Room Type is unavailble and upgrades are unavailable!");
            }
        }
        
        //allocation if the rooms for original room type are available
        for (int i = 0; i < numOfRoomsRequired; i++) {
                    roomsAvailableForAllocation.get(i).setRoomAllocated(true);
                    roomsAvailableForAllocation.get(i).setCurrentReservation(reservation);
                    roomsAllocatedToGuest.add(roomsAvailableForAllocation.get(i));
        }

        return roomsAllocatedToGuest;
    }
    
}
