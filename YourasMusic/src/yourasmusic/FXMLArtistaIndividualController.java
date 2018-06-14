
package yourasmusic;

import classes.Album;
import classes.Artista;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;


public class FXMLArtistaIndividualController implements Initializable {

    @FXML Label nomeAlbum;
    @FXML Label anoAlbum;
    
    Artista artistaAtual;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
    void initData(Artista artistaClicado) {
        this.artistaAtual = artistaClicado;
        nomeAlbum.setText(artistaClicado.getNomeArtista());
        anoAlbum.setText(artistaClicado.getNacionalidade());
    }   
    
}
