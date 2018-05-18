/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yourasmusic.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author pedro
 */
@Entity
@Table(name = "RESERVA")
@NamedQueries({
    @NamedQuery(name = "Reserva.findAll", query = "SELECT r FROM Reserva r")})
public class Reserva implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "RESERVA_ID")
    private Integer reservaId;
    @Basic(optional = false)
    @Column(name = "DATA_RESERVA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataReserva;
    @Basic(optional = false)
    @Column(name = "ISCONFIRMED")
    private Character isconfirmed;
    @Basic(optional = false)
    @Column(name = "ISACTIVE")
    private Character isactive;
    @JoinColumn(name = "ESTUDIO_ID", referencedColumnName = "ESTUDIO_ID")
    @ManyToOne
    private Estudio estudioId;
    @JoinColumn(name = "REQUERENTE_ID", referencedColumnName = "UTILIZADOR_ID")
    @ManyToOne
    private Utilizador requerenteId;

    public Reserva() {
    }

    public Reserva(Integer reservaId) {
        this.reservaId = reservaId;
    }

    public Reserva(Integer reservaId, Date dataReserva, Character isconfirmed, Character isactive) {
        this.reservaId = reservaId;
        this.dataReserva = dataReserva;
        this.isconfirmed = isconfirmed;
        this.isactive = isactive;
    }

    public Integer getReservaId() {
        return reservaId;
    }

    public void setReservaId(Integer reservaId) {
        this.reservaId = reservaId;
    }

    public Date getDataReserva() {
        return dataReserva;
    }

    public void setDataReserva(Date dataReserva) {
        this.dataReserva = dataReserva;
    }

    public Character getIsconfirmed() {
        return isconfirmed;
    }

    public void setIsconfirmed(Character isconfirmed) {
        this.isconfirmed = isconfirmed;
    }

    public Character getIsactive() {
        return isactive;
    }

    public void setIsactive(Character isactive) {
        this.isactive = isactive;
    }

    public Estudio getEstudioId() {
        return estudioId;
    }

    public void setEstudioId(Estudio estudioId) {
        this.estudioId = estudioId;
    }

    public Utilizador getRequerenteId() {
        return requerenteId;
    }

    public void setRequerenteId(Utilizador requerenteId) {
        this.requerenteId = requerenteId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reservaId != null ? reservaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Reserva)) {
            return false;
        }
        Reserva other = (Reserva) object;
        if ((this.reservaId == null && other.reservaId != null) || (this.reservaId != null && !this.reservaId.equals(other.reservaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yourasmusic.entities.Reserva[ reservaId=" + reservaId + " ]";
    }
    
}
