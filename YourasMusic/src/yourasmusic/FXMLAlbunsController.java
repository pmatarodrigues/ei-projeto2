package yourasmusic;

import classes.Album;
import classes.Artista;
import classes.Utilizador;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.hibernate.Criteria;
import org.hibernate.Query;
import yourasmusic.YourasMusic;


public class FXMLAlbunsController implements Initializable {
    
    
    @FXML TilePane tileAlbuns;
    @FXML BorderPane mainPane;
    
    List<Album> albums;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {        
        carregarAlbums();
    }   
    
    private void carregarAlbums(){
        
        org.hibernate.Session session = hibernate.HibernateUtil.getSessionFactory().openSession();        
        //ArrayList<Button> buttons = new ArrayList<>();
        this.albums = session.createCriteria(Album.class).list();
        
        for(Album a : albums){
            Button btnAlbuns = new Button();
            String nomeAlbum = a.getNome().toString();
            String ano = a.getAno().toString();
            
            btnAlbuns.setId("button_tile");
            btnAlbuns.setMinSize(200, 200);
            btnAlbuns.setMaxSize(200, 200);            
            
            btnAlbuns.setText(nomeAlbum + "\n" + ano);
            
            // --- Personalização do TilePane
            tileAlbuns.setHgap(20);
            tileAlbuns.setVgap(20);
            tileAlbuns.setAlignment(Pos.CENTER);
            tileAlbuns.getChildren().add(btnAlbuns);
            
            // --- Quando o botão é clicado, passa o id do album clicado como parametro
            btnAlbuns.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        buttonClicked(String.valueOf(a.getAlbumId()));
                    } catch (IOException ex) {
                        Logger.getLogger(FXMLAlbunsController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        }
       
        session.close();
    }
    
    public void buttonClicked(String idAlbum) throws IOException{
        Album albumClicado = new Album();
        // --- Iguala o album clicado a um novo album dentro da função
        for(Album a : albums){
            if(String.valueOf(a.getAlbumId()).equals(idAlbum)){
                albumClicado = a;
            }
        }
             
        // ---- PASSAR AS INFORMAÇÕES DO ALBUM PARA O FXML DE ALBUM INDIVIDUAL
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("FXMLAlbumIndividual.fxml"));
        
        // ---- ABRIR ALBUM INDIVIDUAL
        YourasMusic.getROOT().setRight(loader.load());
        
        FXMLAlbumIndividualController album = loader.getController();
        album.initData(albumClicado);       
    }
        
}
