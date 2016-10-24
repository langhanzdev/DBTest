/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.iotll.vcs.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author langhanz
 */
@Entity
@Table(name = "LPCS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Lpcs.findAll", query = "SELECT l FROM Lpcs l"),
    @NamedQuery(name = "Lpcs.findByIdLPC", query = "SELECT l FROM Lpcs l WHERE l.idLPC = :idLPC"),
    @NamedQuery(name = "Lpcs.findBySwicth", query = "SELECT l FROM Lpcs l WHERE l.swicth = :swicth"),
    @NamedQuery(name = "Lpcs.findByDimmer", query = "SELECT l FROM Lpcs l WHERE l.dimmer = :dimmer"),
    @NamedQuery(name = "Lpcs.findByTemperature", query = "SELECT l FROM Lpcs l WHERE l.temperature = :temperature"),
    @NamedQuery(name = "Lpcs.findByCurrent", query = "SELECT l FROM Lpcs l WHERE l.current = :current"),
    @NamedQuery(name = "Lpcs.findByLuminosity", query = "SELECT l FROM Lpcs l WHERE l.luminosity = :luminosity")})
public class Lpcs implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdLPC")
    private Integer idLPC;
    @Column(name = "Swicth")
    private Boolean swicth;
    @Column(name = "Dimmer")
    private Integer dimmer;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "Temperature")
    private Double temperature;
    @Column(name = "Current")
    private Double current;
    @Column(name = "Luminosity")
    private Integer luminosity;
    @OneToMany(mappedBy = "lpc")
    private List<CommandHist> commandHistList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lpc")
    private List<LpcsHist> lpcsHistList;
    @JoinColumn(name = "Address", referencedColumnName = "IdAddress")
    @ManyToOne
    private Addresses address;
    @JoinColumn(name = "SC", referencedColumnName = "IdSegmentController")
    @ManyToOne
    private Scs sc;

    public Lpcs() {
    }

    public Lpcs(Integer idLPC) {
        this.idLPC = idLPC;
    }

    public Integer getIdLPC() {
        return idLPC;
    }

    public void setIdLPC(Integer idLPC) {
        this.idLPC = idLPC;
    }

    public Boolean getSwicth() {
        return swicth;
    }

    public void setSwicth(Boolean swicth) {
        this.swicth = swicth;
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

    @XmlTransient
    public List<CommandHist> getCommandHistList() {
        return commandHistList;
    }

    public void setCommandHistList(List<CommandHist> commandHistList) {
        this.commandHistList = commandHistList;
    }

    @XmlTransient
    public List<LpcsHist> getLpcsHistList() {
        return lpcsHistList;
    }

    public void setLpcsHistList(List<LpcsHist> lpcsHistList) {
        this.lpcsHistList = lpcsHistList;
    }

    public Addresses getAddress() {
        return address;
    }

    public void setAddress(Addresses address) {
        this.address = address;
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
        hash += (idLPC != null ? idLPC.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Lpcs)) {
            return false;
        }
        Lpcs other = (Lpcs) object;
        if ((this.idLPC == null && other.idLPC != null) || (this.idLPC != null && !this.idLPC.equals(other.idLPC))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.iotll.vcs.entities.Lpcs[ idLPC=" + idLPC + " ]";
    }
    
}
