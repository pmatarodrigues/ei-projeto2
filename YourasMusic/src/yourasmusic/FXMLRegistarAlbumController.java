
package yourasmusic;

import classes.Album;
import classes.Artista;
import classes.DirEstudio;
import classes.Editora;
import classes.Musica;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.hibernate.Session;

public class FXMLRegistarAlbumController implements Initializable {

    @FXML TextField txfNomeAlbum;
    @FXML ComboBox cmbNomeEditora;
    @FXML TextField txfLink;
    @FXML TextField txfAno;
    @FXML TextField txfEndereco;
    
    Session session;
    Album album;
    Blob capa;
    
    List<Editora> editoras;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        session = hibernate.HibernateUtil.getSessionFactory().openSession();     
        editoras = session.createCriteria(Editora.class).list(); 
        
        session = hibernate.HibernateUtil.getSessionFactory().openSession();                    
        
        for(Editora e : editoras){
            cmbNomeEditora.getItems().add(e.getNome());
        }
        
        session.close();
    }   
    
    @FXML
    public void enviarUpload(ActionEvent event){    
        Editora editora = null;
        
        for(Editora e : editoras){
            if(e.getNome().equals(cmbNomeEditora.getValue().toString())){
                editora = e;
            }
        }
        
        session = hibernate.HibernateUtil.getSessionFactory().openSession();                    


        session.beginTransaction();
        album = new Album();
        album.setNome(txfNomeAlbum.getText());
        album.setArtista(IniciarSessaoController.artistaLogin);
        album.setCapa(capa);
        album.setEditora(editora);
        album.setLinkAlbum(txfLink.getText());
        album.setAno(txfAno.getText());

        session.save(album);
        session.getTransaction().commit();
        
        session.close();
    }
            
    @FXML
     public void uploadCapa(ActionEvent event) throws SQLException, FileNotFoundException{
        
        File myFile = null;
        FileInputStream fis = null;
        
        // filtro de ficheiros
        FileFilter filter = new FileNameExtensionFilter("jpeg", "png", "jpg");
        // file chooser + diretorio onde irá abrir
        JFileChooser chooser = new JFileChooser("%UserProfile%\\Desktop ");
        
        chooser.addChoosableFileFilter(filter);
        
        int returnVal = chooser.showOpenDialog(null);
        
        if(returnVal == JFileChooser.APPROVE_OPTION){
            
            myFile = chooser.getSelectedFile();
            txfEndereco.setText(myFile.toString());  
            fis = new FileInputStream(myFile);
            
        }
        
        
        session = hibernate.HibernateUtil.getSessionFactory().openSession();
        Blob capaAEnviar = session.getLobHelper().createBlob(fis, myFile.length());
        capa = capaAEnviar;
        
        
        session.close();
             
     }
    
}
