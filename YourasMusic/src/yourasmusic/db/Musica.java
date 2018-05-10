/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yourasmusic.db;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author pedro
 */
@Entity
@Table(name = "MUSICA")
@NamedQueries({
    @NamedQuery(name = "Musica.findAll", query = "SELECT m FROM Musica m")})
public class Musica implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "MUSICA_ID")
    private Integer musicaId;
    @Basic(optional = false)
    @Column(name = "NOME")
    private String nome;
    @Column(name = "GENERO")
    private String genero;
    @Column(name = "DURACAO")
    private Integer duracao;
    @Basic(optional = false)
    @Lob
    @Column(name = "AUDIO")
    private Serializable audio;
    @JoinColumn(name = "ALBUM_ID", referencedColumnName = "ALBUM_ID")
    @ManyToOne
    private Album albumId;
    @JoinColumn(name = "ARTISTA_ID", referencedColumnName = "UTILIZADOR_ID")
    @ManyToOne(optional = false)
    private Utilizador artistaId;

    public Musica() {
    }

    public Musica(Integer musicaId) {
        this.musicaId = musicaId;
    }

    public Musica(Integer musicaId, String nome, Serializable audio) {
        this.musicaId = musicaId;
        this.nome = nome;
        this.audio = audio;
    }

    public Integer getMusicaId() {
        return musicaId;
    }

    public void setMusicaId(Integer musicaId) {
        this.musicaId = musicaId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public Integer getDuracao() {
        return duracao;
    }

    public void setDuracao(Integer duracao) {
        this.duracao = duracao;
    }

    public Serializable getAudio() {
        return audio;
    }

    public void setAudio(Serializable audio) {
        this.audio = audio;
    }

    public Album getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Album albumId) {
        this.albumId = albumId;
    }

    public Utilizador getArtistaId() {
        return artistaId;
    }

    public void setArtistaId(Utilizador artistaId) {
        this.artistaId = artistaId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (musicaId != null ? musicaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Musica)) {
            return false;
        }
        Musica other = (Musica) object;
        if ((this.musicaId == null && other.musicaId != null) || (this.musicaId != null && !this.musicaId.equals(other.musicaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yourasmusic.db.Musica[ musicaId=" + musicaId + " ]";
    }
    
}
