
package yourasmusic;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;


public class FXMLInicioPaneController implements Initializable {

    @FXML
    AnchorPane anchor;
    @FXML ListView listaGeneros;
    @FXML TilePane listaGenerosTile;
    @FXML Button btnGenero;
    
    IniciarSessaoController iniciarSessaoController = new IniciarSessaoController();
    
    ArrayList<String> generos = new ArrayList<>();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {                    
        
        generos.add("Pop");
        generos.add("Rock");
        generos.add("Metal");
        generos.add("HipHop");
        generos.add("Indie");
        generos.add("Reggae");

        carregarGenerosDeMusica();
    }    
    
    
    private void carregarGenerosDeMusica(){             
        for(String s : generos){
            Button newButton = new Button();
            newButton.setStyle("-fx-text-fill: #FFF; "
                               + "-fx-background-color: #272727; "
                               + "-fx-padding: 10px");
            newButton.setMaxSize(213, 160);
            newButton.setText(s);
            listaGenerosTile.getChildren().add(newButton);
        }
    }
    
}
