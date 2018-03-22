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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author langhanz
 */
@Entity
@Table(name = "COMMAND_HIST")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CommandHist.findAll", query = "SELECT c FROM CommandHist c"),
    @NamedQuery(name = "CommandHist.findByIdCommandHist", query = "SELECT c FROM CommandHist c WHERE c.idCommandHist = :idCommandHist"),
    @NamedQuery(name = "CommandHist.findByCategory", query = "SELECT c FROM CommandHist c WHERE c.category = :category"),
    @NamedQuery(name = "CommandHist.findByType", query = "SELECT c FROM CommandHist c WHERE c.type = :type"),
    @NamedQuery(name = "CommandHist.findByValue", query = "SELECT c FROM CommandHist c WHERE c.value = :value"),
    @NamedQuery(name = "CommandHist.findByStatus", query = "SELECT c FROM CommandHist c WHERE c.status = :status"),
    @NamedQuery(name = "CommandHist.findByMessage", query = "SELECT c FROM CommandHist c WHERE c.message = :message"),
    @NamedQuery(name = "CommandHist.findByDateTime", query = "SELECT c FROM CommandHist c WHERE c.dateTime = :dateTime")})
public class CommandHist implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdCommandHist")
    private Integer idCommandHist;
    @Size(max = 45)
    @Column(name = "Category")
    private String category;
    @Size(max = 45)
    @Column(name = "Type")
    private String type;
    @Size(max = 45)
    @Column(name = "Value")
    private String value;
    @Size(max = 45)
    @Column(name = "Status")
    private String status;
    @Size(max = 200)
    @Column(name = "Message")
    private String message;
    @Column(name = "DateTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTime;
    @JoinColumn(name = "LPC", referencedColumnName = "IdLPC")
    @ManyToOne
    private Lpcs lpc;
    @JoinColumn(name = "SC", referencedColumnName = "IdSegmentController")
    @ManyToOne
    private Scs sc;

    public CommandHist() {
    }

    public CommandHist(Integer idCommandHist) {
        this.idCommandHist = idCommandHist;
    }

    public Integer getIdCommandHist() {
        return idCommandHist;
    }

    public void setIdCommandHist(Integer idCommandHist) {
        this.idCommandHist = idCommandHist;
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

    public Lpcs getLpc() {
        return lpc;
    }

    public void setLpc(Lpcs lpc) {
        this.lpc = lpc;
    }

    public Scs getSc() {
        return sc;
    }

    public void setSc(Scs sc) {
        this.sc = sc;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCommandHist != null ? idCommandHist.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CommandHist)) {
            return false;
        }
        CommandHist other = (CommandHist) object;
        if ((this.idCommandHist == null && other.idCommandHist != null) || (this.idCommandHist != null && !this.idCommandHist.equals(other.idCommandHist))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.iotll.vcs.entities.CommandHist[ idCommandHist=" + idCommandHist + " ]";
    }
    
}
