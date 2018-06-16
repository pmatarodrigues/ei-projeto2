/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yourasmusic;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class FXMLInicioRootController implements Initializable {

    String tipoEditora = null;
    
    
    /**
     * Initializes the controller class.
     */
    @FXML
    private Pane paneEsquerda;
    @FXML private Button but_albuns;
    @FXML private Button but_artistas;
    @FXML private Button but_editoras;
    @FXML private Button but_estudios;
    @FXML private Button but_musicas;
    @FXML private Button but_upload;
    @FXML private Button but_registar_album;
    @FXML private Label lblUsername;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       tipoEditora = IniciarSessaoController.userLogin.getTipo();
              
       // --- --> 2 ESPAÇOS <-- DEPOIS DO NOME DO TIPO DE 'UTILIZADOR'
       if(IniciarSessaoController.userLogin.getTipo().equals((String)("E  "))){   
            // --- Botao de Registar Estudio
            // --- Botao desativado
            but_registar_album.setVisible(false);
            but_upload.setVisible(false);
       } else if ("S  ".equals(IniciarSessaoController.userLogin.getTipo().toString())){  
           // --- Botao de Registar Estudio
            but_registar_album.setMinHeight(60);
            but_registar_album.setText("Registar Estúdio");
            but_registar_album.setTextAlignment(TextAlignment.CENTER);
           // --- Botao de Reservas
            but_upload.setText("Reservas");
       }              
       
       String tipo = IniciarSessaoController.userLogin.getTipo();
       
       if(tipo.equals("A  ")){
        lblUsername.setText(IniciarSessaoController.artistaLogin.getNomeArtista().toString());
       } else if(tipo.equals("E  ")){
           lblUsername.setText(IniciarSessaoController.editoraLogin.getNome().toString());
       } else if(tipo.equals("S  ")){
           lblUsername.setText(IniciarSessaoController.dirEstudioLogin.getNome().toString());
       }
    }
    
    @FXML
    private void switchCenter(ActionEvent event){
        switch(((Button) event.getSource()).getId()){
            
            case "but_inicio":
                changeCenterPane("FXMLInicioPane.fxml");
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
                if(IniciarSessaoController.userLogin.getTipo().toString().equals("S  ")){
                    changeCenterPane("FXMLConsultarReservas.fxml");                    
                }
                else{
                    changeCenterPane("FXMLUpload.fxml");
                }
                break;
            case "but_registar_album":
                if(IniciarSessaoController.userLogin.getTipo().toString().equals("S  ")){
                    changeCenterPane("FXMLRegistarEstudio.fxml");
                }
                else{
                    changeCenterPane("FXMLRegistarAlbum.fxml");
                }
                break;
            default:
        }
        
    }

    private void changeCenterPane(String paneName) {
        try {
            System.out.println("Abriu o " + paneName);
            Pane newPane = FXMLLoader.load(getClass().getResource(paneName));
            YourasMusic.getROOT().setRight(newPane);
        } catch (IOException ex) {
            ex.getMessage();
        }
    }
    
    @FXML
    public void terminarSessao(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("FXMLInitialPane.fxml"));
        
        Pane musicPane = FXMLLoader.load(getClass().getResource("FXMLIniciarSessao.fxml"));
        ((BorderPane) root).setRight(musicPane);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.hide();
        YourasMusic.setROOT((BorderPane) root);
        stage.setScene(new Scene(root));
        stage.show();
    }
    
}
