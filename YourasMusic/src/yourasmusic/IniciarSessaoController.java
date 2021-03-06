
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
import java.util.List;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import classes.*;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;
import yourasmusic.YourasMusic;



public class IniciarSessaoController implements Initializable {
       
    public static Utilizador userLogin;
    public static Artista artistaLogin;
    public static Editora editoraLogin;
    public static DirEstudio dirEstudioLogin;

    @FXML
    private TextField txfUsername;
    @FXML
    private TextField txfPassword;

    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
     
    }    
    
    @FXML
    public void abrirPaginaCriarConta(ActionEvent event) throws IOException{
        Pane paneCriarConta= FXMLLoader.load(getClass().getResource("FXMLCriarConta.fxml"));
        YourasMusic.getROOT().setCenter(paneCriarConta);
    }
    
    @FXML
    public void iniciarSessao(ActionEvent event) throws SQLException, IOException{
        int verifica;
        verifica = verificarDadosLogin(event);
        if(verifica==0){
            Parent root = FXMLLoader.load(getClass().getResource("FXMLInicioRoot.fxml"));
            FXMLLoader musicPane = new FXMLLoader();
            if(artistaLogin != null){
                musicPane.setLocation(getClass().getResource("FXMLArtistaIndividual.fxml"));
                ((BorderPane) root).setRight(musicPane.load());

                FXMLArtistaIndividualController artista = musicPane.getController();
                // --- passar o artista com sessao iniciada
                artista.initData(artistaLogin);       
            } else{
                musicPane.setLocation(getClass().getResource("FXMLInicioPane.fxml"));
                ((BorderPane) root).setRight(musicPane.load());                
            }
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.hide();
            YourasMusic.setROOT((BorderPane) root);
            stage.setScene(new Scene(root));
            stage.show();
        }
    }
    
    @FXML
    public int verificarDadosLogin(ActionEvent event) throws SQLException{
        
        org.hibernate.Session session = hibernate.HibernateUtil.getSessionFactory().openSession();
        Criteria cr = session.createCriteria(Utilizador.class);                
        List<Utilizador> users = cr.list();                
                       
        // --- Aplicar restrições ao query na base de dados
        String emailRecebido = txfUsername.getText().toString();
        String passwordRecebida = txfPassword.getText().toString();
        
        Criterion email = Restrictions.eq("email", emailRecebido);
        Criterion password = Restrictions.eq("passwordU", passwordRecebida);
 
        LogicalExpression passEmail = Restrictions.and(email, password);
        cr.add(passEmail);
        
        for(Utilizador u : users){                        
            if(u.getEmail().toString().equals(emailRecebido)){
               userLogin = u;
               switch(userLogin.getTipo().trim()){
                    case "A":
                        Criteria crArtista = session.createCriteria(Artista.class);                
                        List<Artista> artistas = crArtista.list();                                                
                        for(Artista a : artistas){
                            if(u.getUtilizadorId() == a.getArtistaId()){
                                artistaLogin = a;
                            }
                        }
                    case "E":
                        Criteria crEditora = session.createCriteria(Editora.class);                
                        List<Editora> editoras = crEditora.list();
                        for(Editora e : editoras){
                            if(u.getUtilizadorId() == e.getEditoraId()){
                                editoraLogin = e;
                            }
                        }
                    case "S":
                        Criteria crEstudio = session.createCriteria(DirEstudio.class);                
                        List<DirEstudio> diretores = crEstudio.list();                                                
                        for(DirEstudio d : diretores){
                            if(u.getUtilizadorId() == d.getDirEstudioId()){
                                dirEstudioLogin = d;
                            }
                        }
                }               
            }        
            
        }
        if(!cr.list().isEmpty()){
            //mensagemPopup("SUCESSO", "O login foi efetuado com sucesso!", Alert.AlertType.INFORMATION);
            System.out.println("SUCESSO! \n");
            return 0;
        } else{
            mensagemPopup("ERRO", "Username ou Password invalido!", Alert.AlertType.ERROR);
            return -1;
        }                
    }
    
    
    private boolean mensagemPopup(String title, String texto, Alert.AlertType tipo){
        Alert alert = new Alert(tipo);
        alert.setTitle(title);
        alert.setHeaderText(texto);
        
        Optional<ButtonType> option = alert.showAndWait();
        return option.get() == ButtonType.CANCEL;
    } 
    
    public Utilizador getUserLogin(){
        return this.userLogin;
    }
}
