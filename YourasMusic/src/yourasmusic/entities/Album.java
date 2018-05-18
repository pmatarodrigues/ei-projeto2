/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yourasmusic.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
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
@Table(name = "ALBUM")
@NamedQueries({
    @NamedQuery(name = "Album.findAll", query = "SELECT a FROM Album a")})
public class Album implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ALBUM_ID")
    private Integer albumId;
    @Basic(optional = false)
    @Column(name = "NOME")
    private String nome;
    @Basic(optional = false)
    @Lob
    @Column(name = "CAPA")
    private Serializable capa;
    @Column(name = "PRODUTOR")
    private String produtor;
    @Column(name = "LINK_ALBUM")
    private String linkAlbum;
    @Column(name = "ANO")
    private String ano;
    @OneToMany(mappedBy = "albumId")
    private List<Musica> musicaList;
    @JoinColumn(name = "ARTISTA_ID", referencedColumnName = "ARTISTA_ID")
    @ManyToOne
    private Artista artistaId;
    @JoinColumn(name = "EDITORA_ID", referencedColumnName = "EDITORA_ID")
    @ManyToOne
    private Editora editoraId;

    public Album() {
    }

    public Album(Integer albumId) {
        this.albumId = albumId;
    }

    public Album(Integer albumId, String nome, Serializable capa) {
        this.albumId = albumId;
        this.nome = nome;
        this.capa = capa;
    }

    public Integer getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Integer albumId) {
        this.albumId = albumId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Serializable getCapa() {
        return capa;
    }

    public void setCapa(Serializable capa) {
        this.capa = capa;
    }

    public String getProdutor() {
        return produtor;
    }

    public void setProdutor(String produtor) {
        this.produtor = produtor;
    }

    public String getLinkAlbum() {
        return linkAlbum;
    }

    public void setLinkAlbum(String linkAlbum) {
        this.linkAlbum = linkAlbum;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public List<Musica> getMusicaList() {
        return musicaList;
    }

    public void setMusicaList(List<Musica> musicaList) {
        this.musicaList = musicaList;
    }

    public Artista getArtistaId() {
        return artistaId;
    }

    public void setArtistaId(Artista artistaId) {
        this.artistaId = artistaId;
    }

    public Editora getEditoraId() {
        return editoraId;
    }

    public void setEditoraId(Editora editoraId) {
        this.editoraId = editoraId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (albumId != null ? albumId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Album)) {
            return false;
        }
        Album other = (Album) object;
        if ((this.albumId == null && other.albumId != null) || (this.albumId != null && !this.albumId.equals(other.albumId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yourasmusic.entities.Album[ albumId=" + albumId + " ]";
    }
    
}
