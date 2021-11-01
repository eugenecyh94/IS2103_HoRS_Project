/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
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
public class RoomEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private RoomTypeEntity roomType;
    @Column(length = 4, nullable = false, unique = true)
    private String roomNumber;
    private boolean roomStatus;
    //private boolean roomReserved; reference from Visual Para v2
    private boolean roomAllocated;
    private boolean disabled; //for deletion of room , false = default, not deleted
    
    public RoomEntity() {
        this.roomStatus = true; // true = available
        this.roomAllocated = false; // false = not allocated
        this.disabled = false;
    }

    public RoomEntity(RoomTypeEntity roomType, String roomNumber) {
        this();
        this.roomType = roomType;
        this.roomNumber = roomNumber;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomId != null ? roomId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the roomId fields are not set
        if (!(object instanceof RoomEntity)) {
            return false;
        }
        RoomEntity other = (RoomEntity) object;
        if ((this.roomId == null && other.roomId != null) || (this.roomId != null && !this.roomId.equals(other.roomId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RoomEntity[ id=" + roomId + " ]";
    }

    public RoomTypeEntity getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomTypeEntity roomType) {
        this.roomType = roomType;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public boolean isRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(boolean roomStatus) {
        this.roomStatus = roomStatus;
    }

    public boolean isRoomAllocated() {
        return roomAllocated;
    }

    public void setRoomAllocated(boolean roomAllocated) {
        this.roomAllocated = roomAllocated;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
    
}
