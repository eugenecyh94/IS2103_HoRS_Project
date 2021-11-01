/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author Eugene Chua
 */
@Entity
public class DateEntity implements Serializable {
    
//attribute
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dateId;
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date date;
    @Column(nullable = false)
    private int availRooms;
    
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private RoomTypeEntity roomType;
    
//constructor
    public DateEntity() {
    }

    public DateEntity(Date date, RoomTypeEntity roomType) {
        this.date = date;
        //to intialise date
        //Date firstDate2 = new Date(int year, int month, int date, int hrs, int min);
        this.roomType = roomType;
        //when date is created it is assumed all room available.
        /*conducted query on 1)respective rooms status to
        * check overall availability + 2)reservation list of room types to check
        * for any reservation of the room type and the qty on that day. 
        * total avail room = total rooms (from room type) 
        * - status not available - disabled rooms
        * - reserved qty of rooms based on reservation
method to loop through dates       
        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1))
{
    ...
}
        */
        this.availRooms = roomType.getTotalRooms(); 
    }
    
//methods 
    public Long getDateId() {
        return dateId;
    }

    public void setDateId(Long dateId) {
        this.dateId = dateId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dateId != null ? dateId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the dateId fields are not set
        if (!(object instanceof DateEntity)) {
            return false;
        }
        DateEntity other = (DateEntity) object;
        if ((this.dateId == null && other.dateId != null) || (this.dateId != null && !this.dateId.equals(other.dateId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.DateEntity[ id=" + dateId + " ]";
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getAvailRooms() {
        return availRooms;
    }

    public void setAvailRooms(int availRooms) {
        this.availRooms = availRooms;
    }

    public RoomTypeEntity getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomTypeEntity roomType) {
        this.roomType = roomType;
    }
    
}
