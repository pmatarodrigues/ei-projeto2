/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yourasmusic;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class FXMLMusicPaneController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private Pane paneEsquerda;
    
  
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
    }
    
    @FXML
    private void switchCenter(ActionEvent event){
        // Load the new pane
        // menuitem people -> document.fxml
        // menuItem 
        switch(((Button) event.getSource()).getId()){
            
            case "but_inicio":
                changeCenterPane("FXMLMusic.fxml");
                break;
            case "but_albuns":
                changeCenterPane("FXMLAlbuns.fxml");
                break;
            case "but_artistas":
                changeCenterPane("FXMLArtistas.fxml");
                break;
            case "but_editoras":
                changeCenterPane("FXMLEditora.fxml");
                break;
            case "but_estudios":
                changeCenterPane("FXMLEstudio.fxml");
                break;
            case "but_musicas":
                changeCenterPane("FXMLMusicas.fxml");
                break;
            case "but_upload":
                changeCenterPane("FXMLUpload.fxml");
                break;
            default:
        }
        
    }

    private void changeCenterPane(String paneName) {
         
        try {
            Pane newPane = FXMLLoader.load(getClass().getResource(paneName));
            YourasMusic.getROOT().setRight(newPane);
        } catch (IOException ex) {
        }
       // ((BorderPane) root).setCenter(peoplePane);
    }
    
}
