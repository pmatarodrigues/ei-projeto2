
package yourasmusic;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import yourasmusic.entities.Artista;
import yourasmusic.entities.DirEstudio;
import yourasmusic.entities.Editora;
import yourasmusic.entities.Estudio;
import yourasmusic.entities.Utilizador;



public class CriarContaController implements Initializable {
    
    String tipo;
    
    @FXML
    TextField txtfldUsername;
    @FXML
    TextField txtfldEmail;
    @FXML
    TextField txtfldPassword;
    @FXML
    TextField txtfldConfirmarPassword;
    @FXML
    ComboBox cmboxTipoUtilizador; 
    
    @FXML
    TextField txfldNomeCompleto;
    @FXML
    Label lblNomeCompleto;
    @FXML
    TextField txfldNomeArtista;
    @FXML
    Label lblNomeArtista;
    @FXML
    TextField txfldNacionalidade;
    @FXML
    Label lblNacionalidade;
    @FXML
    TextField txfldContacto;
    @FXML
    Label lblContacto;
    @FXML
    Label lblDataNascimento;
    @FXML
    DatePicker dtpDataNascimento;
    
    @FXML
    BorderPane bdpCriarConta;
    
    EntityManager em;
    EntityManagerFactory emf;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        emf = Persistence.createEntityManagerFactory("YourasMusicPU");
        em = emf.createEntityManager();
        cmboxTipoUtilizador.setItems(FXCollections.observableArrayList("Artista", "Editora", "Dir. Estudio"));
        
        lblNomeCompleto.setVisible(false);
        lblNomeArtista.setVisible(false);
        lblNacionalidade.setVisible(false);
        lblContacto.setVisible(false);
        lblDataNascimento.setVisible(false);
        txfldNomeCompleto.setVisible(false);
        txfldNomeArtista.setVisible(false);
        txfldNacionalidade.setVisible(false);
        txfldContacto.setVisible(false);
        dtpDataNascimento.setVisible(false);
    }
   
    @FXML
    public void abrirPaginaIniciarSessao(ActionEvent event) throws IOException{
        Pane paneCriarConta= FXMLLoader.load(getClass().getResource("FXMLIniciarSessao.fxml"));
        YourasMusic.getROOT().setCenter(paneCriarConta);
    }
    
    
    @FXML
    public void criarConta(ActionEvent event) throws IOException, SQLException{        
        // --------- verificar qual o tipo de utilizador a adicionar
        Artista artista = new Artista();
        Editora editora = new Editora();
        DirEstudio dirEstudio = new DirEstudio();

        /* INICIAR ENVIO PARA A BASE DE DADOS*/
        em.getTransaction().begin();
        Utilizador user = new Utilizador(this.txtfldEmail.getText(), this.txtfldPassword.getText(), tipo);
        em.persist(user);
        /* ENVIAR DADOS PARA A BASE DE DADOS*/
        em.getTransaction().commit();
        em.clear();
        
        ResultSet result;        
        em.getTransaction().begin();
        //---- ASSOCIAR A BASE DE DADOS À VARIAVEL 'CON'
            java.sql.Connection con = em.unwrap(java.sql.Connection.class);
            PreparedStatement st = con.prepareStatement("SELECT * \n" +
                                                        "FROM \n" +
                                                        "    (SELECT *\n" +
                                                        "     FROM Utilizador\n" +
                                                        "     ORDER BY Utilizador_ID DESC)\n" +
                                                        "WHERE  rownum <= 1");
        //---- EXECUTAR QUERY
            result = st.executeQuery();
            result.next();
            System.out.println(result.getInt(1));
        em.getTransaction().commit();
        em.clear();
        int userID = result.getInt(1);
        
       em.getTransaction().begin();
        switch(tipo){
            case "A":
                artista = new Artista(userID, txfldNomeCompleto.getText().toString(), txfldNomeArtista.getText().toString(), Date.from(dtpDataNascimento.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()), 
                    txfldNacionalidade.getText().toString(), txfldContacto.getText().toString());
                em.persist(artista);
                break;
            case "E":
                editora = new Editora(userID, txfldNomeCompleto.getText().toString(), txfldNomeArtista.getText().toString(), txfldContacto.getText().toString());
                em.persist(editora);                       
                break;
            default:
                dirEstudio = new DirEstudio(userID, txfldNomeCompleto.getText().toString(), txfldNomeArtista.getText().toString());
                em.persist(dirEstudio);
                break;
        }
        em.getTransaction().commit();
        em.clear();


    }
    
    @FXML
    public void verificarTipoUser(ActionEvent event){
        switch (this.cmboxTipoUtilizador.getValue().toString()) {
            case "Artista":
                tipo = "A";
                // -------- torna visiveis os componentes de registo de artista
                    lblNomeCompleto.setText("Nome Completo");
                    lblNomeCompleto.setVisible(true);
                    lblNomeArtista.setText("Nome Artista");
                    lblNomeArtista.setVisible(true);
                    lblNacionalidade.setText("Nacionalidade");
                    lblNacionalidade.setVisible(true);
                    lblContacto.setText("Contacto");
                    lblContacto.setVisible(true);
                    lblDataNascimento.setText("Data de Nascimento");
                    lblDataNascimento.setVisible(true);
                    dtpDataNascimento.setValue(LocalDate.now());
                    
                    txfldNomeCompleto.setVisible(true);
                    txfldNomeArtista.setVisible(true);
                    txfldNacionalidade.setVisible(true);
                    txfldContacto.setVisible(true);
                    dtpDataNascimento.setVisible(true);              
                
                break;
            case "Editora":
                tipo = "E";     
                // -------- torna visiveis os componentes de registo de editora
                    lblNacionalidade.setVisible(false);
                    lblDataNascimento.setVisible(false);                   
                    txfldNacionalidade.setVisible(false);
                    dtpDataNascimento.setVisible(false);
                    
                    lblNomeCompleto.setText("Nome");
                    lblNomeCompleto.setVisible(true);
                    lblNomeArtista.setText("Morada");
                    lblNomeArtista.setVisible(true);
                    lblContacto.setText("Contacto");
                    lblContacto.setVisible(true);
                    
                    txfldNomeCompleto.setVisible(true);
                    txfldNomeArtista.setVisible(true);
                    txfldContacto.setVisible(true);           
               
                break;
            default:
                tipo = "S";
                // -------- torna visiveis os componentes de registo de diretor de estudio
                    lblNacionalidade.setVisible(false);
                    lblContacto.setVisible(false);
                    lblDataNascimento.setVisible(false);                  
                    txfldNacionalidade.setVisible(false);
                    txfldContacto.setVisible(false);
                    dtpDataNascimento.setVisible(false);

                    lblNomeCompleto.setText("Nome");
                    lblNomeCompleto.setVisible(true);
                    lblNomeArtista.setText("Contacto");
                    lblNomeArtista.setVisible(true);
                                        
                    txfldNomeCompleto.setVisible(true);
                    txfldNomeArtista.setVisible(true);                                    
                break;
        }
    }
}