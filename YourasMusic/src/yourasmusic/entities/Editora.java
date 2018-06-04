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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author pedro
 */
@Entity
@Table(name = "EDITORA")
@NamedQueries({
    @NamedQuery(name = "Editora.findAll", query = "SELECT e FROM Editora e")})
public class Editora implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "EDITORA_ID")
    private Integer editoraId;
    @Basic(optional = false)
    @Column(name = "NOME")
    private String nome;
    @Basic(optional = false)
    @Column(name = "MORADA")
    private String morada;
    @Column(name = "CONTACTO")
    private String contacto;
    @JoinColumn(name = "EDITORA_ID", referencedColumnName = "UTILIZADOR_ID", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Utilizador utilizador;
    @OneToMany(mappedBy = "editoraId")
    private List<Album> albumList;

    public Editora() {
    }

    public Editora(Integer editoraId) {
        this.editoraId = editoraId;
    }

    public Editora(Integer editoraId, String nome, String morada) {
        this.editoraId = editoraId;
        this.nome = nome;
        this.morada = morada;
    }

    public Editora(Integer editoraId, String nome, String morada, String contacto) {
        this.editoraId = editoraId;
        this.nome = nome;
        this.morada = morada;
        this.contacto = contacto;
    }
    
    

    public Integer getEditoraId() {
        return editoraId;
    }

    public void setEditoraId(Integer editoraId) {
        this.editoraId = editoraId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMorada() {
        return morada;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public Utilizador getUtilizador() {
        return utilizador;
    }

    public void setUtilizador(Utilizador utilizador) {
        this.utilizador = utilizador;
    }

    public List<Album> getAlbumList() {
        return albumList;
    }

    public void setAlbumList(List<Album> albumList) {
        this.albumList = albumList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (editoraId != null ? editoraId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Editora)) {
            return false;
        }
        Editora other = (Editora) object;
        if ((this.editoraId == null && other.editoraId != null) || (this.editoraId != null && !this.editoraId.equals(other.editoraId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yourasmusic.entities.Editora[ editoraId=" + editoraId + " ]";
    }
    
}
