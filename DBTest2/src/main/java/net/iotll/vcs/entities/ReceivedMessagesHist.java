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
@Table(name = "RECEIVED_MESSAGES_HIST")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ReceivedMessagesHist.findAll", query = "SELECT r FROM ReceivedMessagesHist r"),
    @NamedQuery(name = "ReceivedMessagesHist.findByIdMessage", query = "SELECT r FROM ReceivedMessagesHist r WHERE r.idMessage = :idMessage"),
    @NamedQuery(name = "ReceivedMessagesHist.findByMessage", query = "SELECT r FROM ReceivedMessagesHist r WHERE r.message = :message"),
    @NamedQuery(name = "ReceivedMessagesHist.findByDateTime", query = "SELECT r FROM ReceivedMessagesHist r WHERE r.dateTime = :dateTime")})
public class ReceivedMessagesHist implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdMessage")
    private Integer idMessage;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "Message")
    private String message;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DateTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTime;

    public ReceivedMessagesHist() {
    }

    public ReceivedMessagesHist(Integer idMessage) {
        this.idMessage = idMessage;
    }

    public ReceivedMessagesHist(Integer idMessage, String message, Date dateTime) {
        this.idMessage = idMessage;
        this.message = message;
        this.dateTime = dateTime;
    }

    public Integer getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(Integer idMessage) {
        this.idMessage = idMessage;
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
        hash += (idMessage != null ? idMessage.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ReceivedMessagesHist)) {
            return false;
        }
        ReceivedMessagesHist other = (ReceivedMessagesHist) object;
        if ((this.idMessage == null && other.idMessage != null) || (this.idMessage != null && !this.idMessage.equals(other.idMessage))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.iotll.vcs.entities.ReceivedMessagesHist[ idMessage=" + idMessage + " ]";
    }
    
}
