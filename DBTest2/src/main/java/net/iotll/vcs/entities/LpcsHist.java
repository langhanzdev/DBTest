/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.iotll.vcs.entities;

import java.io.Serializable;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author langhanz
 */
@Entity
@Table(name = "LPCS_HIST")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LpcsHist.findAll", query = "SELECT l FROM LpcsHist l"),
    @NamedQuery(name = "LpcsHist.findByIdLpcHist", query = "SELECT l FROM LpcsHist l WHERE l.idLpcHist = :idLpcHist"),
    @NamedQuery(name = "LpcsHist.findBySwitch1", query = "SELECT l FROM LpcsHist l WHERE l.switch1 = :switch1"),
    @NamedQuery(name = "LpcsHist.findByDimmer", query = "SELECT l FROM LpcsHist l WHERE l.dimmer = :dimmer"),
    @NamedQuery(name = "LpcsHist.findByTemperature", query = "SELECT l FROM LpcsHist l WHERE l.temperature = :temperature"),
    @NamedQuery(name = "LpcsHist.findByCurrent", query = "SELECT l FROM LpcsHist l WHERE l.current = :current"),
    @NamedQuery(name = "LpcsHist.findByLuminosity", query = "SELECT l FROM LpcsHist l WHERE l.luminosity = :luminosity")})
public class LpcsHist implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdLpcHist")
    private Integer idLpcHist;
    @Column(name = "Switch")
    private Boolean switch1;
    @Column(name = "Dimmer")
    private Integer dimmer;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "Temperature")
    private Double temperature;
    @Column(name = "Current")
    private Double current;
    @Size(max = 45)
    @Column(name = "Luminosity")
    private String luminosity;
    @JoinColumn(name = "LPC", referencedColumnName = "IdLPC")
    @ManyToOne(optional = false)
    private Lpcs lpc;

    public LpcsHist() {
    }

    public LpcsHist(Integer idLpcHist) {
        this.idLpcHist = idLpcHist;
    }

    public Integer getIdLpcHist() {
        return idLpcHist;
    }

    public void setIdLpcHist(Integer idLpcHist) {
        this.idLpcHist = idLpcHist;
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

    public String getLuminosity() {
        return luminosity;
    }

    public void setLuminosity(String luminosity) {
        this.luminosity = luminosity;
    }

    public Lpcs getLpc() {
        return lpc;
    }

    public void setLpc(Lpcs lpc) {
        this.lpc = lpc;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idLpcHist != null ? idLpcHist.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LpcsHist)) {
            return false;
        }
        LpcsHist other = (LpcsHist) object;
        if ((this.idLpcHist == null && other.idLpcHist != null) || (this.idLpcHist != null && !this.idLpcHist.equals(other.idLpcHist))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.iotll.vcs.entities.LpcsHist[ idLpcHist=" + idLpcHist + " ]";
    }
    
}
