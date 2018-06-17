
package yourasmusic;

import classes.Album;
import classes.Musica;
import classes.Reserva;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import static javafx.collections.FXCollections.observableArrayList;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.TilePane;
import javafx.scene.text.TextAlignment;
import org.hibernate.Query;
import org.hibernate.Session;


public class FXMLConsultarReservasController implements Initializable {

    @FXML TilePane tileReservas;
    
    Session session;
    
    Reserva reservaAEliminar;
    
    List<Reserva> reservas;
    private ObservableList<Reserva> reservasTabela = observableArrayList();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Session session = hibernate.HibernateUtil.getSessionFactory().openSession();        
        
        carregarReservas();
        
        session.close();
    }    
    
    private void carregarReservas(){
        
        session = hibernate.HibernateUtil.getSessionFactory().openSession();
        
        List<Reserva> reservas = session.createCriteria(Reserva.class).list();
        
        for(Reserva r : reservas){
            Button btnReservas = new Button();
            btnReservas.setId("button_tile");
            btnReservas.setMinSize(800, 50);
            btnReservas.setMaxSize(800, 50);
            btnReservas.setTextAlignment(TextAlignment.LEFT);
            btnReservas.setText(r.getUtilizador().getEmail().toString() + "     " + r.getDataReserva().toString());
            tileReservas.setAlignment(Pos.TOP_CENTER);
            tileReservas.setVgap(10);
            tileReservas.getChildren().add(btnReservas);
            
            // --- Quando o botão é clicado, passa o id do album clicado como parametro
            btnReservas.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    //TODO: BOTAO FICA SELECIONADO COM CLICK
                    reservaAEliminar = r;
                }
            });
        }   
        
        session.close();
                
    }

    @FXML
    private void deleteReserva(ActionEvent event){
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Eliminar Reserva");
        alert.setHeaderText("Tem a certeza que pretende eliminar?");
        
        Optional<ButtonType> option = alert.showAndWait();
        
        if(option.get() == ButtonType.OK){        
            session = hibernate.HibernateUtil.getSessionFactory().openSession();

            session.beginTransaction();
            Query delete = session.createQuery("DELETE Reserva WHERE reservaId =" + reservaAEliminar.getReservaId());
            delete.executeUpdate();
            session.getTransaction().commit();

            session.close();
        }
    }
    
}
