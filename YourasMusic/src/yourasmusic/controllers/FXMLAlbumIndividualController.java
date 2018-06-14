
package yourasmusic.controllers;

import classes.Album;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class FXMLAlbumIndividualController implements Initializable {

    @FXML Label nomeAlbum;
    @FXML Label anoAlbum;
    
    Album albumClicado;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    void initData(Album albumClicado) {
        this.albumClicado = albumClicado;
        nomeAlbum.setText(albumClicado.getNome());
        anoAlbum.setText(albumClicado.getAno());
    }

    
}
