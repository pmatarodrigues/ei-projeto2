
package yourasmusic;

import classes.Album;
import classes.DirEstudio;
import classes.Estudio;
import classes.Utilizador;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;


public class FXMLRegistarEstudioController implements Initializable {
    
    @FXML TextArea txaMorada;
    @FXML Label lblDiretor;
    @FXML Label lblAvisos;
    
    String moradaNova;
    List<DirEstudio> diretores;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblDiretor.setText(IniciarSessaoController.dirEstudioLogin.getNome().toString());
    }    
    
    public void registarEstudio() throws IOException{    
        
        if(txaMorada.getText().length() < 5){
            lblAvisos.setText("Introduza uma morada valida");            
        }
        else{
            
            // --- POPUP DE INFORMAÇAO
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Registo Efetuado");
            alert.setHeaderText("Foi registado o estudio com a morada " + txaMorada.getText());  
            alert.showAndWait();
            
            
            // -- VOLTAR PARA A PAGINA INICIAL
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("FXMLRegistarEstudio.fxml"));
            YourasMusic.getROOT().setRight(loader.load());
            
            DirEstudio dirEstudio = null;
            moradaNova = txaMorada.getText().toString();       

            System.out.println(IniciarSessaoController.userLogin.getUtilizadorId());
            org.hibernate.Session session = hibernate.HibernateUtil.getSessionFactory().openSession();

            session.beginTransaction();
            Estudio estudio = new Estudio(IniciarSessaoController.dirEstudioLogin, moradaNova);
            session.save(estudio);
            session.getTransaction().commit();
            session.close();
        }
    }        
}
