
package yourasmusic;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.application.Platform;
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
    private static BorderPane ROOT;
    
    @Override
    public void start(Stage stage) throws Exception {        
        Parent root = FXMLLoader.load(getClass().getResource("FXMLInitialPane.fxml"));
        
        Pane iniciarSessaoPane = FXMLLoader.load(getClass().getResource("FXMLIniciarSessao.fxml"));
        ((BorderPane) root).setCenter(iniciarSessaoPane);
        
        YourasMusic.ROOT = (BorderPane) root;     
        
        stage.setTitle("Youras Music");
        stage.setScene(new Scene(root));
        stage.show();
        
        stage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
               
    }

    public static BorderPane getROOT() {
        return ROOT;
    }

    public static void setROOT(BorderPane ROOT) {
        YourasMusic.ROOT = ROOT;
    }
    
        
    public static void main(String[] args) {
        launch(args);
    }

    //-------------- RECEBE STRING COM O QUERY A EXECUTAR
    public ResultSet executarQuery(String queryRecebido) throws SQLException{
        ResultSet result;
        String query = queryRecebido;
        
        em.getTransaction().begin();
        //---- ASSOCIAR A BASE DE DADOS À VARIAVEL 'CON'
            java.sql.Connection con = em.unwrap(java.sql.Connection.class);
            PreparedStatement st = con.prepareStatement(query);
        //---- EXECUTAR QUERY
            result = st.executeQuery();
        em.getTransaction().commit();
        em.clear();
        return result;
    }

}
