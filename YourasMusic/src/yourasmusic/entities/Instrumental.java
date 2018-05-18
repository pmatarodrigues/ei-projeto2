/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yourasmusic.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author pedro
 */
@Entity
@Table(name = "INSTRUMENTAL")
@NamedQueries({
    @NamedQuery(name = "Instrumental.findAll", query = "SELECT i FROM Instrumental i")})
public class Instrumental implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "INSTRUMENTAL_ID")
    private Integer instrumentalId;
    @Basic(optional = false)
    @Lob
    @Column(name = "AUDIO")
    private Serializable audio;
    @Column(name = "COMPOSITOR")
    private String compositor;

    public Instrumental() {
    }

    public Instrumental(Integer instrumentalId) {
        this.instrumentalId = instrumentalId;
    }

    public Instrumental(Integer instrumentalId, Serializable audio) {
        this.instrumentalId = instrumentalId;
        this.audio = audio;
    }

    public Integer getInstrumentalId() {
        return instrumentalId;
    }

    public void setInstrumentalId(Integer instrumentalId) {
        this.instrumentalId = instrumentalId;
    }

    public Serializable getAudio() {
        return audio;
    }

    public void setAudio(Serializable audio) {
        this.audio = audio;
    }

    public String getCompositor() {
        return compositor;
    }

    public void setCompositor(String compositor) {
        this.compositor = compositor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (instrumentalId != null ? instrumentalId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Instrumental)) {
            return false;
        }
        Instrumental other = (Instrumental) object;
        if ((this.instrumentalId == null && other.instrumentalId != null) || (this.instrumentalId != null && !this.instrumentalId.equals(other.instrumentalId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yourasmusic.entities.Instrumental[ instrumentalId=" + instrumentalId + " ]";
    }
    
}
