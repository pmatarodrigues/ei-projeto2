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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import yourasmusic.entities.Utilizador;

/**
 * FXML Controller class
 *
 * @author pedro
 */
public class FXMLCriarContaArtistaController implements Initializable {

    @FXML
    TextField txfNomeCompleto;
    @FXML
    TextField txfNomeArtista;
    @FXML
    DatePicker dtpDataNascimento;
    @FXML
    TextField txfNacionalidade;
    @FXML
    TextField txfContacto;
    
    int idUser;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }   
    
    
    @FXML
    public void abrirPaginaIniciarSessao(ActionEvent event) throws IOException{
        Pane paneCriarConta = FXMLLoader.load(getClass().getResource("FXMLIniciarSessao.fxml"));
        YourasMusic.getROOT().setCenter(paneCriarConta);
        
        System.out.println(idUser);
    }
    
    public void initData(Utilizador user){
        idUser = user.getUtilizadorId();
    }
    
}
