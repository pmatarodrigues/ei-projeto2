
package yourasmusic;

import classes.Album;
import classes.Artista;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.TilePane;
import org.hibernate.Query;
import org.hibernate.Session;
import yourasmusic.YourasMusic;


public class FXMLArtistasController implements Initializable {

    @FXML TilePane tileArtistas;
    @FXML ScrollPane scrollPane;
    @FXML ComboBox cmbTipoOrdenacao;
    
    List<Artista> artistas;
    
    Session session;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cmbTipoOrdenacao.getItems().addAll("Ordenar por Nome", "Ordenar por Idade");
        cmbTipoOrdenacao.getSelectionModel().selectFirst();
        
        carregarArtistas();
    }    
    
    
    private void carregarArtistas(){        
        session = hibernate.HibernateUtil.getSessionFactory().openSession();
        
        Query obterArtista = session.createQuery("From Artista ORDER BY nomeArtista DESC");
        obterArtista.setFirstResult(0);
        obterArtista.setMaxResults(12);
        artistas = obterArtista.list();
        
        displayArtistas();
        
        session.close();
        
    }
    
    
    public void buttonClicked(String idArtista) throws IOException{
        System.out.println("CHEGOU");
        Artista artistaClicado = new Artista();
        // --- Iguala o album clicado a um novo album dentro da função
        for(Artista a : artistas){
            if(String.valueOf(a.getArtistaId()).equals(idArtista)){
                artistaClicado = a;
            }
        }
             
        // ---- PASSAR AS INFORMAÇÕES DO ALBUM PARA O FXML DE ALBUM INDIVIDUAL
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("FXMLArtistaIndividual.fxml"));
        
        // ---- ABRIR ALBUM INDIVIDUAL
        YourasMusic.getROOT().setRight(loader.load());
        
        FXMLArtistaIndividualController album = loader.getController();
        album.initData(artistaClicado);       
    }
    
    
    @FXML
    public void ordenarArtistas(ActionEvent event){
        
        session = hibernate.HibernateUtil.getSessionFactory().openSession();
        
        if(cmbTipoOrdenacao.getValue().toString().equals("Ordenar por Nome")){            
            Query obterArtistas = session.createQuery("From Artista ORDER BY nomeArtista DESC");
            obterArtistas.setFirstResult(0);
            obterArtistas.setMaxResults(12);
            artistas = obterArtistas.list();
        } else if(cmbTipoOrdenacao.getValue().toString().equals("Ordenar por Idade")){
            Query obterAlbuns = session.createQuery("From Artista ORDER BY dataNascimento ASC");
            obterAlbuns.setFirstResult(0);
            obterAlbuns.setMaxResults(12);
            artistas = obterAlbuns.list();
        }
        
        tileArtistas.getChildren().clear();
        
        displayArtistas();
        
        session.close();
    }
    
    private void displayArtistas(){
        for(Artista a : artistas){
            Button btnArtistas = new Button();

            btnArtistas.setId("button_tile");
            btnArtistas.setMinSize(200, 200);
            btnArtistas.setText(a.getNomeArtista().toString());

            tileArtistas.setHgap(20);
            tileArtistas.setVgap(20);
            tileArtistas.setAlignment(Pos.TOP_CENTER);
            tileArtistas.getChildren().add(btnArtistas);

            btnArtistas.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        buttonClicked(String.valueOf(a.getArtistaId()));
                    } catch (IOException ex) {
                        Logger.getLogger(FXMLArtistasController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        }    
    }
    
}
