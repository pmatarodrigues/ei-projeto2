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
@Table(name = "DIR_ESTUDIO")
@NamedQueries({
    @NamedQuery(name = "DirEstudio.findAll", query = "SELECT d FROM DirEstudio d")})
public class DirEstudio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "DIR_ESTUDIO_ID")
    private Integer dirEstudioId;
    @Basic(optional = false)
    @Column(name = "NOME")
    private String nome;
    @Column(name = "CONTACTO")
    private String contacto;
    @JoinColumn(name = "DIR_ESTUDIO_ID", referencedColumnName = "UTILIZADOR_ID", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Utilizador utilizador;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "diretorId")
    private List<Estudio> estudioList;

    public DirEstudio() {
    }

    public DirEstudio(Integer dirEstudioId) {
        this.dirEstudioId = dirEstudioId;
    }

    public DirEstudio(Integer dirEstudioId, String nome) {
        this.dirEstudioId = dirEstudioId;
        this.nome = nome;
    }

    public Integer getDirEstudioId() {
        return dirEstudioId;
    }

    public void setDirEstudioId(Integer dirEstudioId) {
        this.dirEstudioId = dirEstudioId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public List<Estudio> getEstudioList() {
        return estudioList;
    }

    public void setEstudioList(List<Estudio> estudioList) {
        this.estudioList = estudioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dirEstudioId != null ? dirEstudioId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DirEstudio)) {
            return false;
        }
        DirEstudio other = (DirEstudio) object;
        if ((this.dirEstudioId == null && other.dirEstudioId != null) || (this.dirEstudioId != null && !this.dirEstudioId.equals(other.dirEstudioId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yourasmusic.db.DirEstudio[ dirEstudioId=" + dirEstudioId + " ]";
    }
    
}
