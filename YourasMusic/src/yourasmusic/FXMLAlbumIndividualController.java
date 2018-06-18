
package yourasmusic;

import classes.Album;
import classes.Artista;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.hibernate.Criteria;
import org.hibernate.Session;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class FXMLAlbumIndividualController implements Initializable {

    @FXML Label nomeAlbum;
    @FXML Label anoAlbum;
    @FXML Label lblCapa;
    
    Album albumAtual;
    Artista artistaAlbum;
    
    Session session;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    void initData(Album albumClicado) throws SQLException {
        this.albumAtual = albumClicado;
        nomeAlbum.setText(albumClicado.getNome());
        anoAlbum.setText(albumClicado.getAno());                
        
        if(albumAtual.getCapa() != null){
            carregarCapa();            
        }
        else{
            lblCapa.setText("Capa nao disponível");
        }
    }

    private void carregarCapa() throws SQLException{
        ImageView capa;
        
        // atribuir imagem a Image
        Image imagem = new Image(albumAtual.getCapa().getBinaryStream());
        capa = new ImageView(imagem);
        capa.setPreserveRatio(false);
        capa.setFitHeight(550);
        capa.setFitWidth(550);
        lblCapa.setText("");
        // -- "Escrever" a imagem na label
        lblCapa.setGraphic(capa);      

    }
    
    private String verificarArtista(){
        
        session = hibernate.HibernateUtil.getSessionFactory().openSession();
        
        List<Artista> artistas = session.createCriteria(Artista.class).list();
        
        for(Artista a : artistas){
            if(a.equals(albumAtual.getArtista())){
                artistaAlbum = a;
            }
        }
        
        session.close();
        
        return artistaAlbum.getNomeArtista();
    }
    
}
