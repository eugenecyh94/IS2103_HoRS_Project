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
import util.exception.PartnerNotFoundException;
import util.exception.ReservationCannotBeFoundException;

/**
 *
 * @author Eugene Chua
 */
@Remote
public interface ReservationEntitySessionBeanRemote {

    public ReservationEntity createNewGuestReservation(ReservationEntity reservation, Long guestId) throws GuestNotFoundException;

    public ReservationEntity retrieveReservationById(Long reservationId) throws ReservationCannotBeFoundException;

    public List<ReservationEntity> retrieveAllReservationsByGuestId(Long guestId) throws GuestNotFoundException;

    public List<ReservationEntity> retrieveAllReservationsBySearchDates(LocalDate guestCheckInDate, LocalDate guestCheckOutDate);

    public List<ReservationEntity> retrieveAllReservationsByCheckInDate(LocalDate reservationCheckInDate);

    public List<ReservationEntity> retrieveAllReservationsByPartnerId(Long partnerId) throws PartnerNotFoundException;

}
