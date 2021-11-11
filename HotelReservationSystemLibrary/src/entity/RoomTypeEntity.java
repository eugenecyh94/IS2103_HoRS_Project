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
import javax.persistence.OneToMany;
import util.enumeration.BedSizeEnum;
import util.enumeration.RoomAmenitiesEnum;

/**
 *
 * @author Eugene Chua
 */
@Entity
public class RoomTypeEntity implements Serializable {

//attributes
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long roomTypeId;
    @Column(unique = true, length = 20, nullable = false)
    private String name;
    @Column(length = 255)
    private String description;
    @Column(nullable = false)
    private int capacity;
    @Column(length = 10, nullable = false)
    private String roomSize; //sqm - to look into using string or int or enum
    @Column(nullable = false)
    private BedSizeEnum bedSize;
    @Column(nullable = false)
    private List<RoomAmenitiesEnum> roomAmenities;
    @Column(nullable = false)
    private Boolean roomTypeEnabled;
    @Column(nullable = false, length = 16)
    private String nextHigherRoomType;

    @OneToMany(mappedBy = "roomType", fetch = FetchType.LAZY)
    private List<RoomEntity> rooms;

    @OneToMany(mappedBy = "roomType", fetch = FetchType.LAZY)
    private List<ReservationEntity> reservations;

//constructors
    public RoomTypeEntity() {
        this.rooms = new ArrayList<>();
        this.reservations = new ArrayList<>();
        this.roomAmenities = new ArrayList<>();
        this.roomTypeEnabled = Boolean.FALSE;
    }

    public RoomTypeEntity(String name, String description, int capacity, String roomSize, BedSizeEnum bedSize, List<RoomAmenitiesEnum> roomAmenities, String priority) {
        this();
        this.name = name;
        this.description = description;
        this.capacity = capacity;
        this.roomSize = roomSize;
        this.bedSize = bedSize;
        this.roomAmenities = roomAmenities;
        this.nextHigherRoomType = priority;
    }
    
    

    //methods
    public Long getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(Long roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomTypeId != null ? roomTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the roomTypeId fields are not set
        if (!(object instanceof RoomTypeEntity)) {
            return false;
        }
        RoomTypeEntity other = (RoomTypeEntity) object;
        if ((this.roomTypeId == null && other.roomTypeId != null) || (this.roomTypeId != null && !this.roomTypeId.equals(other.roomTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RoomTypeEntity[ id=" + roomTypeId + " ]";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getRoomSize() {
        return roomSize;
    }

    public void setRoomSize(String roomSize) {
        this.roomSize = roomSize;
    }

    public BedSizeEnum getBedSize() {
        return bedSize;
    }

    public void setBedSize(BedSizeEnum bedSize) {
        this.bedSize = bedSize;
    }

    public List<RoomAmenitiesEnum> getRoomAmenities() {
        return roomAmenities;
    }

    public void setRoomAmenities(List<RoomAmenitiesEnum> roomAmenities) {
        this.roomAmenities = roomAmenities;
    }

    public boolean isRoomTypeEnabled() {
        return roomTypeEnabled;
    }

    public void setRoomTypeEnabled(boolean roomTypeEnabled) {
        this.roomTypeEnabled = roomTypeEnabled;
    }

    public List<RoomEntity> getRooms() {
        return rooms;
    }

    public void setRooms(List<RoomEntity> rooms) {
        this.rooms = rooms;
    }

    public List<ReservationEntity> getReservations() {
        return reservations;
    }

    public void setReservations(List<ReservationEntity> reservations) {
        this.reservations = reservations;
    }

    /**
     * @return the priority
     */
    public String getNextHigherRoomType() {
        return nextHigherRoomType;
    }

    /**
     * @param nextHigherRoomType the priority to set
     */
    public void setNextHigherRoomType(String nextHigherRoomType) {
        this.nextHigherRoomType = nextHigherRoomType;
    }

    

}
