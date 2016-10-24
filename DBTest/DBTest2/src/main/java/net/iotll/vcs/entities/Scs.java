/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.iotll.vcs.entities;

import java.io.Serializable;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author langhanz
 */
@Entity
@Table(name = "SCS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Scs.findAll", query = "SELECT s FROM Scs s"),
    @NamedQuery(name = "Scs.findByIdSegmentController", query = "SELECT s FROM Scs s WHERE s.idSegmentController = :idSegmentController"),
    @NamedQuery(name = "Scs.findByIpSegmentController", query = "SELECT s FROM Scs s WHERE s.ipSegmentController = :ipSegmentController")})
public class Scs implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdSegmentController")
    private Integer idSegmentController;
    @Size(max = 45)
    @Column(name = "IpSegmentController")
    private String ipSegmentController;
    @OneToMany(mappedBy = "sc")
    private List<CommandHist> commandHistList;
    @JoinColumn(name = "IdAddress", referencedColumnName = "IdAddress")
    @ManyToOne(optional = false)
    private Addresses idAddress;
    @OneToMany(mappedBy = "sc")
    private List<Lpcs> lpcsList;

    public Scs() {
    }

    public Scs(Integer idSegmentController) {
        this.idSegmentController = idSegmentController;
    }

    public Integer getIdSegmentController() {
        return idSegmentController;
    }

    public void setIdSegmentController(Integer idSegmentController) {
        this.idSegmentController = idSegmentController;
    }

    public String getIpSegmentController() {
        return ipSegmentController;
    }

    public void setIpSegmentController(String ipSegmentController) {
        this.ipSegmentController = ipSegmentController;
    }

    @XmlTransient
    public List<CommandHist> getCommandHistList() {
        return commandHistList;
    }

    public void setCommandHistList(List<CommandHist> commandHistList) {
        this.commandHistList = commandHistList;
    }

    public Addresses getIdAddress() {
        return idAddress;
    }

    public void setIdAddress(Addresses idAddress) {
        this.idAddress = idAddress;
    }

    @XmlTransient
    public List<Lpcs> getLpcsList() {
        return lpcsList;
    }

    public void setLpcsList(List<Lpcs> lpcsList) {
        this.lpcsList = lpcsList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idSegmentController != null ? idSegmentController.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Scs)) {
            return false;
        }
        Scs other = (Scs) object;
        if ((this.idSegmentController == null && other.idSegmentController != null) || (this.idSegmentController != null && !this.idSegmentController.equals(other.idSegmentController))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.iotll.vcs.entities.Scs[ idSegmentController=" + idSegmentController + " ]";
    }
    
}
