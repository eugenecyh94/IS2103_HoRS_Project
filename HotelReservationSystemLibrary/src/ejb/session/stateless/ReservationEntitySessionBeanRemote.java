/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ReservationEntity;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Remote;
import util.exception.GuestNotFoundException;
import util.exception.ReservationCannotBeFoundException;

/**
 *
 * @author Eugene Chua
 */
@Remote
public interface ReservationEntitySessionBeanRemote {

    public ReservationEntity createNewReservation(ReservationEntity reservation);

    public ReservationEntity retrieveReservationById(Long reservationId) throws ReservationCannotBeFoundException;

    public List<ReservationEntity> retrieveAllReservationsByGuestId(String guestId) throws GuestNotFoundException;

    public List<ReservationEntity> retrieveAllReservationsByDates(LocalDate guestCheckInDate, LocalDate guestCheckOutDate);
    
}
