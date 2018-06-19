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
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.TilePane;
import javafx.scene.text.TextAlignment;
import javazoom.jl.decoder.JavaLayerException;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
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
    @FXML ComboBox cmbGeneroMusica;
    
    List<Musica> musicas;
    
    Session session;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cmbGeneroMusica.getItems().addAll("Pop", "Rock", "Metal", "HipHop", "Indie", "Reggae", "Outros");
        
        carregarMusicas();
    }    
    
    
    private void carregarMusicas(){        
        session = hibernate.HibernateUtil.getSessionFactory().openSession();
        
        Query obterMusica = session.createQuery("From Musica");
        obterMusica.setFirstResult(0);
        obterMusica.setMaxResults(15);
        musicas = obterMusica.list();
        
        displayMusicas();
        
        session.close();
    }   
    
    
    @FXML
    private void ordenarMusicas(){
        session = hibernate.HibernateUtil.getSessionFactory().openSession();

        Query obterMusicas = session.createQuery("From Musica WHERE genero = :genero ORDER BY nome DESC");
        
        switch(cmbGeneroMusica.getValue().toString()){
            case "Pop":
                obterMusicas.setParameter("genero", "Pop");
                break;
            case "Rock":
                obterMusicas.setParameter("genero", "Rock");
                break;
            case "Metal":
                obterMusicas.setParameter("genero", "Metal");
                break;
            case "HipHop":
                obterMusicas.setParameter("genero", "HipHop");
                break;
            case "Indie":
                obterMusicas.setParameter("genero", "Indie");
                break;
            case "Reggae":
                obterMusicas.setParameter("genero", "Reggae");
                break;
            default:
                obterMusicas = session.createQuery("From Musica Where genero != 'Pop' AND genero != 'Rock' AND genero != 'Metal' AND genero != 'HipHop' AND genero != 'Indie' AND genero != 'Reggae' ORDER BY nome DESC");                
                break;
        }
        obterMusicas.setMaxResults(15);
        musicas = obterMusicas.list();
                
        tileMusicas.getChildren().clear();
        
        displayMusicas();
        
        session.close();
    }
    
    
    private void displayMusicas(){
        ToggleGroup grupo = new ToggleGroup();

        
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
            btnMusicas.setToggleGroup(grupo);
            
            
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
    }
        
}
