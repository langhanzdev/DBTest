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
@Table(name = "LPC")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Lpc.findAll", query = "SELECT l FROM Lpc l"),
    @NamedQuery(name = "Lpc.findByIdLpc", query = "SELECT l FROM Lpc l WHERE l.idLpc = :idLpc"),
    @NamedQuery(name = "Lpc.findBySwitch1", query = "SELECT l FROM Lpc l WHERE l.switch1 = :switch1"),
    @NamedQuery(name = "Lpc.findByDimmer", query = "SELECT l FROM Lpc l WHERE l.dimmer = :dimmer"),
    @NamedQuery(name = "Lpc.findByTemperature", query = "SELECT l FROM Lpc l WHERE l.temperature = :temperature"),
    @NamedQuery(name = "Lpc.findByCurrent", query = "SELECT l FROM Lpc l WHERE l.current = :current"),
    @NamedQuery(name = "Lpc.findByLuminosity", query = "SELECT l FROM Lpc l WHERE l.luminosity = :luminosity")})
public class Lpc implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_LPC")
    private Integer idLpc;
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
    @OneToMany(mappedBy = "idLpc")
    private List<CommandHistory> commandHistoryList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idLpc")
    private List<LpcHistory> lpcHistoryList;
    @JoinColumn(name = "ID_ADDRESS", referencedColumnName = "ID_ADDRESS")
    @ManyToOne
    private Address idAddress;
    @JoinColumn(name = "ID_SC", referencedColumnName = "ID_SC")
    @ManyToOne(optional = false)
    private Sc idSc;

    public Lpc() {
    }

    public Lpc(Integer idLpc) {
        this.idLpc = idLpc;
    }

    public Integer getIdLpc() {
        return idLpc;
    }

    public void setIdLpc(Integer idLpc) {
        this.idLpc = idLpc;
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

    @XmlTransient
    public List<CommandHistory> getCommandHistoryList() {
        return commandHistoryList;
    }

    public void setCommandHistoryList(List<CommandHistory> commandHistoryList) {
        this.commandHistoryList = commandHistoryList;
    }

    @XmlTransient
    public List<LpcHistory> getLpcHistoryList() {
        return lpcHistoryList;
    }

    public void setLpcHistoryList(List<LpcHistory> lpcHistoryList) {
        this.lpcHistoryList = lpcHistoryList;
    }

    public Address getIdAddress() {
        return idAddress;
    }

    public void setIdAddress(Address idAddress) {
        this.idAddress = idAddress;
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
        hash += (idLpc != null ? idLpc.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Lpc)) {
            return false;
        }
        Lpc other = (Lpc) object;
        if ((this.idLpc == null && other.idLpc != null) || (this.idLpc != null && !this.idLpc.equals(other.idLpc))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.iotll.vcs.entities.Lpc[ idLpc=" + idLpc + " ]";
    }
    
}
