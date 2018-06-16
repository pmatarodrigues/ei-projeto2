/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yourasmusic;

import classes.Artista;
import classes.Musica;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;
import javafx.scene.text.TextAlignment;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class FXMLMusicasController implements Initializable {

    @FXML TilePane tileMusicas;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarMusicas();
    }    
    
    
    private void carregarMusicas(){
        org.hibernate.Session session = hibernate.HibernateUtil.getSessionFactory().openSession();
        
        List<Musica> musicas = session.createCriteria(Musica.class).list();
        
        for(Musica m : musicas){
            Button btnMusicas = new Button();
            btnMusicas.setId("button_tile");
            btnMusicas.setMinSize(800, 50);
            btnMusicas.setMaxSize(800, 50);
            btnMusicas.setTextAlignment(TextAlignment.LEFT);
            btnMusicas.setText(m.getNome().toString() + "     " + m.getMusicaId());
            tileMusicas.setAlignment(Pos.TOP_CENTER);
            tileMusicas.setVgap(10);
            tileMusicas.getChildren().add(btnMusicas);
        }   
        
        session.close();
    }   
    
}
