/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yourasmusic.db;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author pedro
 */
@Entity
@Table(name = "ESTUDIO")
@NamedQueries({
    @NamedQuery(name = "Estudio.findAll", query = "SELECT e FROM Estudio e")})
public class Estudio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ESTUDIO_ID")
    private Integer estudioId;
    @Basic(optional = false)
    @Column(name = "MORADA")
    private String morada;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estudioId")
    private List<Reserva> reservaList;
    @JoinColumn(name = "DIRETOR_ID", referencedColumnName = "DIR_ESTUDIO_ID")
    @ManyToOne(optional = false)
    private DirEstudio diretorId;

    public Estudio() {
    }

    public Estudio(Integer estudioId) {
        this.estudioId = estudioId;
    }

    public Estudio(Integer estudioId, String morada) {
        this.estudioId = estudioId;
        this.morada = morada;
    }

    public Integer getEstudioId() {
        return estudioId;
    }

    public void setEstudioId(Integer estudioId) {
        this.estudioId = estudioId;
    }

    public String getMorada() {
        return morada;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }

    public List<Reserva> getReservaList() {
        return reservaList;
    }

    public void setReservaList(List<Reserva> reservaList) {
        this.reservaList = reservaList;
    }

    public DirEstudio getDiretorId() {
        return diretorId;
    }

    public void setDiretorId(DirEstudio diretorId) {
        this.diretorId = diretorId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (estudioId != null ? estudioId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Estudio)) {
            return false;
        }
        Estudio other = (Estudio) object;
        if ((this.estudioId == null && other.estudioId != null) || (this.estudioId != null && !this.estudioId.equals(other.estudioId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yourasmusic.db.Estudio[ estudioId=" + estudioId + " ]";
    }
    
}
