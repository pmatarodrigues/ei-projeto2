/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yourasmusic;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import static java.sql.JDBCType.BLOB;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static java.sql.Types.BLOB;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
//import javazoom.jl.decoder.JavaLayerException;
import static oracle.jdbc.OracleTypes.BLOB;
import oracle.sql.BLOB;
import yourasmusic.entities.Musica;
import yourasmusic.entities.Utilizador;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class FXMLUploadController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    EntityManager em;
    EntityManagerFactory emf;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        emf = Persistence.createEntityManagerFactory("YourasMusicPU");
        em = emf.createEntityManager();
    }    
    
    @FXML
     public void uploadMusic(ActionEvent event) throws SQLException, FileNotFoundException{
        
        File myFile = null;
        int fileLenght = 0;
        FileInputStream fis = null;
        BLOB mus = null;
        
        // filtro de ficheiros
        FileFilter filter = new FileNameExtensionFilter("MP3 Files", "mp3", "mpeg3");
        // file chooser + diretorio onde irá abrir
        JFileChooser chooser = new JFileChooser("%UserProfile%\\Desktop ");
        
        chooser.addChoosableFileFilter(filter);
        
        int returnVal = chooser.showOpenDialog(null);
        
        if(returnVal == JFileChooser.APPROVE_OPTION){
            
            myFile = chooser.getSelectedFile();
            fis = new FileInputStream(myFile);
            
        }
        
        /* INICIAR ENVIO PARA A BASE DE DADOS*/
        em.getTransaction().begin();
        java.sql.Connection con = em.unwrap(java.sql.Connection.class);
        PreparedStatement st = con.prepareStatement("INSERT INTO musica (nome, artista_id, audio) VALUES (?, ?, ?)");
        st.setString(1, "nome_musica");
        st.setInt(2, 56);
        st.setBinaryStream(3, fis, (int) myFile.length());
        st.executeQuery();
        
        em.getTransaction().commit();
        em.clear();
        
     
        
         
     }
}
