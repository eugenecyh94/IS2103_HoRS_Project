package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

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
    @Column(length = 4, nullable = false, unique = true) // need to validate minimum
    private String roomNumber;
    @Column(nullable = false)
    private boolean roomStatusAvail;
    @Column(nullable = false)
    private boolean roomAllocated;
    @Column(nullable = false)
    private boolean disabled; //for deletion of room , false = default, not deleted
    private ReservationEntity currentReservation; //check in = set, check out = null

    public RoomEntity() {
        this.roomStatusAvail = true; // true = available
        this.roomAllocated = false; // false = not allocated
        this.disabled = false;
    }

    public RoomEntity(String roomNumber) {
        this();
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

    public boolean isRoomStatusAvail() {
        return roomStatusAvail;
    }

    public void setRoomStatusAvail(boolean roomStatusAvail) {
        this.roomStatusAvail = roomStatusAvail;
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

    public ReservationEntity getCurrentReservation() {
        return currentReservation;
    }

    public void setCurrentReservation(ReservationEntity currentReservation) {
        this.currentReservation = currentReservation;
    }

}
