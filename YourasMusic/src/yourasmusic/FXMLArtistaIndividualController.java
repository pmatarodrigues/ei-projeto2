
package yourasmusic;

import classes.Album;
import classes.Artista;
import classes.Musica;
import classes.Reserva;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.TilePane;
import javafx.scene.text.TextAlignment;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;


public class FXMLArtistaIndividualController implements Initializable {

    @FXML Label nomeAlbum;
    @FXML Label anoAlbum;
    @FXML TilePane tileMusicaArtista;
    
    Artista artistaAtual;  

    Session session;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
            carregarMusicasArtista();
    }    
    
    void initData(Artista artistaClicado) {
        this.artistaAtual = artistaClicado;
        nomeAlbum.setText(artistaClicado.getNomeArtista());
        anoAlbum.setText(artistaClicado.getNacionalidade());
    }   
    
    
    private void carregarMusicasArtista(){
        session = hibernate.HibernateUtil.getSessionFactory().openSession();

        Criteria cr = session.createCriteria(Musica.class);
        List<Musica> musicas = cr.list();
        
        Criterion mus = Restrictions.eq("utilizador", artistaAtual);
 
        LogicalExpression musi = Restrictions.and(mus, mus);
        cr.add(musi);
                
        for(Musica m : musicas){
            Button btnMusicas = new Button();
            btnMusicas.setId("button_tile");
            btnMusicas.setMinSize(800, 50);
            btnMusicas.setMaxSize(800, 50);
            btnMusicas.setTextAlignment(TextAlignment.LEFT);
            btnMusicas.setText(m.getNome().toString());
            tileMusicaArtista.setAlignment(Pos.TOP_CENTER);
            tileMusicaArtista.setVgap(10);
            tileMusicaArtista.getChildren().add(btnMusicas);
            
        }   
        
        session.close();
    }
    
}
