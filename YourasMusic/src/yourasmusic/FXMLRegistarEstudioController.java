
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
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;


public class FXMLRegistarEstudioController implements Initializable {
    
    @FXML TextArea txaMorada;
    @FXML ComboBox cmbDiretor;
    
    String moradaNova;
    List<DirEstudio> diretores;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        org.hibernate.Session session = hibernate.HibernateUtil.getSessionFactory().openSession();
        diretores = session.createCriteria(DirEstudio.class).list();
                
        for(DirEstudio e : diretores){
            cmbDiretor.getItems().add(e.getNome().toString());
        }
        
        session.close();             
    }    
    
    public void registarEstudio(){        
        DirEstudio dirEstudio = null;
        moradaNova = txaMorada.getText().toString();       
        
        System.out.println(IniciarSessaoController.userLogin.getUtilizadorId());
        org.hibernate.Session session = hibernate.HibernateUtil.getSessionFactory().openSession();
        
        for(DirEstudio dir : diretores){
            if(dir.getNome().equals(cmbDiretor.getValue().toString())){
                dirEstudio = dir;
            }
        }

        session.beginTransaction();
        Estudio estudio = new Estudio(dirEstudio, moradaNova);
        session.save(estudio);
        session.getTransaction().commit();
        session.close();
    }        
}
