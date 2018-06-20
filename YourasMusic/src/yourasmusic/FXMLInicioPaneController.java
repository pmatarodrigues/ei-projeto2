
package yourasmusic;

import classes.Album;
import classes.Artista;
import classes.Musica;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import org.hibernate.Query;
import org.hibernate.Session;


public class FXMLInicioPaneController implements Initializable {
    
    public static ArrayList<String> generosMusica;
    
    @FXML
    AnchorPane anchor;
    @FXML ListView listaGeneros;
    @FXML TilePane listaGenerosTile;
    @FXML Button btnGenero;
    @FXML Label lblUltimoUploadMusica;
    @FXML Label lblUltimoUploadMusicaArtista;
    
    @FXML Label lblUltimoAlbum;
    @FXML Label lblUltimoAlbumArtista;
    @FXML Label lblUltimoAlbumAno;
    @FXML Label lblUltimoAlbumCapa;
    
    IniciarSessaoController iniciarSessaoController = new IniciarSessaoController();
    
    ArrayList<String> generos = new ArrayList<>();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {                    
        
        generos.add("Pop");
        generos.add("Rock");
        generos.add("Metal");
        generos.add("HipHop");
        generos.add("Indie");
        generos.add("Reggae");

        carregarGenerosDeMusica();
        carregarUltimoUpload();
        try {
           carregarUltimoAlbum();
        } catch (SQLException ex) {
            Logger.getLogger(FXMLInicioPaneController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        generosMusica = generos;
    }    
    
    
    private void carregarGenerosDeMusica(){  
        for(String s : generos){
            Button newButton = new Button();
            newButton.setId("button_tile");
            newButton.setMinSize(213, 160);
            newButton.setText(s);
            listaGenerosTile.getChildren().add(newButton);
        }
    }
    
        
    private void carregarUltimoUpload(){
        
        Session session;
        
        session = hibernate.HibernateUtil.getSessionFactory().openSession();
        
        List<Artista> artistas = session.createCriteria(Artista.class).list();
        
        Query query = session.createQuery("FROM Musica ORDER BY musicaId DESC");
        query.setMaxResults(1);
        Musica lastUpload = (Musica) query.uniqueResult();
        
        if(lastUpload != null){
            lblUltimoUploadMusica.setText(lastUpload.getNome().toString());
            for(Artista a : artistas){
                if(a.getArtistaId() == lastUpload.getUtilizador().getUtilizadorId()){
                    lblUltimoUploadMusicaArtista.setText(a.getNomeArtista().toString());
                }
            }
        } else{
            lblUltimoUploadMusica.setText("Nao existem musicas");
            lblUltimoUploadMusicaArtista.setVisible(false);
        }
        
        session.close();
    }
    
    private void carregarUltimoAlbum() throws SQLException{
        Session session;
        
        session = hibernate.HibernateUtil.getSessionFactory().openSession();                
        
        Query query = session.createQuery("FROM Album ORDER BY albumId DESC");
        query.setMaxResults(1);
        Album lastUpload = (Album) query.uniqueResult();
        
        if(lastUpload != null){
            lblUltimoAlbum.setText(lastUpload.getNome().toString());
            lblUltimoAlbumAno.setText(lastUpload.getAno().toString());
            lblUltimoAlbumArtista.setText(lastUpload.getArtista().getNomeArtista());
            
            session.close();
        
            ImageView capa;

            // atribuir imagem a Image
            if(lastUpload.getCapa() != null){
                Image imagem = new Image(lastUpload.getCapa().getBinaryStream());
                capa = new ImageView(imagem);
                capa.setPreserveRatio(false);
                capa.setFitHeight(200);
                capa.setFitWidth(200);
                capa.setStyle("-fx-border-color: #FFF; -fx-border-width: 20");
                lblUltimoAlbumCapa.setText("");
                // -- "Escrever" a imagem na label
                lblUltimoAlbumCapa.setGraphic(capa);
            } else {
                lblUltimoAlbumCapa.setText("Sem Capa");
            }            
        } else {
            lblUltimoAlbum.setText("Nao existem albuns");
            lblUltimoAlbumCapa.setVisible(false);
            lblUltimoAlbumArtista.setVisible(false);
            lblUltimoAlbumAno.setVisible(false);
        }

        
    }
    
    
}
