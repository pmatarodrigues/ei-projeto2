
package yourasmusic;

import classes.Album;
import classes.Reserva;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import static javafx.collections.FXCollections.observableArrayList;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.hibernate.Session;


public class FXMLConsultarReservasController implements Initializable {

    @FXML TableView<Reserva> tblReservas;
    @FXML TableColumn<Reserva, String> tbl_colunaNumero;
    @FXML TableColumn<Reserva, String> tbl_colunaNome;
    @FXML TableColumn<Reserva, String> tbl_colunaRequerente;
    @FXML TableColumn<Reserva, String> tbl_colunaData;
    
    List<Reserva> reservas;
    private ObservableList<Reserva> reservasTabela = observableArrayList();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarReservas();
    }    
    
    private void carregarReservas(){
        Session session = hibernate.HibernateUtil.getSessionFactory().openSession();        
        
        this.reservas = session.createCriteria(Reserva.class).list();
        
        for(Reserva r : reservas){
            reservasTabela.add(r);
        }
        // TODO: CARREGAR ITEMS DAS RESERVAS NA TABELA
        tblReservas.setItems(reservasTabela);
        
        session.close();
    }
    
}
