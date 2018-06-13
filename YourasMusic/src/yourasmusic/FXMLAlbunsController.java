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
import javafx.scene.layout.TilePane;
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
        
        List<Artista> albums = session.createCriteria(Artista.class).list();
        
        Query query = session.createQuery("SELECT NOME FROM ALBUM");
        Iterator ite = query.iterate();
        while(ite.hasNext()){
            Button btnAlbuns = new Button();
            btnAlbuns.setId("button_tile");
            btnAlbuns.setMinSize(200, 200);
            btnAlbuns.setText(query.getFirstResult().toString());
            tileAlbuns.getChildren().add(btnAlbuns);
        }
        
        /*
        for(Artista a : albums){
            Button btnAlbuns = new Button();
            btnAlbuns.setStyle("-fx-background-color: #272727;"
                             + "-fx-text-fill: #FFF;");
            btnAlbuns.setMinSize(200, 200);
            btnAlbuns.setText(a.getNomeArtista().toString());
            tileAlbuns.getChildren().add(btnAlbuns);
        }   
*/
        
        session.close();
    }
    
    
}
