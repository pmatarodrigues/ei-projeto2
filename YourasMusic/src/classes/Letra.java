package classes;
// Generated 13/jun/2018 18:32:28 by Hibernate Tools 4.3.1



/**
 * Letra generated by hbm2java
 */
public class Letra  implements java.io.Serializable {


     private int letraId;
     private String lyrics;
     private String autor;

    public Letra() {
    }

	
    public Letra(int letraId, String lyrics) {
        this.letraId = letraId;
        this.lyrics = lyrics;
    }
    public Letra(int letraId, String lyrics, String autor) {
       this.letraId = letraId;
       this.lyrics = lyrics;
       this.autor = autor;
    }
   
    public int getLetraId() {
        return this.letraId;
    }
    
    public void setLetraId(int letraId) {
        this.letraId = letraId;
    }
    public String getLyrics() {
        return this.lyrics;
    }
    
    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }
    public String getAutor() {
        return this.autor;
    }
    
    public void setAutor(String autor) {
        this.autor = autor;
    }




}

