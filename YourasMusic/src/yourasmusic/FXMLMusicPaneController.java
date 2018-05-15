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
    
    @FXML
    private Label albuns;
    
    @FXML
    private Button teste;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
    }    
    
    @FXML
    public void abrirAlbuns(ActionEvent event) throws IOException{
            Pane newPane = FXMLLoader.load(getClass().getResource("FXMLAlbuns.fxml"));
            YourasMusic.getROOT().setRight(newPane);
    }
    
}
