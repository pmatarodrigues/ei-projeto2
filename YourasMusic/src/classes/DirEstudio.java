package classes;
// Generated 13/jun/2018 18:32:28 by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;

/**
 * DirEstudio generated by hbm2java
 */
public class DirEstudio  implements java.io.Serializable {


     private int dirEstudioId;
     private String nome;
     private String contacto;
     private Set<Estudio> estudios = new HashSet<Estudio>(0);

    public DirEstudio() {
    }

    public DirEstudio(int dirEstudioId, String nome, String contacto) {
        this.dirEstudioId = dirEstudioId;
        this.nome = nome;
        this.contacto = contacto;
    }
	
    public DirEstudio(int dirEstudioId, String nome) {
        this.dirEstudioId = dirEstudioId;
        this.nome = nome;
    }
    public DirEstudio(int dirEstudioId, String nome, String contacto, Set<Estudio> estudios) {
       this.dirEstudioId = dirEstudioId;
       this.nome = nome;
       this.contacto = contacto;
       this.estudios = estudios;
    }
   
    public int getDirEstudioId() {
        return this.dirEstudioId;
    }
    
    public void setDirEstudioId(int dirEstudioId) {
        this.dirEstudioId = dirEstudioId;
    }
    public String getNome() {
        return this.nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getContacto() {
        return this.contacto;
    }
    
    public void setContacto(String contacto) {
        this.contacto = contacto;
    }
    public Set<Estudio> getEstudios() {
        return this.estudios;
    }
    
    public void setEstudios(Set<Estudio> estudios) {
        this.estudios = estudios;
    }




}


