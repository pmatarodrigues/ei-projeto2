package yourasmusic;

import classes.Album;
import classes.Artista;
import classes.Utilizador;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import org.hibernate.Criteria;
import org.hibernate.Query;


public class FXMLAlbunsController implements Initializable {
    
    
    @FXML TilePane tileAlbuns;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {        
        carregarAlbums();
    }   
    
    private void carregarAlbums(){
        
        org.hibernate.Session session = hibernate.HibernateUtil.getSessionFactory().openSession();
        
        List<Album> albums = session.createCriteria(Album.class).list();
        
        for(Album a : albums){
            Button btnAlbuns = new Button();
            VBox vbox = new VBox();
            String nomeAlbum = a.getNome().toString();
            String ano = a.getAno().toString();
            
            btnAlbuns.setId("button_tile");
            btnAlbuns.setMinSize(200, 200);
            btnAlbuns.setMaxSize(200, 200);
            
            btnAlbuns.setText(nomeAlbum + "\n" + ano);
            tileAlbuns.getChildren().add(btnAlbuns);
        }
       
        session.close();
    }
    
    
}
