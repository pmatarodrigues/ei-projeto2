/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yourasmusic;

import classes.Artista;
import classes.Musica;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.TilePane;
import javafx.scene.text.TextAlignment;
import javazoom.jl.decoder.JavaLayerException;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;

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
            ToggleButton btnMusicas = new ToggleButton();
            btnMusicas.setId("button_tile");
            btnMusicas.setMinSize(800, 50);
            btnMusicas.setMaxSize(800, 50);
            btnMusicas.setTextAlignment(TextAlignment.LEFT);
            btnMusicas.setText(m.getNome().toString());
            tileMusicas.setAlignment(Pos.TOP_CENTER);
            tileMusicas.setVgap(10);
            tileMusicas.getChildren().add(btnMusicas);
            
            btnMusicas.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {                                                                                   
                    YourasMusic.getMp().blb = m.getAudio();
                    try {
                        YourasMusic.getMp().Pause();
                        YourasMusic.getMp().Play();
                    } catch (JavaLayerException ex) {
                        Logger.getLogger(FXMLMusicasController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(FXMLMusicasController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SQLException ex) {
                        Logger.getLogger(FXMLMusicasController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            });
        }   
        
        session.close();
    }   
        
}
