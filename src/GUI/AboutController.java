package GUI;

import GUI.util.Alerts;
import GUI.util.Utils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Lembrete;
import model.service.LembretesService;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.*;

public class AboutController implements Initializable {

    private Integer idIncrement = 0;

    private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    private final List<CheckBox> checkBoxes = new ArrayList<>();
    private final List<Label> listLbEditar = new ArrayList<>();
    private final List<Label> listLbDeletar = new ArrayList<>();
    private LembretesService service = new LembretesService();

    @FXML
    private VBox vBoxEditar;
    @FXML
    private VBox vBoxDelete;
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


    //Inicio do programa
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showedUpCheckBox(new Date());
        Utils.formatDatePicker(dpLembrete, "dd/MM/yyyy");
        dpLembrete.setValue(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        dpDay.setValue(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
    }

    //Adicionar tarefas na tabela
    @FXML
    public void onBtnSubmit() {
        try {
            Instant instant = Instant.from(dpLembrete.getValue().atStartOfDay(ZoneId.systemDefault()));
            Lembrete lembrete = new Lembrete(idIncrement, textFieldValor.getText().trim(), Date.from(instant), false);

            List<Lembrete> lembreteList = service.findAll();

            if (lembrete.getName().matches("") || lembrete.getName().trim().equals("")) {
                throw new IllegalArgumentException("A tarefa não pode ser vazia.");
            }
            for (Lembrete individual : lembreteList) {
                if (individual.getName().matches(lembrete.getName()) && Objects.equals(individual.getDate(), Date.from(instant))) {
                    throw new IllegalArgumentException("Essa tarefa já está listada.");
                }
            }

            service.insert(lembrete);
            showedUpCheckBox(lembrete.getDate());
            dpDay.setValue(lembrete.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            takeIdOnList();

//

            Alerts.showAlert(null, "Sucesso!", "Item adicionado à lista", Alert.AlertType.INFORMATION);


        } catch (IllegalArgumentException e) {
            Alerts.showAlert(null, "Erro de entrada", e.getMessage(), Alert.AlertType.ERROR);
        }


    }


    //Mostrar checkbox com as informações exatas
    public void showedUpCheckBox(Date date) {

        Iterator<CheckBox> iter = checkBoxes.iterator();
        Iterator<Label> iterlistLbEditar = listLbEditar.iterator();
        Iterator<Label> iterlistLbDeletar = listLbDeletar.iterator();

        while (iter.hasNext()) {
            CheckBox cb = iter.next();
            Label lbEdi = iterlistLbEditar.next();
            Label lbDel = iterlistLbDeletar.next();

            vBoxItens.getChildren().remove(cb);
            vBoxEditar.getChildren().remove(lbEdi);
            vBoxDelete.getChildren().remove(lbDel);
            iter.remove();
            iterlistLbEditar.remove();
            iterlistLbDeletar.remove();
        }
        List<Lembrete> lembreteList = service.findByDate(date);


        for (Lembrete lembrete : lembreteList) {

            addBox(lembrete, lembreteList);
            takeIdOnList();

        }


    }
    //Adiciona, estilisa e coloca a função nas checkbox e nos botoes

    private void addBox(Lembrete lembrete, List<Lembrete> lembreteList) {


        CheckBox cb = new CheckBox(lembrete.getName());

        cb.setStyle("-fx-padding: 10 0 10 20; -fx-font-size: 15; -fx-font-weight: bold;");


        cb.setSelected(lembrete.getStatus());
        if (!lembrete.getStatus()) {
            cb.setTextFill(Color.RED);
        } else {
            cb.setTextFill(Color.GREEN);

        }
        Label lbEditar = new Label("Editar");

        lbEditar.setStyle("-fx-padding: 10 10 10 10; -fx-font-size: 15; -fx-background-insets: 3px; -fx-font-weight: bold; -fx-border-radius: 50px;" +
                "-fx-background-radius: 50px; -fx-background-color: yellow;");

        Label lbDeletar = new Label("Delete");

        lbDeletar.setStyle("-fx-padding: 10 10 10 10; -fx-font-size: 15; -fx-font-weight: bold; -fx-background-insets: 3px; -fx-border-radius: 50px;" +
                "-fx-background-radius: 50px; -fx-background-color: red");

        if (lembrete.getDate().compareTo(Date.from(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())) < 0 && lembrete.getStatus()) {
            cb.setDisable(true);
            lbEditar.setDisable(true);

        } else {
            cb.setOnAction(event -> handleCheckBox(cb));
            cb.setDisable(false);

            //Adicionando evento ao label editar e relacionando eles com o item respectivo no bd
            lbEditar.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    //--------------------Paga o id -----------------------------------------------------------------
                    Label lb = listLbEditar.stream().filter(x -> x.equals(lbEditar)).findAny().orElse(null);
                    int valueIndex = listLbEditar.indexOf(lb);
                    Integer idForEachCheck = lembreteList.get(valueIndex).getId();

                    createDialogForm(service.findById(idForEachCheck), "/GUI/EditFrame.fxml", Utils.currentStage(mouseEvent));
                }
            });

            lbEditar.setDisable(false);

        }
        //Adicionando evento ao label editar e relacionando eles com o item respectivo no bd
        lbDeletar.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                //--------------------Paga o id -----------------------------------------------------------------
                Label lb = listLbDeletar.stream().filter(x -> x.equals(lbDeletar)).findAny().orElse(null);
                int valueIndex = listLbDeletar.indexOf(lb);
                Integer idForEachCheck = lembreteList.get(valueIndex).getId();

