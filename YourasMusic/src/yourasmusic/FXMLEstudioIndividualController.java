
package yourasmusic;

import classes.Artista;
import classes.DirEstudio;
import classes.Estudio;
import classes.Reserva;
import classes.Utilizador;
import java.net.URL;
import java.sql.Date;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

public class FXMLEstudioIndividualController implements Initializable {

    @FXML Label nomeDir;
    @FXML Label moradaEstudio;
    @FXML Button btnReserva;
    @FXML DatePicker dtDataReserva;
    @FXML Label lblAvisos;
    
    Estudio estudio;
    DirEstudio dirEstudio;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void initData(Estudio estudioClicado) {
        org.hibernate.Session session = hibernate.HibernateUtil.getSessionFactory().openSession();
        
        List<DirEstudio> direstudios = session.createCriteria(DirEstudio.class).list();
        
        for(DirEstudio d : direstudios){                        
            if(d.getDirEstudioId() == (estudioClicado.getDirEstudio().getDirEstudioId())){
               dirEstudio = d;
            }
        }
        
        this.estudio = estudioClicado;
        nomeDir.setText(dirEstudio.getNome().toString());
        moradaEstudio.setText(estudioClicado.getMorada());
        
        if(IniciarSessaoController.userLogin.getTipo().equals("S  ")){
            dtDataReserva.setVisible(false);
            btnReserva.setVisible(false);
        }
                
        session.close();
    }  
    
    @FXML
    public void efetuarReserva(ActionEvent event){
        Boolean estudioOcupado = false;
        
        if(dtDataReserva.getValue() == null){
            lblAvisos.setText("Selecione uma data");
        } else{
            org.hibernate.Session session = hibernate.HibernateUtil.getSessionFactory().openSession();
            // --- Recebe a data do DatePicker
            java.util.Date dataReserva = Date.from(dtDataReserva.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());

            // --- Receber o utilizador com login iniciado
            Utilizador user = IniciarSessaoController.userLogin;

            List<Reserva> reservas = session.createCriteria(Reserva.class).list();

            for(Reserva r : reservas){
                if(r.getEstudio().getEstudioId() == estudio.getEstudioId()){
                    if(r.getDataReserva().equals(dataReserva)){
                        estudioOcupado = true;
                    }
                }
            }
            if(estudioOcupado){
                mensagemPopup("Selecione outra data", "Estudio ocupado", Alert.AlertType.ERROR);
            } else{
                mensagemPopup("Reserva registada", "Obrigado!", Alert.AlertType.INFORMATION);
                Reserva reserva;
                // ---- Registar a reserva
                reserva = new Reserva(dataReserva, user, estudio, 'N', 'N');              
                session.beginTransaction();
                session.save(reserva);
                session.getTransaction().commit(); 
                session.close();
            }           
        }
    }
    
    
    private boolean mensagemPopup(String title, String texto, Alert.AlertType tipo){
        Alert alert = new Alert(tipo);
        alert.setTitle(title);
        alert.setHeaderText(texto);
        
        Optional<ButtonType> option = alert.showAndWait();
        return option.get() == ButtonType.CANCEL;
    } 
    
    
}
