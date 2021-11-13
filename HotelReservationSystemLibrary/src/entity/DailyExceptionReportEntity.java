/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Eugene Chua
 */
@Entity
public class DailyExceptionReportEntity implements Serializable {

    //attributes
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long exceptionReportId;
    @Column(nullable = false, unique = true)
    private LocalDate date;
    @Column(nullable = false)
    private List<String> exceptionDetails;

    //constructors
    public DailyExceptionReportEntity() {
        this.exceptionDetails = new ArrayList<>();
    }

    public DailyExceptionReportEntity(LocalDate date) {
        this();
        this.date = date;
    }
    
    //methods    
    public Long getExceptionReportId() {
        return exceptionReportId;
    }

    public void setExceptionReportId(Long exceptionReportId) {
        this.exceptionReportId = exceptionReportId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (exceptionReportId != null ? exceptionReportId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the exceptionReportId fields are not set
        if (!(object instanceof DailyExceptionReportEntity)) {
            return false;
        }
        DailyExceptionReportEntity other = (DailyExceptionReportEntity) object;
        if ((this.exceptionReportId == null && other.exceptionReportId != null) || (this.exceptionReportId != null && !this.exceptionReportId.equals(other.exceptionReportId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.DailyExceptionReportEntity[ id=" + exceptionReportId + " ]";
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<String> getExceptionDetails() {
        return exceptionDetails;
    }

    public void setExceptionDetails(List<String> exceptionDetails) {
        this.exceptionDetails = exceptionDetails;
    }
    
}
