/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yourasmusic;

import classes.Artista;
import classes.Editora;
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
public class FXMLEditoraController implements Initializable {

    @FXML TilePane tileEditoras;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
            carregarEditoras();
    }    
    
    
    private void carregarEditoras(){
        org.hibernate.Session session = hibernate.HibernateUtil.getSessionFactory().openSession();
        
        List<Editora> editoras = session.createCriteria(Editora.class).list();
        
        for(Editora e : editoras){
            Button btnEditoras = new Button();
            btnEditoras.setId("button_tile");
            btnEditoras.setMinSize(200, 200);
            btnEditoras.setText(e.getNome().toString());
            tileEditoras.setHgap(20);
            tileEditoras.setVgap(20);
            tileEditoras.setAlignment(Pos.CENTER);
            tileEditoras.getChildren().add(btnEditoras);
        }   
        
        session.close();
    } 
    
}
