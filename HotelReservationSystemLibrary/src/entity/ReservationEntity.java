/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author Eugene Chua
 */
@Entity
public class ReservationEntity implements Serializable {

//attributes
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;
    @Column(nullable = false)
    private LocalDate checkInDate;
    @Column(nullable = false)
    private LocalDate checkOutDate;
    @Column(nullable = false)
    private int numOfAdults;
    @Column(nullable = false)
    private int numOfRooms;
    @Column(nullable = false)
    private BigDecimal totalAmount;
    //is a bookingMode attribute necessary? only affects the normal / published rate, which we can filter in sb
    @ManyToOne(fetch = FetchType.LAZY)
    private RoomTypeEntity roomType;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private GuestEntity guest;

//construtors
    public ReservationEntity() {
    }

    public ReservationEntity(LocalDate checkInDate, LocalDate checkOutDate, int numOfAdults, int numOfRooms, RoomTypeEntity roomType, GuestEntity guest) {
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.numOfAdults = numOfAdults;
        this.numOfRooms = numOfRooms;
        this.roomType = roomType;
        this.guest = guest;
    }
    
    public ReservationEntity(LocalDate checkInDate, LocalDate checkOutDate, int numOfAdults, int numOfRooms, RoomTypeEntity roomType) {
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.numOfAdults = numOfAdults;
        this.numOfRooms = numOfRooms;
        this.roomType = roomType;
    }
    
//methods    
    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reservationId != null ? reservationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the reservationId fields are not set
        if (!(object instanceof ReservationEntity)) {
            return false;
        }
        ReservationEntity other = (ReservationEntity) object;
        if ((this.reservationId == null && other.reservationId != null) || (this.reservationId != null && !this.reservationId.equals(other.reservationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ReservationEntity[ id=" + reservationId + " ]";
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public int getNumOfAdults() {
        return numOfAdults;
    }

    public void setNumOfAdults(int numOfAdults) {
        this.numOfAdults = numOfAdults;
    }

    public int getNumOfRooms() {
        return numOfRooms;
    }

    public void setNumOfRooms(int numOfRooms) {
        this.numOfRooms = numOfRooms;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public RoomTypeEntity getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomTypeEntity roomType) {
        this.roomType = roomType;
    }

    public GuestEntity getGuest() {
        return guest;
    }

    public void setGuest(GuestEntity guest) {
        this.guest = guest;
    }
    
}
