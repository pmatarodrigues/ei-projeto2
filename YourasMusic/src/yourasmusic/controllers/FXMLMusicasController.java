/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yourasmusic.controllers;

import classes.Artista;
import classes.Musica;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;

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
            btnMusicas.setMinSize(200, 200);
            btnMusicas.setText(m.getNome().toString());
            tileMusicas.getChildren().add(btnMusicas);
        }   
        
        session.close();
    }   
    
}
