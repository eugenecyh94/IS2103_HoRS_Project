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
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.NoRoomAllocationException;
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
    public void allocateRoom(Long reservationId) throws RoomAllocationUpgradedException, NoRoomAllocationException {

        ReservationEntity reservation = em.find(ReservationEntity.class, reservationId);
        RoomTypeEntity reservedRoomType = reservation.getRoomType();
        RoomTypeEntity upgradedRoomType = reservedRoomType;

        int numOfRoomsRequired = reservation.getNumOfRooms();
        List<RoomEntity> roomsAllocatedToGuest = new ArrayList<>();

        //querying all rooms of the room Type 
        Query query = em.createQuery("SELECT r FROM RoomEntity r WHERE r.roomType = :inRoomType AND r.roomAllocated = FALSE "
                + "AND r.roomStatusAvail = TRUE AND r.disabled = FALSE");
        query.setParameter("inRoomType", reservedRoomType);
        List<RoomEntity> roomsAvailableForAllocation = query.getResultList();

        //allocation if the rooms for original room type are available 
        if (!roomsAvailableForAllocation.isEmpty() && (roomsAvailableForAllocation.size() >= reservation.getNumOfRooms())) {
            for (int i = 0; i < numOfRoomsRequired; i++) {
                roomsAvailableForAllocation.get(i).setRoomAllocated(true);
                roomsAvailableForAllocation.get(i).setCurrentReservation(reservation);
                roomsAllocatedToGuest.add(roomsAvailableForAllocation.get(i));
            }
        }

        //query next room type by using current next higher room type name to handle upgrades 
        //if the original room type for allocation is empty or the number of rooms available are less than required 
        String upgradedRoomTypeName = reservedRoomType.getNextHigherRoomType();
        while (roomsAvailableForAllocation.isEmpty() || (roomsAvailableForAllocation.size() < numOfRoomsRequired)) {
            //search for higher room type 
            try {
                upgradedRoomType = (RoomTypeEntity) em.createQuery("SELECT rt FROM RoomTypeEntity rt WHERE rt.name = :inName")
                        .setParameter("inName", upgradedRoomTypeName).getSingleResult();

                roomsAvailableForAllocation = em.createQuery("SELECT r FROM RoomEntity r WHERE r.roomType = :inRoomType AND r.roomAllocated = FALSE "
                        + "AND r.roomStatusAvail = TRUE AND r.disabled = FALSE")
                        .setParameter("inRoomType", upgradedRoomType).getResultList();

                if (!roomsAvailableForAllocation.isEmpty() && (roomsAvailableForAllocation.size() >= reservation.getNumOfRooms())) {
                    for (int i = 0; i < numOfRoomsRequired; i++) {
                        roomsAvailableForAllocation.get(i).setRoomAllocated(true);
                        roomsAvailableForAllocation.get(i).setCurrentReservation(reservation);
                        roomsAllocatedToGuest.add(roomsAvailableForAllocation.get(i));
                    }
                    throw new RoomAllocationUpgradedException("Reserved Room Type is upgraded to "
                            + upgradedRoomType.getName() + "!");
                } else {
                    upgradedRoomTypeName = upgradedRoomType.getNextHigherRoomType();
                }
            } catch (NoResultException ex) {
                throw new NoRoomAllocationException("Reserved Room Type is unavailable and upgrades are unavailable!");
            }
        }
    }
}
