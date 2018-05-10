
package yourasmusic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;



public class YourasMusic extends Application {
    
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLIniciarSessao.fxml"));
                
        stage.setTitle("Youras Music");
        stage.setScene(new Scene(root, 1280, 800));
        stage.show();
               
    }

 
    
    public static void main(String[] args) {
        launch(args);
    }
    

}
