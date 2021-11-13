/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

/**
 *
 * @author Eugene Chua
 */
@Entity
public class GuestEntity implements Serializable {

//attributes
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long guestId;
    @Column(nullable = false, length = 16)
    private String firstName;
    @Column(nullable = false, length = 16)
    private String lastName;
    @Column(nullable = false, length = 24, unique = true)
    private String passportNumber;
    @Column(nullable = false, length = 128, unique = true)
    private String email;
    @Column(nullable = false, length = 128, unique = true)
    private String mobileNumber;
    @Column(nullable = false, length = 16)
    private String username;
    @Column(nullable = false, length = 8)
    private String password;
    @Column(nullable = false)
    private boolean registered;
    
    @OneToMany(mappedBy = "guest", fetch = FetchType.LAZY)
    private List<ReservationEntity> reservations;

//constructors
    public GuestEntity() {
        this.reservations = new ArrayList<>();
        this.registered = false;
        this.username = "default";
        this.password = "default";
    }

    public GuestEntity(String firstName, String lastName, String passportNumber, String email, String mobileNumber, String userName, String password) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.passportNumber = passportNumber;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.username = userName;
        this.password = password;
    }
    
//methods    
    public Long getGuestId() {
        return guestId;
    }

    public void setGuestId(Long guestId) {
        this.guestId = guestId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (guestId != null ? guestId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the guestId fields are not set
        if (!(object instanceof GuestEntity)) {
            return false;
        }
        GuestEntity other = (GuestEntity) object;
        if ((this.guestId == null && other.guestId != null) || (this.guestId != null && !this.guestId.equals(other.guestId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.GuestEntity[ id=" + guestId + " ]";
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<ReservationEntity> getReservations() {
        return reservations;
    }

    public void setReservations(List<ReservationEntity> reservations) {
        this.reservations = reservations;
    }

    public void setRegistered(Boolean registered) {
        this.setRegistered((boolean) registered);
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }
    
}
