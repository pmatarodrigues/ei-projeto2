
package yourasmusic;

import classes.Artista;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;


public class FXMLArtistasController implements Initializable {

    @FXML TilePane tileArtistas;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarArtistas();
    }    
    
    
    private void carregarArtistas(){        
        org.hibernate.Session session = hibernate.HibernateUtil.getSessionFactory().openSession();
        
        List<Artista> artistas = session.createCriteria(Artista.class).list();
        
        for(Artista a : artistas){
            Button btnArtistas = new Button();
            btnArtistas.setId("button_tile");
            btnArtistas.setMinSize(200, 200);
            btnArtistas.setText(a.getNomeArtista().toString());
            tileArtistas.getChildren().add(btnArtistas);
        }   
        
        session.close();
    }
    
}
