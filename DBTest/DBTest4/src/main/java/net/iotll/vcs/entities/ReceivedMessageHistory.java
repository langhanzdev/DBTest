/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.iotll.vcs.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author langhanz
 */
@Entity
@Table(name = "RECEIVED_MESSAGE_HISTORY")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ReceivedMessageHistory.findAll", query = "SELECT r FROM ReceivedMessageHistory r"),
    @NamedQuery(name = "ReceivedMessageHistory.findByIdReceivedMessageHistory", query = "SELECT r FROM ReceivedMessageHistory r WHERE r.idReceivedMessageHistory = :idReceivedMessageHistory"),
    @NamedQuery(name = "ReceivedMessageHistory.findByMessage", query = "SELECT r FROM ReceivedMessageHistory r WHERE r.message = :message"),
    @NamedQuery(name = "ReceivedMessageHistory.findByDateTime", query = "SELECT r FROM ReceivedMessageHistory r WHERE r.dateTime = :dateTime")})
public class ReceivedMessageHistory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_RECEIVED_MESSAGE_HISTORY")
    private Integer idReceivedMessageHistory;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "MESSAGE")
    private String message;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTime;

    public ReceivedMessageHistory() {
    }

    public ReceivedMessageHistory(Integer idReceivedMessageHistory) {
        this.idReceivedMessageHistory = idReceivedMessageHistory;
    }

    public ReceivedMessageHistory(Integer idReceivedMessageHistory, String message, Date dateTime) {
        this.idReceivedMessageHistory = idReceivedMessageHistory;
        this.message = message;
        this.dateTime = dateTime;
    }

    public Integer getIdReceivedMessageHistory() {
        return idReceivedMessageHistory;
    }

    public void setIdReceivedMessageHistory(Integer idReceivedMessageHistory) {
        this.idReceivedMessageHistory = idReceivedMessageHistory;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idReceivedMessageHistory != null ? idReceivedMessageHistory.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ReceivedMessageHistory)) {
            return false;
        }
        ReceivedMessageHistory other = (ReceivedMessageHistory) object;
        if ((this.idReceivedMessageHistory == null && other.idReceivedMessageHistory != null) || (this.idReceivedMessageHistory != null && !this.idReceivedMessageHistory.equals(other.idReceivedMessageHistory))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.iotll.vcs.entities.ReceivedMessageHistory[ idReceivedMessageHistory=" + idReceivedMessageHistory + " ]";
    }
    
}
