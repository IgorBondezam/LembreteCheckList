package GUI;

import GUI.util.Alerts;
import GUI.util.Utils;
import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.entities.Lembrete;
import model.service.LembretesService;

import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.util.*;

public class AboutController implements Initializable {

    private Integer idIncrement = 0;

    private final List<CheckBox> checkBoxes = new ArrayList<>();
    private LembretesService service = new LembretesService();

    @FXML
    private VBox vBoxItens;

    @FXML
    private Button btnSumDate;

    @FXML
    private Button btnMinusDate;

    @FXML
    private DatePicker dpDay;

    @FXML
    private Button btnSubmit;

    @FXML
    private TextField textFieldValor;

    @FXML
    private DatePicker dpLembrete;

    @FXML
    private ScrollPane scrollPaneItens;




    @FXML
    public void onBtnSubmit(){
        try {
            Instant instant = Instant.from(dpLembrete.getValue().atStartOfDay(ZoneId.systemDefault()));
            Lembrete lembrete = new Lembrete(idIncrement, textFieldValor.getText(), Date.from(instant), false);

            List<Lembrete> lembreteList = service.findAll();

            if (lembrete.getName().matches("") || lembrete.getName().trim().equals("")) {
                throw new IllegalArgumentException("A tarefa não pode ser vazia.");
            }
            for (Lembrete individual: lembreteList) {
                if (individual.getName().matches(lembrete.getName()) && Objects.equals(individual.getDate(), Date.from(instant))){
                    throw new IllegalArgumentException("Essa tarefa já está listada.");
                }
            }

            service.insert(lembrete);
            showedUpCheckBox(new Date());
            takeIdOnList();

//            if(Date.from(instant).getDay() == new Date().getDay() && Date.from(instant).getMonth() == new Date().getMonth()){
//                addBox(lembrete);
//            }

            Alerts.showAlert(null, "Sucesso!","Item adicionado à lista", Alert.AlertType.INFORMATION );


        }catch (IllegalArgumentException e){
            Alerts.showAlert(null, "Erro de entrada", e.getMessage(), Alert.AlertType.ERROR);
        }


    }

    @FXML
    private void onBtnSumDate(){
        dpDay.setValue(dpDay.getValue().plusDays(1));
        onDpDay();
    }

    @FXML
    private void onBtnMinusDate(){
        dpDay.setValue(dpDay.getValue().minusDays(1));
        onDpDay();
    }

    @FXML
    private void onDpDay(){
        Instant instant = Instant.from(dpDay.getValue().atStartOfDay(ZoneId.systemDefault()));

        showedUpCheckBox(Date.from(instant));

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showedUpCheckBox(new Date());
        Utils.formatDatePicker(dpLembrete,"dd/MM/yyyy");
        dpLembrete.setValue(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        dpDay.setValue(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
    }

    public void showedUpCheckBox(Date date){

        Iterator<CheckBox> iter = checkBoxes.iterator();

        while (iter.hasNext()){
            CheckBox cb =iter.next();
            vBoxItens.getChildren().remove(cb);
            iter.remove();
        }
        List<Lembrete> lembreteList = service.findByDate(date);


        for (Lembrete lembrete : lembreteList) {

            addBox(lembrete);
            takeIdOnList();

        }


    }


    private void addBox(Lembrete lembrete){


        CheckBox cb = new CheckBox(lembrete.getName());

        cb.setStyle("-fx-padding: 10 0 10 20; -fx-font-size: 15; -fx-font-weight: bold;");

        cb.setSelected(lembrete.getStatus());
        if (!lembrete.getStatus()) {
            cb.setTextFill(Color.RED);
        } else {
            cb.setTextFill(Color.GREEN);

        }
        cb.setOnAction(event -> handleCheckBox(cb));



        vBoxItens.getChildren().add(cb);
        checkBoxes.add(cb);
    }

    private void takeIdOnList(){
        List<Lembrete> listId = service.findAll();

        for (Lembrete lembrete : listId) {


            if(idIncrement <= lembrete.getId()) {
                idIncrement = lembrete.getId() + 1;
            }
        }
    }

    private void handleCheckBox(CheckBox cb){

        Lembrete tarefa = service.findByName(cb.getText());
        tarefa.setStatus(cb.isSelected());
        service.changeStatus(tarefa);

        if (!tarefa.getStatus()) {
            cb.setTextFill(Color.RED);
        } else {
            cb.setTextFill(Color.GREEN);

        }
    }


}
