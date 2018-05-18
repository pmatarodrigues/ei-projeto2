
package yourasmusic;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import yourasmusic.entities.Utilizador;



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
    @FXML
    BorderPane bdpCriarConta;
    
    EntityManager em;
    EntityManagerFactory emf;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        emf = Persistence.createEntityManagerFactory("YourasMusicPU");
        em = emf.createEntityManager();
        cmboxTipoUtilizador.setItems(FXCollections.observableArrayList("Artista", "Editora", "Estudio"));
    }
   
    @FXML
    public void abrirPaginaIniciarSessao(ActionEvent event) throws IOException{
        Pane paneCriarConta= FXMLLoader.load(getClass().getResource("FXMLIniciarSessao.fxml"));
        YourasMusic.getROOT().setCenter(paneCriarConta);
    }
    
    
    @FXML
    public void criarConta(ActionEvent event) throws IOException{        
        // --------- verificar qual o tipo de utilizador a adicionar
        String tipo;
        switch (this.cmboxTipoUtilizador.getValue().toString()) {
            case "Artista":
                tipo = "A";
                break;
            case "Editora":
                tipo = "E";
                break;
            default:
                tipo = "S";
                break;
        }
        
        /* INICIAR ENVIO PARA A BASE DE DADOS*/
        em.getTransaction().begin();
            Utilizador user = new Utilizador(this.txtfldEmail.getText(), this.txtfldPassword.getText(), tipo);
            em.persist(user);
            /* ENVIAR DADOS PARA A BASE DE DADOS*/
        em.getTransaction().commit();
        
        
        switch (tipo) {
            case "A":
                Pane paneContaArtista = FXMLLoader.load(getClass().getResource("FXMLCriarContaArtista.fxml"));
                YourasMusic.getROOT().setCenter(paneContaArtista);                
                break;
            case "E":
                Pane paneContaEditora = FXMLLoader.load(getClass().getResource("FXMLCriarContaEditora.fxml"));
                YourasMusic.getROOT().setCenter(paneContaEditora);  
                break;
            case "S":
                Pane paneContaEstudio = FXMLLoader.load(getClass().getResource("FXMLCriarContaEstudio.fxml"));
                YourasMusic.getROOT().setCenter(paneContaEstudio);  
                break;
            default:
                System.out.println("Default reached");
                break;
        }
        em.clear();
    }
}