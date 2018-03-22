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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author langhanz
 */
@Entity
@Table(name = "LPC_HISTORY")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LpcHistory.findAll", query = "SELECT l FROM LpcHistory l"),
    @NamedQuery(name = "LpcHistory.findByIdLpcHistory", query = "SELECT l FROM LpcHistory l WHERE l.idLpcHistory = :idLpcHistory"),
    @NamedQuery(name = "LpcHistory.findBySwitch1", query = "SELECT l FROM LpcHistory l WHERE l.switch1 = :switch1"),
    @NamedQuery(name = "LpcHistory.findByDimmer", query = "SELECT l FROM LpcHistory l WHERE l.dimmer = :dimmer"),
    @NamedQuery(name = "LpcHistory.findByTemperature", query = "SELECT l FROM LpcHistory l WHERE l.temperature = :temperature"),
    @NamedQuery(name = "LpcHistory.findByCurrent", query = "SELECT l FROM LpcHistory l WHERE l.current = :current"),
    @NamedQuery(name = "LpcHistory.findByLuminosity", query = "SELECT l FROM LpcHistory l WHERE l.luminosity = :luminosity"),
    @NamedQuery(name = "LpcHistory.findByDateTime", query = "SELECT l FROM LpcHistory l WHERE l.dateTime = :dateTime")})
public class LpcHistory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_LPC_HISTORY")
    private Integer idLpcHistory;
    @Column(name = "SWITCH")
    private Boolean switch1;
    @Column(name = "DIMMER")
    private Integer dimmer;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "TEMPERATURE")
    private Double temperature;
    @Column(name = "CURRENT")
    private Double current;
    @Column(name = "LUMINOSITY")
    private Integer luminosity;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTime;
    @JoinColumn(name = "ID_LPC", referencedColumnName = "ID_LPC")
    @ManyToOne(optional = false)
    private Lpc idLpc;

    public LpcHistory() {
    }

    public LpcHistory(Integer idLpcHistory) {
        this.idLpcHistory = idLpcHistory;
    }

    public LpcHistory(Integer idLpcHistory, Date dateTime) {
        this.idLpcHistory = idLpcHistory;
        this.dateTime = dateTime;
    }

    public Integer getIdLpcHistory() {
        return idLpcHistory;
    }

    public void setIdLpcHistory(Integer idLpcHistory) {
        this.idLpcHistory = idLpcHistory;
    }

    public Boolean getSwitch1() {
        return switch1;
    }

    public void setSwitch1(Boolean switch1) {
        this.switch1 = switch1;
    }

    public Integer getDimmer() {
        return dimmer;
    }

    public void setDimmer(Integer dimmer) {
        this.dimmer = dimmer;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getCurrent() {
        return current;
    }

    public void setCurrent(Double current) {
        this.current = current;
    }

    public Integer getLuminosity() {
        return luminosity;
    }

    public void setLuminosity(Integer luminosity) {
        this.luminosity = luminosity;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idLpcHistory != null ? idLpcHistory.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LpcHistory)) {
            return false;
        }
        LpcHistory other = (LpcHistory) object;
        if ((this.idLpcHistory == null && other.idLpcHistory != null) || (this.idLpcHistory != null && !this.idLpcHistory.equals(other.idLpcHistory))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.iotll.vcs.entities.LpcHistory[ idLpcHistory=" + idLpcHistory + " ]";
    }
    
}
