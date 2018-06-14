
package yourasmusic;

import classes.Album;
import classes.Artista;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;
import yourasmusic.YourasMusic;


public class FXMLArtistasController implements Initializable {

    @FXML TilePane tileArtistas;
    
    List<Artista> artistas;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarArtistas();
    }    
    
    
    private void carregarArtistas(){        
        org.hibernate.Session session = hibernate.HibernateUtil.getSessionFactory().openSession();
        
        this.artistas = session.createCriteria(Artista.class).list();
        
        for(Artista a : artistas){
            Button btnArtistas = new Button();
            
            btnArtistas.setId("button_tile");
            btnArtistas.setMinSize(200, 200);
            btnArtistas.setText(a.getNomeArtista().toString());
            
            tileArtistas.setHgap(20);
            tileArtistas.setVgap(20);
            tileArtistas.setAlignment(Pos.CENTER);
            tileArtistas.getChildren().add(btnArtistas);
            
            btnArtistas.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        buttonClicked(String.valueOf(a.getArtistaId()));
                    } catch (IOException ex) {
                        Logger.getLogger(FXMLArtistasController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        }                       
        session.close();
    }
    
    
    public void buttonClicked(String idArtista) throws IOException{
        System.out.println("CHEGOU");
        Artista artistaClicado = new Artista();
        // --- Iguala o album clicado a um novo album dentro da função
        for(Artista a : artistas){
            if(String.valueOf(a.getArtistaId()).equals(idArtista)){
                artistaClicado = a;
            }
        }
             
        // ---- PASSAR AS INFORMAÇÕES DO ALBUM PARA O FXML DE ALBUM INDIVIDUAL
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("FXMLArtistaIndividual.fxml"));
        
        // ---- ABRIR ALBUM INDIVIDUAL
        YourasMusic.getROOT().setRight(loader.load());
        
        FXMLArtistaIndividualController album = loader.getController();
        album.initData(artistaClicado);       
    }
    
}
