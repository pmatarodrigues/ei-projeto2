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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author pedro
 */
@Entity
@Table(name = "LETRA")
@NamedQueries({
    @NamedQuery(name = "Letra.findAll", query = "SELECT l FROM Letra l")})
public class Letra implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "LETRA_ID")
    private Integer letraId;
    @Basic(optional = false)
    @Column(name = "LYRICS")
    private String lyrics;
    @Column(name = "AUTOR")
    private String autor;

    public Letra() {
    }

    public Letra(Integer letraId) {
        this.letraId = letraId;
    }

    public Letra(Integer letraId, String lyrics) {
        this.letraId = letraId;
        this.lyrics = lyrics;
    }

    public Integer getLetraId() {
        return letraId;
    }

    public void setLetraId(Integer letraId) {
        this.letraId = letraId;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (letraId != null ? letraId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Letra)) {
            return false;
        }
        Letra other = (Letra) object;
        if ((this.letraId == null && other.letraId != null) || (this.letraId != null && !this.letraId.equals(other.letraId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yourasmusic.entities.Letra[ letraId=" + letraId + " ]";
    }
    
}
