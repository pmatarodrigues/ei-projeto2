
package yourasmusic;

import classes.Album;
import classes.Artista;
import classes.Musica;
import classes.Reserva;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.TilePane;
import javafx.scene.text.TextAlignment;
import javazoom.jl.decoder.JavaLayerException;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;


public class FXMLArtistaIndividualController implements Initializable {

    @FXML Label nomeAlbum;
    @FXML Label anoAlbum;
    @FXML TilePane tileMusicaArtista;
    @FXML Label lblSemMusicas;
    
    Artista artistaAtual;  
    Session session;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
            //carregarMusicasArtista();
    }    
    
    void initData(Artista artistaClicado) {
        this.artistaAtual = artistaClicado;
        nomeAlbum.setText(artistaClicado.getNomeArtista());
        anoAlbum.setText(artistaClicado.getNacionalidade());
        carregarMusicasArtista();
    }   
        
    private void carregarMusicasArtista(){
        Boolean temMusicas = false;
        ToggleGroup group = new ToggleGroup();
        
        session = hibernate.HibernateUtil.getSessionFactory().openSession();

        Criteria cr = session.createCriteria(Musica.class);
        List<Musica> musicas = cr.list();                        
        
        lblSemMusicas.setVisible(false);
        
        for(Musica m : musicas){               
            if(artistaAtual.getArtistaId() == m.getUtilizador().getUtilizadorId()){
                System.out.println("Musica " + m.getNome().toString());
                ToggleButton btnMusicas = new ToggleButton();
                btnMusicas.setId("button_tile");
                btnMusicas.setMinSize(800, 50);
                btnMusicas.setMaxSize(800, 50);
                btnMusicas.setTextAlignment(TextAlignment.LEFT);
                btnMusicas.setText(m.getNome().toString());
                tileMusicaArtista.setAlignment(Pos.TOP_CENTER);
                tileMusicaArtista.setVgap(10);
                tileMusicaArtista.getChildren().add(btnMusicas);
                btnMusicas.setToggleGroup(group);
                
                btnMusicas.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {                                                                                   
                    YourasMusic.getMp().blb = m.getAudio();
                    try {
                        YourasMusic.getMp().Pause();
                        YourasMusic.getMp().Play();
                        //FXMLInicioRootController.musicPlaying.setText(m.getNome().toString());
                    } catch (JavaLayerException | IOException | SQLException ex) {
                        Logger.getLogger(FXMLMusicasController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                });                               
            temMusicas = true;
            }            
        }   
        if(!temMusicas){
            lblSemMusicas.setVisible(true);
        }        
        session.close();
    }
    
}
