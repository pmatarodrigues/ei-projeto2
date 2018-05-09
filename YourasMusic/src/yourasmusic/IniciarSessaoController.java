
package yourasmusic;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.*;
import javafx.scene.control.Button;



public class IniciarSessaoController implements Initializable {
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }    
    
    @FXML
    public void abrirPaginaCriarConta(ActionEvent event) throws IOException{
        Parent iniciarSessaoParent = FXMLLoader.load(getClass().getResource("FXMLCriarConta.fxml"));
        Scene iniciarSessaoScene = new Scene(iniciarSessaoParent);
        
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.hide();
        stage.setScene(iniciarSessaoScene);
        stage.show();
    }
    
    
}
