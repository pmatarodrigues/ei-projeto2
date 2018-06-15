/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yourasmusic;

import classes.Artista;
import classes.Estudio;
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
import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class FXMLEstudioController implements Initializable {

    @FXML TilePane tileEstudios;
    
    List<Estudio> estudios;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
            carregarArtistas();
    }    
    
    
    private void carregarArtistas(){
        org.hibernate.Session session = hibernate.HibernateUtil.getSessionFactory().openSession();
        
        estudios = session.createCriteria(Estudio.class).list();
        
        for(Estudio e : estudios){
            Button btnEstudios = new Button();
            btnEstudios.setId("button_tile");
            btnEstudios.setMinSize(200, 200);
            btnEstudios.setText(e.getMorada().toString());
            tileEstudios.setHgap(20);
            tileEstudios.setVgap(20);
            tileEstudios.setAlignment(Pos.TOP_CENTER);
            tileEstudios.getChildren().add(btnEstudios);
            
            
            btnEstudios.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        buttonClicked(String.valueOf(e.getEstudioId()));
                    } catch (IOException ex) {
                        Logger.getLogger(FXMLArtistasController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        }           
        session.close();
    }
    
    
     public void buttonClicked(String idEstudio) throws IOException{
        Estudio estudioClicado = new Estudio();
        // --- Iguala o album clicado a um novo album dentro da função
        for(Estudio e : estudios){
            if(String.valueOf(e.getEstudioId()).equals(idEstudio)){
                estudioClicado = e;
            }
        }
             
        // ---- PASSAR AS INFORMAÇÕES DO ALBUM PARA O FXML DE ALBUM INDIVIDUAL
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("FXMLEstudioIndividual.fxml"));
        
        // ---- ABRIR ALBUM INDIVIDUAL
        YourasMusic.getROOT().setRight(loader.load());
        
        FXMLEstudioIndividualController estudio = loader.getController();
        estudio.initData(estudioClicado);       
    }
    
}
