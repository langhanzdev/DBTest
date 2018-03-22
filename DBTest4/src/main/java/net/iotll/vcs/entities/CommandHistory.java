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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "COMMAND_HISTORY")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CommandHistory.findAll", query = "SELECT c FROM CommandHistory c"),
    @NamedQuery(name = "CommandHistory.findByIdCommandHistory", query = "SELECT c FROM CommandHistory c WHERE c.idCommandHistory = :idCommandHistory"),
    @NamedQuery(name = "CommandHistory.findByCategory", query = "SELECT c FROM CommandHistory c WHERE c.category = :category"),
    @NamedQuery(name = "CommandHistory.findByType", query = "SELECT c FROM CommandHistory c WHERE c.type = :type"),
    @NamedQuery(name = "CommandHistory.findByValue", query = "SELECT c FROM CommandHistory c WHERE c.value = :value"),
    @NamedQuery(name = "CommandHistory.findByStatus", query = "SELECT c FROM CommandHistory c WHERE c.status = :status"),
    @NamedQuery(name = "CommandHistory.findByMessage", query = "SELECT c FROM CommandHistory c WHERE c.message = :message"),
    @NamedQuery(name = "CommandHistory.findByDateTime", query = "SELECT c FROM CommandHistory c WHERE c.dateTime = :dateTime")})
public class CommandHistory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_COMMAND_HISTORY")
    private Integer idCommandHistory;
    @Size(max = 45)
    @Column(name = "CATEGORY")
    private String category;
    @Size(max = 45)
    @Column(name = "TYPE")
    private String type;
    @Size(max = 45)
    @Column(name = "VALUE")
    private String value;
    @Size(max = 45)
    @Column(name = "STATUS")
    private String status;
    @Size(max = 300)
    @Column(name = "MESSAGE")
    private String message;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTime;
    @JoinColumn(name = "ID_LPC", referencedColumnName = "ID_LPC")
    @ManyToOne
    private Lpc idLpc;
    @JoinColumn(name = "ID_SC", referencedColumnName = "ID_SC")
    @ManyToOne
    private Sc idSc;

    public CommandHistory() {
    }

    public CommandHistory(Integer idCommandHistory) {
        this.idCommandHistory = idCommandHistory;
    }

    public CommandHistory(Integer idCommandHistory, Date dateTime) {
        this.idCommandHistory = idCommandHistory;
        this.dateTime = dateTime;
    }

    public Integer getIdCommandHistory() {
        return idCommandHistory;
    }

    public void setIdCommandHistory(Integer idCommandHistory) {
        this.idCommandHistory = idCommandHistory;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Lpc getIdLpc() {
        return idLpc;
    }

    public void setIdLpc(Lpc idLpc) {
        this.idLpc = idLpc;
    }

    public Sc getIdSc() {
        return idSc;
    }

    public void setIdSc(Sc idSc) {
        this.idSc = idSc;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCommandHistory != null ? idCommandHistory.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CommandHistory)) {
            return false;
        }
        CommandHistory other = (CommandHistory) object;
        if ((this.idCommandHistory == null && other.idCommandHistory != null) || (this.idCommandHistory != null && !this.idCommandHistory.equals(other.idCommandHistory))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.iotll.vcs.entities.CommandHistory[ idCommandHistory=" + idCommandHistory + " ]";
    }
    
}
