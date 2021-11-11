/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import ejb.session.stateless.PartnerEntitySessionBeanLocal;
import ejb.session.stateless.ReservationEntitySessionBeanLocal;
import ejb.session.stateless.SearchSessionBeanLocal;
import entity.PartnerEntity;
import entity.ReservationEntity;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import util.exception.InvalidLoginCredentialException;
import util.exception.NoRoomTypeAvailableException;
import util.exception.PartnerNotFoundException;
import util.exception.ReservationCannotBeFoundException;

/**
 *
 * @author yunus
 */
@WebService(serviceName = "PartnerEntityWebService")
@Stateless()
public class PartnerEntityWebService {

    @EJB
    private ReservationEntitySessionBeanLocal reservationEntitySessionBeanLocal;

    @EJB
    private SearchSessionBeanLocal searchSessionBeanLocal;

    @EJB
    private PartnerEntitySessionBeanLocal partnerEntitySessionBeanLocal;

    @WebMethod(operationName = "partnerLogin")
    public PartnerEntity partnerLogin(@WebParam(name = "username") String username, @WebParam(name = "password") String password) throws InvalidLoginCredentialException {
        PartnerEntity partnerEntity = partnerEntitySessionBeanLocal.partnerLogin(username, password);
        return partnerEntity;
    }

    @WebMethod(operationName = "partnerSearchRoom")
    public List<String> partnerSearchRoom(@WebParam(name = "checkinDate") LocalDate checkinDate, @WebParam(name = "checkoutDate") LocalDate checkoutDate, @WebParam(name = "numRooms") int guestNumberOfRooms) throws NoRoomTypeAvailableException {
        List<String> availableRooms = searchSessionBeanLocal.searchAvailableRoomTypesOnline(checkinDate, checkoutDate, guestNumberOfRooms);
        return availableRooms;
    }

    @WebMethod(operationName = "partnerReserveRoom")
    public ReservationEntity partnerReserveRoom(@WebParam(name = "reservationEntity") ReservationEntity reservationEntity) {
        reservationEntity = reservationEntitySessionBeanLocal.createNewReservation(reservationEntity);
        return reservationEntity;
    }

    @WebMethod(operationName = "viewPartnerReservationDetails")
    public ReservationEntity viewPartnerReservationDetails(@WebParam(name = "reservationId") Long reservationId) throws ReservationCannotBeFoundException {
        ReservationEntity reservationEntity = reservationEntitySessionBeanLocal.retrieveReservationById(reservationId);
        return reservationEntity;
    }

    @WebMethod(operationName = "viewAllPartnerReservations")
    public List<ReservationEntity> viewAllPartnerReservations(@WebParam(name = "partnerId") Long partnerID) throws PartnerNotFoundException {
        List<ReservationEntity> reservationEntitys = reservationEntitySessionBeanLocal.retrieveAllReservationsByPartnerId(partnerID);
        return reservationEntitys;
                
    }
}
