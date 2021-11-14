package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import util.enumeration.RateTypeEnum;

@Entity
public class RoomRateEntity implements Serializable {

    //attributes
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomRateId;
    @NotNull
    @Size(min=1, max=45)
    @Column(length = 45, nullable = false)
    private String rateName;
    @NotNull
    @Column(nullable = false)
    private RateTypeEnum rateType;
    @NotNull
    @Column(nullable = false)
    private BigDecimal rate;
    private LocalDate startDate;
    private LocalDate endDate;
    @NotNull
    @OneToOne(optional = false)
    private RoomTypeEntity roomType;

    //constructors
    public RoomRateEntity() {
    }

    public RoomRateEntity(String rateName, RateTypeEnum rateType, BigDecimal rate, RoomTypeEntity roomType) {
        this.rateName = rateName;
        this.rateType = rateType;
        this.rate = rate;
        this.roomType = roomType;
    }

    public RoomRateEntity(String rateName, RateTypeEnum rateType, BigDecimal rate, RoomTypeEntity roomType, LocalDate startDate, LocalDate endDate) {
        this.rateName = rateName;
        this.rateType = rateType;
        this.rate = rate;
        this.roomType = roomType;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public RoomRateEntity(String rateName, RateTypeEnum rateType, BigDecimal rate, LocalDate startDate, LocalDate endDate, RoomTypeEntity roomType) {
        this.rateName = rateName;
        this.rateType = rateType;
        this.rate = rate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.roomType = roomType;
    }

    //methods
    public Long getRoomRateId() {
        return roomRateId;
    }

    public void setRoomRateId(Long roomRateId) {
        this.roomRateId = roomRateId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roomRateId != null ? roomRateId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the roomRateId fields are not set
        if (!(object instanceof RoomRateEntity)) {
            return false;
        }
        RoomRateEntity other = (RoomRateEntity) object;
        if ((this.roomRateId == null && other.roomRateId != null) || (this.roomRateId != null && !this.roomRateId.equals(other.roomRateId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RoomRateEntity[ id=" + roomRateId + " ]";
    }

    public String getRateName() {
        return rateName;
    }

    public void setRateName(String rateName) {
        this.rateName = rateName;
    }

    public RateTypeEnum getRateType() {
        return rateType;
    }

    public void setRateType(RateTypeEnum rateType) {
        this.rateType = rateType;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public RoomTypeEntity getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomTypeEntity roomType) {
        this.roomType = roomType;
    }

}
