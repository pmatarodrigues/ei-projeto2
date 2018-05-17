
package yourasmusic;

import yourasmusic.db.*;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;



public class CriarContaController implements Initializable {
    
    @FXML
    TextField txtfldUsername;
    @FXML
    TextField txtfldEmail;
    @FXML
    TextField txtfldPassword;
    @FXML
    TextField txtfldConfirmarPassword;
    @FXML
    ComboBox cmboxTipoUtilizador; 
    
    EntityManager em;
    EntityManagerFactory emf;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        emf = Persistence.createEntityManagerFactory("YourasMusicPU");
        em = emf.createEntityManager();
        
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
    
    
    @FXML
    public void criarConta(ActionEvent event){
        
        /* INICIAR ENVIO PARA A BASE DE DADOS*/
        em.getTransaction().begin();
            Utilizador user = new Utilizador(this.txtfldEmail.getText(), this.txtfldPassword.getText());
            em.persist(user);
            /* ENVIAR DADOS PARA A BASE DE DADOS*/
        em.getTransaction().commit();
        em.clear();
       
    }
    
}