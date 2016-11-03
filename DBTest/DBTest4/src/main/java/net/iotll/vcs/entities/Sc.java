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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author langhanz
 */
@Entity
@Table(name = "SC")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Sc.findAll", query = "SELECT s FROM Sc s"),
    @NamedQuery(name = "Sc.findByIdSc", query = "SELECT s FROM Sc s WHERE s.idSc = :idSc"),
    @NamedQuery(name = "Sc.findByIpSc", query = "SELECT s FROM Sc s WHERE s.ipSc = :ipSc")})
public class Sc implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_SC")
    private Integer idSc;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "IP_SC")
    private String ipSc;
    @OneToMany(mappedBy = "idSc")
    private List<CommandHistory> commandHistoryList;
    @JoinColumn(name = "ID_ADDRESS", referencedColumnName = "ID_ADDRESS")
    @ManyToOne
    private Address idAddress;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idSc")
    private List<Lpc> lpcList;

    public Sc() {
    }

    public Sc(Integer idSc) {
        this.idSc = idSc;
    }

    public Sc(Integer idSc, String ipSc) {
        this.idSc = idSc;
        this.ipSc = ipSc;
    }

    public Integer getIdSc() {
        return idSc;
    }

    public void setIdSc(Integer idSc) {
        this.idSc = idSc;
    }

    public String getIpSc() {
        return ipSc;
    }

    public void setIpSc(String ipSc) {
        this.ipSc = ipSc;
    }

    @XmlTransient
    public List<CommandHistory> getCommandHistoryList() {
        return commandHistoryList;
    }

    public void setCommandHistoryList(List<CommandHistory> commandHistoryList) {
        this.commandHistoryList = commandHistoryList;
    }

    public Address getIdAddress() {
        return idAddress;
    }

    public void setIdAddress(Address idAddress) {
        this.idAddress = idAddress;
    }

    @XmlTransient
    public List<Lpc> getLpcList() {
        return lpcList;
    }

    public void setLpcList(List<Lpc> lpcList) {
        this.lpcList = lpcList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idSc != null ? idSc.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sc)) {
            return false;
        }
        Sc other = (Sc) object;
        if ((this.idSc == null && other.idSc != null) || (this.idSc != null && !this.idSc.equals(other.idSc))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.iotll.vcs.entities.Sc[ idSc=" + idSc + " ]";
    }
    
}
