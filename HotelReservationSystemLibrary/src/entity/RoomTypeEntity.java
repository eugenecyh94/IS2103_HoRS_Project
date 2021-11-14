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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import util.enumeration.BedSizeEnum;

/**
 *
 * @author Eugene Chua
 */
@Entity
public class RoomTypeEntity implements Serializable {

//attributes
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomTypeId;
    @Column(unique = true, length = 30, nullable = false)
    @NotNull
    @Size(min = 1, max = 30)
    private String name;
    @Column(length = 255)
    @Size(min = 0, max = 255)
    private String description;
    @Column(nullable = false)
    private int capacity;
    @Column(length = 10, nullable = false)
    @Size(min = 1, max = 10)
    @NotNull
    private String roomSize; //sqm - to look into using string or int or enum
    @Column(nullable = false)
    @NotNull
    private BedSizeEnum bedSize;
    @Column(nullable = false)
    @NotNull
    private List<String> roomAmenities;
    @Column(nullable = false)
    @NotNull
    private boolean roomTypeEnabled;
    @NotNull
    @Size(min = 1, max = 16)
    @Column(nullable = false, length = 16)
    private String nextHigherRoomType;

    @OneToMany(mappedBy = "roomType", fetch = FetchType.LAZY)
    private List<RoomEntity> rooms;

//constructors
    public RoomTypeEntity() {
        this.rooms = new ArrayList<>();
        this.roomAmenities = new ArrayList<>();
        this.roomTypeEnabled = true;
    }

    public RoomTypeEntity(String name, String description, int capacity, String roomSize, BedSizeEnum bedSize, List<String> roomAmenities, String priority) {
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

    public List<String> getRoomAmenities() {
        return roomAmenities;
    }

    public void setRoomAmenities(List<String> roomAmenities) {
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
