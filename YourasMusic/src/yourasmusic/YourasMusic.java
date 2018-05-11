
package yourasmusic;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public class YourasMusic extends Application {
    
    EntityManagerFactory emf;
    EntityManager em;
    
    @Override
    public void start(Stage stage) throws Exception {        
        Parent root = FXMLLoader.load(getClass().getResource("FXMLIniciarSessao.fxml"));
        
        emf = Persistence.createEntityManagerFactory("YourasMusicPU");
        em = emf.createEntityManager();        
        
        stage.setTitle("Youras Music");
        stage.setScene(new Scene(root, 1280, 800));
        stage.show();
               
    }

 
    
    public static void main(String[] args) {
        launch(args);
    }

    

}
