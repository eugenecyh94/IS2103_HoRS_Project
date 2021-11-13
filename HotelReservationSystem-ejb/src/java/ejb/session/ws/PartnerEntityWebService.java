/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import ejb.session.stateless.GuestEntitySessionBeanLocal;
import ejb.session.stateless.PartnerEntitySessionBeanLocal;
import ejb.session.stateless.ReservationEntitySessionBeanLocal;
import ejb.session.stateless.RoomTypeEntitySessionBeanLocal;
import ejb.session.stateless.SearchSessionBeanLocal;
import entity.GuestEntity;
import entity.PartnerEntity;
import entity.ReservationEntity;
import entity.RoomTypeEntity;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.GuestNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.NoRoomTypeAvailableException;
import util.exception.PartnerNotFoundException;
import util.exception.ReservationCannotBeFoundException;
import util.exception.RoomTypeCannotBeFoundException;

/**
 *
 * @author yunus
 */
@WebService(serviceName = "PartnerEntityWebService")
@Stateless()
public class PartnerEntityWebService {

    @EJB
    private GuestEntitySessionBeanLocal guestEntitySessionBeanLocal;

    @EJB
    private RoomTypeEntitySessionBeanLocal roomTypeEntitySessionBeanLocal;

    @EJB
    private ReservationEntitySessionBeanLocal reservationEntitySessionBeanLocal;

    @EJB
    private SearchSessionBeanLocal searchSessionBeanLocal;

    @EJB
    private PartnerEntitySessionBeanLocal partnerEntitySessionBeanLocal;
    
    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;
    
    

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    @WebMethod(operationName = "partnerLogin")
    public PartnerEntity partnerLogin(@WebParam(name = "username") String username, @WebParam(name = "password") String password) throws InvalidLoginCredentialException {
        PartnerEntity partnerEntity = partnerEntitySessionBeanLocal.partnerLogin(username, password);
        return partnerEntity;
    }

    @WebMethod(operationName = "retrieveGuestByPassportNumber")
    public GuestEntity retrieveGuestByPassportNumber(@WebParam(name = "passportNumber") String passportNumber) throws GuestNotFoundException {
        
        GuestEntity guestEntity = guestEntitySessionBeanLocal.retrieveGuestByPassportNumber(passportNumber);
        
        em.detach(guestEntity);
        guestEntity.setReservations(null);
        return guestEntity;
    }
    
    @WebMethod(operationName = "registerAsGuest")
    public GuestEntity registerAsGuest(@WebParam(name = "guestentity")GuestEntity newGuestEntity) {
        
        GuestEntity guestEntity = guestEntitySessionBeanLocal.registerAsGuest(newGuestEntity);
        
        em.detach(guestEntity);
        guestEntity.setReservations(null);
        return guestEntity;
    }
    
    @WebMethod(operationName = "addPartnerReservation")
    public PartnerEntity addPartnerReservation(@WebParam(name ="reservationEntity") Long reservationEntityId, @WebParam(name = "partnerId") Long partnerId) throws ReservationCannotBeFoundException, PartnerNotFoundException {
        
        PartnerEntity partnerEntity = partnerEntitySessionBeanLocal.addParnterReservation(reservationEntityId, partnerId);
        return partnerEntity;
    }
    
    @WebMethod(operationName = "partnerSearchRoom")
    public List<String> partnerSearchRoom(@WebParam(name = "checkinDate") String checkinDate, @WebParam(name = "checkoutDate") String checkoutDate, @WebParam(name = "numRooms") int guestNumberOfRooms, @WebParam(name = "numAdults") int numAdults) throws NoRoomTypeAvailableException, RoomTypeCannotBeFoundException{
        
        LocalDate checkInDate = LocalDate.parse(checkinDate, formatter);
        LocalDate checkOutDate = LocalDate.parse(checkoutDate, formatter);
        List<String> availableRooms = searchSessionBeanLocal.searchAvailableRoomTypesOnline(checkInDate, checkOutDate, guestNumberOfRooms, numAdults);
        
        return availableRooms;
    }

    @WebMethod(operationName = "partnerReserveRoom")
    public ReservationEntity partnerReserveRoom(@WebParam(name = "reservationEntity") ReservationEntity reservationEntity, @WebParam(name = "checkinDate") String checkinDate, @WebParam(name = "checkoutDate") String checkoutDate,@WebParam(name = "guestId") Long guestId) throws GuestNotFoundException {
        LocalDate checkInDate = LocalDate.parse(checkinDate, formatter);
        LocalDate checkOutDate = LocalDate.parse(checkoutDate, formatter);
        
        reservationEntity.setCheckInDate(checkInDate);
        reservationEntity.setCheckOutDate(checkOutDate);
        
        reservationEntity = reservationEntitySessionBeanLocal.createNewGuestReservation(reservationEntity, guestId);
        
        em.detach(reservationEntity);
        reservationEntity.setRoomType(null);
        reservationEntity.setGuest(null);
        
        return reservationEntity;
    }

    @WebMethod(operationName = "viewPartnerReservationDetails")
    public ReservationEntity viewPartnerReservationDetails(@WebParam(name = "reservationId") Long reservationId) throws ReservationCannotBeFoundException {
        ReservationEntity reservationEntity = reservationEntitySessionBeanLocal.retrieveReservationById(reservationId);
        
        em.detach(reservationEntity);
        reservationEntity.setRoomType(null);
        reservationEntity.setGuest(null);
        
        return reservationEntity;
    }

    @WebMethod(operationName = "viewAllPartnerReservations")
    public List<ReservationEntity> viewAllPartnerReservations(@WebParam(name = "partnerId") Long partnerID) throws PartnerNotFoundException {
        List<ReservationEntity> reservationEntitys = reservationEntitySessionBeanLocal.retrieveAllReservationsByPartnerId(partnerID);
        
        em.detach(reservationEntitys);
        
        for(ReservationEntity reservationEntity : reservationEntitys) {
            reservationEntity.setGuest(null);
            RoomTypeEntity roomTypeEntity = reservationEntity.getRoomType();
            em.detach(roomTypeEntity);
            
            roomTypeEntity.setReservations(null);
        }
        return reservationEntitys;
                
    }
    
    @WebMethod(operationName = "retreiveRoomTypeEntityByName")
    public RoomTypeEntity retreiveRoomTypeEntityByName (@WebParam(name = "roomTypeName") String roomTypeName) throws RoomTypeCannotBeFoundException {
        RoomTypeEntity roomTypeEntity = roomTypeEntitySessionBeanLocal.retrieveRoomTypeByName(roomTypeName);
        
        em.detach(roomTypeEntity);
        roomTypeEntity.setReservations(null);
        roomTypeEntity.setRooms(null);
        return roomTypeEntity;
    }

            
}
