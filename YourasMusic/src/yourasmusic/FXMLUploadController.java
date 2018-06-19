/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yourasmusic;

import classes.Artista;
import classes.Estudio;
import classes.Musica;
import classes.Utilizador;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.sql.Blob;
import static java.sql.JDBCType.BLOB;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static java.sql.Types.BLOB;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
//import javazoom.jl.decoder.JavaLayerException;
import static oracle.jdbc.OracleTypes.BLOB;
import oracle.sql.BLOB;
import org.hibernate.Session;
import org.hibernate.type.Type;


/**
 * FXML Controller class
 *
 * @author HP
 */
public class FXMLUploadController implements Initializable {

    @FXML TextField nome;
    @FXML ComboBox genero;
    @FXML TextField endereco;
    
    Session session;
    Musica musica;
    Blob upload;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ArrayList<String> generos = new ArrayList<>();
        generos.add("Pop");
        generos.add("Rock");
        generos.add("Metal");
        generos.add("HipHop");
        generos.add("Indie");
        generos.add("Reggae");
        
        genero.getItems().addAll(generos);
    }    
    
    @FXML
    public void enviarUpload(ActionEvent event){                
        session = hibernate.HibernateUtil.getSessionFactory().openSession();     
        session.beginTransaction();
        //List<Artista> artistaUpload = session.createQuery("FROM Artista Where Artista_Id = " + IniciarSessaoController.userLogin.getUtilizadorId()).list();       
        musica = new Musica();
        
        musica.setUtilizador(IniciarSessaoController.userLogin);
        musica.setNome(nome.getText());
        musica.setGenero(genero.getValue().toString());
        musica.setAudio(upload);
        
        session.save(musica);
        session.getTransaction().commit();
        
        session.close();
    }
    
    
    @FXML
     public void uploadMusic(ActionEvent event) throws SQLException, FileNotFoundException{
        
        File myFile = null;
        FileInputStream fis = null;
        
        // filtro de ficheiros
        FileFilter filter = new FileNameExtensionFilter("MP3 Files", "mp3", "mpeg3");
        // file chooser + diretorio onde irá abrir
        JFileChooser chooser = new JFileChooser("%UserProfile%\\Desktop ");
        
        chooser.addChoosableFileFilter(filter);
        
        int returnVal = chooser.showOpenDialog(null);
        
        if(returnVal == JFileChooser.APPROVE_OPTION){
            
            myFile = chooser.getSelectedFile();
            endereco.setText(myFile.toString());  
            fis = new FileInputStream(myFile);
            
        }
        
        
        session = hibernate.HibernateUtil.getSessionFactory().openSession();
        Blob musicaAEnviar = session.getLobHelper().createBlob(fis, myFile.length());
        upload = musicaAEnviar;
                
        session.close();
                 
     }
}
