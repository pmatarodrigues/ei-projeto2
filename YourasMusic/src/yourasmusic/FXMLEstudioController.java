/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yourasmusic;

import classes.Artista;
import classes.Estudio;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class FXMLEstudioController implements Initializable {

    @FXML TilePane tileEstudios;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
            carregarArtistas();
    }    
    
    
    private void carregarArtistas(){
        org.hibernate.Session session = hibernate.HibernateUtil.getSessionFactory().openSession();
        
        List<Estudio> estudios = session.createCriteria(Estudio.class).list();
        
        for(Estudio e : estudios){
            Button btnEstudios = new Button();
            btnEstudios.setId("button_tile");
            btnEstudios.setMinSize(200, 200);
            btnEstudios.setText(e.getMorada().toString());
            tileEstudios.setHgap(20);
            tileEstudios.setVgap(20);
            tileEstudios.setAlignment(Pos.CENTER);
            tileEstudios.getChildren().add(btnEstudios);
        }   
        
        session.close();
    }
    
}
