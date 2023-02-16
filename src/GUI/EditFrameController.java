package GUI;

import GUI.util.Alerts;
import GUI.util.Utils;
import db.DbException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import model.entities.Lembrete;
import model.exceptions.ValidationException;
import model.service.LembretesService;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;


public class EditFrameController implements Initializable {

    private Lembrete entity;

    private LembretesService service;

    public void setEntity(Lembrete entity) {
        this.entity = entity;
    }

    public Lembrete getEntity() {
        return entity;
    }

    public void setService(LembretesService service){
        this.service = service;
    }

    @FXML
    private TextField txtName;

    @FXML
    private DatePicker dpDate;

    @FXML
    private Button btSave;

    @FXML
    private Button btCancel;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Utils.formatDatePicker(dpDate, "dd/MM/yyyy");
    }


    public void updateFormData() {
        if (entity == null) {
            throw new IllegalStateException("Entity was null");
        }

        txtName.setText(entity.getName());


        if (entity.getDate() != null) {
            dpDate.setValue(new Date(entity.getDate().getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        }

    }

    private Lembrete getFormData() {
        Lembrete obj = new Lembrete();

        ValidationException exception = new ValidationException("Validation error");

        obj.setId(entity.getId());

        if (txtName.getText() == null || txtName.getText().trim().equals("")) {
            throw new ValidationException("Campo nome não pode estar vazio");
        }
        obj.setName(txtName.getText());



        if (dpDate.getValue() == null) {
            throw new ValidationException("Campo data não pode estar vazio");
        } else {
            Instant instant = Instant.from(dpDate.getValue().atStartOfDay(ZoneId.systemDefault()));
            obj.setDate(Date.from(instant));
        }


        if (exception.getErrors().size() > 0) {
            throw exception;
        }

        return obj;
    }

    @FXML
    public void onBtSaveAction(ActionEvent event) {
        if (entity == null) {
            throw new IllegalStateException("Entity was null");
        }

        if (service == null) {
            throw new IllegalStateException("Service was null");
        }

        try {
            entity = getFormData();
            service.updateAll(entity);
            Alerts.showAlert("Tarefa Atualizada", null, "Tarefa Atualizada com sucesso!!", Alert.AlertType.INFORMATION);
            Utils.currentStage(event).close();
        } catch (DbException e) {
            Alerts.showAlert("Error Saving object", null, e.getMessage(), Alert.AlertType.ERROR);
        } catch (ValidationException e) {
            Alerts.showAlert("Erro de campo", null, e.getMessage(), Alert.AlertType.ERROR);
        }


    }

    @FXML
    private void onBtCancelAction(ActionEvent event) {
        Utils.currentStage(event).close();
    }
}
