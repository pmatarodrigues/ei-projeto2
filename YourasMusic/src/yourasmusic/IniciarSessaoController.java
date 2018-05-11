
package yourasmusic;

import java.beans.Statement;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.eclipse.persistence.sessions.Session;
import yourasmusic.db.*;



public class IniciarSessaoController implements Initializable {
       
    
    
    @FXML
    private TextField txfUsername;
    @FXML
    private TextField txfPassword;
    
    EntityManagerFactory emf;
    EntityManager em;
     
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        emf = Persistence. createEntityManagerFactory("YourasMusicPU");
        em = emf.createEntityManager();
        
        
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
    
    @FXML
    public void iniciarSessao(ActionEvent event){
        
    }
    
    @FXML
    public void verificarDadosLogin(ActionEvent event) throws SQLException{
       
        ResultSet utilizadores = executarQuery("SELECT * FROM UTILIZADOR WHERE email= 'pedro'");
        if(utilizadores.next() != false){
            ResultSet password = executarQuery("SELECT * FROM UTILIZADOR WHERE email ='pedro' and password_u= '1234'");
            if(password != null){
                System.out.println("SUCESSO! \n");
            }
            else{
                System.out.println("PASSWORD ERRADA!");
            }
        }
        else{
            System.out.println("Username invalido!");
        }
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
        
        
        return result;
    }

    
    
}