                Optional<ButtonType> result = Alerts.showConfirmation("Deletar tarefa", "Você deseja " +
                        "deletar essa tarefa da sua lista? \n");
                if (result.get() == ButtonType.OK) {
                    service.deleteById(idForEachCheck);
                    showedUpCheckBox(Date.from(dpDay.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                }
            }
        });
        lbDeletar.setDisable(false);


        vBoxEditar.getChildren().add(lbEditar);
        vBoxDelete.getChildren().add(lbDeletar);

        listLbEditar.add(lbEditar);
        listLbDeletar.add(lbDeletar);

        vBoxItens.getChildren().add(cb);
        checkBoxes.add(cb);
    }
    //Auto incremento no id

    private void takeIdOnList() {
        List<Lembrete> listId = service.findAll();

        for (Lembrete lembrete : listId) {


            if (idIncrement <= lembrete.getId()) {
                idIncrement = lembrete.getId() + 1;
            }
        }
    }
    //funcao de trocar de status na checkbox

    private void handleCheckBox(CheckBox cb) {

        Lembrete tarefa = service.findByNameAndDate(cb.getText(), Date.from(Instant.from(dpDay.getValue().atStartOfDay(ZoneId.systemDefault()))));


        if (Objects.equals(df.format(tarefa.getDate()), df.format(new Date()))) {

            tarefa.setStatus(cb.isSelected());
            service.changeStatus(tarefa);

            if (!tarefa.getStatus()) {
                cb.setTextFill(Color.RED);
            } else {
                cb.setTextFill(Color.GREEN);

            }
        } else {

            List<Lembrete> lembreteList = service.findByDate(new Date());
            boolean certificar = false;
            for (Lembrete lemb : lembreteList) {
                if (lemb.getName().equals(tarefa.getName())) {
                    certificar = true;
                    break;
                }
            }

            if (certificar) {
                Alerts.showAlert(null, "Lembrete já existente", "Esse lembrete já existe no dia de hoje!", Alert.AlertType.WARNING);
                cb.setSelected(false);
            } else {
                Optional<ButtonType> result = Alerts.showConfirmation("Lembrete em outra data", "O lembrete que você deseja alterar o status" +
                        " é de outra data. Deseja adicionar ele ao dia de hoje e então alterar o status? \n");
                if (result.get() == ButtonType.OK) {
                    Date oldDate = tarefa.getDate();
                    tarefa.setDate(new Date());
                    tarefa.setStatus(cb.isSelected());

                    service.update(tarefa);

                    if (!tarefa.getStatus()) {
                        cb.setTextFill(Color.RED);
                    } else {
                        cb.setTextFill(Color.GREEN);

                    }

                    showedUpCheckBox(oldDate);

                } else {
                    cb.setSelected(false);
                }
            }
        }
    }

    //Visualizar tarefas na data (DatePicker em cima das tarefas)
    @FXML
    private void onBtnSumDate() {
        dpDay.setValue(dpDay.getValue().plusDays(1));
        onDpDay();
    }

    @FXML
    private void onBtnMinusDate() {
        dpDay.setValue(dpDay.getValue().minusDays(1));
        onDpDay();
    }

    @FXML
    private void onDpDay() {
        Instant instant = Instant.from(dpDay.getValue().atStartOfDay(ZoneId.systemDefault()));

        showedUpCheckBox(Date.from(instant));

    }


    private void createDialogForm(Lembrete obj, String absoluteName, Stage parentStage){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane = loader.load();

            EditFrameController controller = loader.getController();
            controller.setEntity(obj);
            controller.setService(new LembretesService());

            controller.updateFormData();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Atualize a tarefa");
            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false);
            dialogStage.initOwner(parentStage);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();

            if(obj.getDate() != controller.getEntity().getDate()) {
                dpDay.setValue(controller.getEntity().getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            }
            showedUpCheckBox(controller.getEntity().getDate());

        }catch (IOException e){
            e.printStackTrace();
            Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), Alert.AlertType.ERROR);



        }
    }

}
