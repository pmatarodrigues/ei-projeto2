package yourasmusic;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;
import yourasmusic.entities.Album;


public class FXMLAlbunsController implements Initializable {
    
    ArrayList<Album> albums = new ArrayList<>();
    
    @FXML TilePane tileAlbuns;
    Album album = new Album();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        album.setNome("Like a Virgin");
        album.setAno("2010");
        album.setAlbumId(2);
        albums.add(album);
        
        carregarAlbums();
    }   
    
    private void carregarAlbums(){
        for(Album a : albums){
            Button btnAlbuns = new Button();
            btnAlbuns.setStyle("-fx-background-color: #272727;"
                             + "-fx-text-fill: #FFF;");
            btnAlbuns.setMinSize(200, 200);
            btnAlbuns.setText(a.getNome().toString());
            tileAlbuns.getChildren().add(btnAlbuns);
        }
        
    }
    
}
