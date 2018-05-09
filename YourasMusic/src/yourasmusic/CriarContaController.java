
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
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;



public class CriarContaController implements Initializable {
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
    
    
    @FXML
    public void abrirPaginaIniciarSessao(ActionEvent event) throws IOException{
        Parent criarContaParent = FXMLLoader.load(getClass().getResource("FXMLIniciarSessao.fxml"));
        Scene criarContaScene = new Scene(criarContaParent);
        
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.hide();
        stage.setScene(criarContaScene);
        stage.show();
    }
    
}