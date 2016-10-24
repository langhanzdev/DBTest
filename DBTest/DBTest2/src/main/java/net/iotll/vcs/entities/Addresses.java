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
@Table(name = "ADDRESSES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Addresses.findAll", query = "SELECT a FROM Addresses a"),
    @NamedQuery(name = "Addresses.findByIdAddress", query = "SELECT a FROM Addresses a WHERE a.idAddress = :idAddress"),
    @NamedQuery(name = "Addresses.findByAddress", query = "SELECT a FROM Addresses a WHERE a.address = :address"),
    @NamedQuery(name = "Addresses.findByLatitude", query = "SELECT a FROM Addresses a WHERE a.latitude = :latitude"),
    @NamedQuery(name = "Addresses.findByLongitude", query = "SELECT a FROM Addresses a WHERE a.longitude = :longitude"),
    @NamedQuery(name = "Addresses.findByDescriptionLocation", query = "SELECT a FROM Addresses a WHERE a.descriptionLocation = :descriptionLocation")})
public class Addresses implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdAddress")
    private Integer idAddress;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "Address")
    private String address;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "Latitude")
    private Double latitude;
    @Column(name = "Longitude")
    private Double longitude;
    @Size(max = 150)
    @Column(name = "DescriptionLocation")
    private String descriptionLocation;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idAddress")
    private List<Scs> scsList;
    @OneToMany(mappedBy = "address")
    private List<Lpcs> lpcsList;

    public Addresses() {
    }

    public Addresses(Integer idAddress) {
        this.idAddress = idAddress;
    }

    public Addresses(Integer idAddress, String address) {
        this.idAddress = idAddress;
        this.address = address;
    }

    public Integer getIdAddress() {
        return idAddress;
    }

    public void setIdAddress(Integer idAddress) {
        this.idAddress = idAddress;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getDescriptionLocation() {
        return descriptionLocation;
    }

    public void setDescriptionLocation(String descriptionLocation) {
        this.descriptionLocation = descriptionLocation;
    }

    @XmlTransient
    public List<Scs> getScsList() {
        return scsList;
    }

    public void setScsList(List<Scs> scsList) {
        this.scsList = scsList;
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
        hash += (idAddress != null ? idAddress.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Addresses)) {
            return false;
        }
        Addresses other = (Addresses) object;
        if ((this.idAddress == null && other.idAddress != null) || (this.idAddress != null && !this.idAddress.equals(other.idAddress))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.iotll.vcs.entities.Addresses[ idAddress=" + idAddress + " ]";
    }
    
}
