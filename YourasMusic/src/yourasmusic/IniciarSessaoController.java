
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
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
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
    public void iniciarSessao(ActionEvent event) throws SQLException, IOException{
        int verifica;
        verifica = verificarDadosLogin(event);
        
        if(verifica==0){
            Parent root = FXMLLoader.load(getClass().getResource("FXMLMusicPane.fxml"));
            Pane musicPane = FXMLLoader.load(getClass().getResource("FXMLMusic.fxml"));
            ((BorderPane) root).setRight(musicPane);
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.hide();
            YourasMusic.setROOT((BorderPane) root);
            stage.setScene(new Scene(root));
            stage.show();
        }
    }
    
    @FXML
    public int verificarDadosLogin(ActionEvent event) throws SQLException{
        
        ResultSet utilizadores = executarQuery("SELECT * FROM UTILIZADOR WHERE email= '" + txfUsername.getText() + "'");
        if(utilizadores.next()){
            ResultSet password = executarQuery("SELECT * FROM UTILIZADOR WHERE email = '" + txfUsername.getText() +"' and password_u= '" + txfPassword.getText() + "'");            
            if(password.next()){
                mensagemPopup("SUCESSO", "O login foi efetuado com sucesso!", Alert.AlertType.INFORMATION);
                System.out.println("SUCESSO! \n");
                return 0;
            }
            else{
                mensagemPopup("ERRO", "Password incorreta!", Alert.AlertType.ERROR);
                return -1;
            }
        }
        else{
            mensagemPopup("ERRO", "Username invalido!", Alert.AlertType.ERROR);
            return -1;
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
        em.clear();
        return result;
    }
    
    private boolean mensagemPopup(String title, String texto, Alert.AlertType tipo){
        Alert alert = new Alert(tipo);
        alert.setTitle(title);
        alert.setHeaderText(texto);
        
        Optional<ButtonType> option = alert.showAndWait();
        return option.get() == ButtonType.CANCEL;
    }

    
    
}
