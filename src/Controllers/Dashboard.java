package Controllers;

import DAO.DBAppointment;
import Model.Appointment;
import Model.Customers;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.ObservableList;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * controls the dashboard screen
 */
public class Dashboard {
    //customer table

    /**
     * reference for the gui
     */
    public TableView customersTable;

    /**
     * reference for the gui
     */
    public TableColumn customerID;
    /**
     * reference for the gui
     */
    public TableColumn name;
    /**
     * reference for the gui
     */
    public TableColumn address;
    /**
     * reference for the gui
     */
    public TableColumn post;
    /**
     * reference for the gui
     */
    public TableColumn phone;
    /**
     * reference for the gui
     */
    public TableColumn division;
    /**
     * reference for the gui
     */
    public TableColumn country;
    /**
     * reference for the gui
     */
    public TableColumn divisionName;

    /**
     * reference for the modify customer screen
     */
    public static Customers modifyCust;
    /**
     * reference for the modify customer screen
     */
    public static int customerIndex;

    //appointment table

    /**
     * reference for the gui
     */
    public TableView appointmentsTable;

    /**
     * reference for the gui
     */
    public TableColumn appointmentsID;
    /**
     * reference for the gui
     */
    public TableColumn title;
    /**
     * reference for the gui
     */
    public TableColumn description;
    /**
     * reference for the gui
     */
    public TableColumn appointmentLocation;
    /**
     * reference for the gui
     */
    public TableColumn appointmentType;
    /**
     * reference for the gui
     */
    public TableColumn start;
    /**
     * reference for the gui
     */
    public TableColumn end;
    /**
     * reference for the gui
     */
    public TableColumn customer;
    /**
     * reference for the gui
     */
    public TableColumn user;
    /**
     * reference for the gui
     */
    public TableColumn contact;

    /**
     * reference for the modify appointment screen
     */
    public static Appointment modifyApp;
    /**
     * reference for the modify appointment screen
     */
    public static int appointmentIndex;
    /**
     * reference for the gui
     */
    public Button exitDash;
    /**
     * reference for the gui
     */
    public Text timeZone;
    /**
     * reference for the gui
     */
    public Button reports;



    /**
     * initializes the dashboard
     */
    @FXML
    private void initialize(){
        //sets timezone
        timeZone.setText("Timezone: " + System.getProperty("user.timezone"));
        //populate the customers table
        customersTable.setItems(DAO.DBCustomers.getCustomerList());
        customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        address.setCellValueFactory(new PropertyValueFactory<>("address"));
        post.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        division.setCellValueFactory(new PropertyValueFactory<>("division"));
        divisionName.setCellValueFactory(new PropertyValueFactory<>("divisionName"));
        country.setCellValueFactory(new PropertyValueFactory<>("country"));

        //populate appointments table
        appointmentsTable.setItems(DAO.DBAppointment.getAppointmentList());
        appointmentsID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        appointmentLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        appointmentType.setCellValueFactory(new PropertyValueFactory<>("type"));
        start.setCellValueFactory(new PropertyValueFactory<>("start"));
        end.setCellValueFactory(new PropertyValueFactory<>("end"));
        customer.setCellValueFactory(new PropertyValueFactory<>("customer"));
        user.setCellValueFactory(new PropertyValueFactory<>("user"));
        contact.setCellValueFactory(new PropertyValueFactory<>("contact"));


    }

    /**
     * sets the appointments to this week
     * @param actionEvent on clicking this week radio button
     */
    public void onThisWeek(ActionEvent actionEvent) {

      appointmentsTable.setItems(DAO.DBAppointment.thisWeek());

    }

    /**
     * sets the appointments to this month
     * @param actionEvent on clicking this month radio button
     */
    public void onThisMonth(ActionEvent actionEvent) {
        appointmentsTable.setItems(DAO.DBAppointment.thisMonth());
    }

    /**
     * sets appointments to all
     * @param actionEvent on clicking all radio button
     */
    public void onAll(ActionEvent actionEvent) {
        appointmentsTable.setItems(DAO.DBAppointment.getAppointmentList());
    }

    /**
     * opens the add customer screen
     * @param actionEvent add customer
     * @throws IOException ioexception
     */
    public void onAddCustomer(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../Views/addCustomer.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 528, 532);
        stage.setTitle("Add Customer");
        stage.setScene(scene);
        stage.getScene().getWindow().centerOnScreen();
        stage.show();
    }

    /**
     * opens the modify customer screen
     * @param actionEvent modify customer
     * @throws IOException ioexception
     */
    public void onModifyCustomer(ActionEvent actionEvent) throws IOException {
        if(!customersTable.getSelectionModel().isEmpty()){
            modifyCust = (Customers)customersTable.getSelectionModel().getSelectedItem();
            customerIndex = customersTable.getSelectionModel().getSelectedIndex();
            Parent root = FXMLLoader.load(getClass().getResource("../Views/modifyCustomer.fxml"));
            Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 528, 532);
            stage.setTitle("Modify Customer");
            stage.setScene(scene);
            stage.getScene().getWindow().centerOnScreen();
            stage.show();
        }
        else
        {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Nothing Selected");
            errorAlert.setContentText("Nothing Selected to Modify");
            errorAlert.showAndWait();
        }
    }

    /**
     * checks if a customer is selected and confirms delete.  on deletion, deletes the customer from the database and observable list
     * then deletes all associated appointments
     * @param actionEvent
     */
    public void onDeleteCustomer(ActionEvent actionEvent) {
        if(!customersTable.getSelectionModel().getSelectedCells().isEmpty()){
            Customers c =(Customers) customersTable.getSelectionModel().getSelectedItem();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Record?");
            alert.setContentText("Are you sure you want to delete this customer?");
            ButtonType CANCEL = new ButtonType("Cancel");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK){
                int i = customersTable.getSelectionModel().getSelectedIndex();
                DAO.DBCustomers.getCustomerList().remove(i);
                i = c.getCustomerID();
                DAO.DBCustomers.deleteCustomerSQL(i);
            }
            else
                return;
        }
        //if nothing is selected, print an error message
        else
        {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Nothing Selected");
            errorAlert.setContentText("Nothing Selected to Delete");
            errorAlert.showAndWait();
        }
    }

    /**
     * switches to the add appointment screen to add an appointment
     * @param actionEvent clicking add
     * @throws IOException ioexception
     */
    public void onAddAppointment(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../Views/addAppointment.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 784, 601);
        stage.setTitle("Add Appointment");
        stage.setScene(scene);
        stage.getScene().getWindow().centerOnScreen();
        stage.show();
    }

    /**
     * opens the modify appointment screen
     * @param actionEvent modify appointment
     * @throws IOException ioexception
     */
    public void onModifyAppointment(ActionEvent actionEvent) throws IOException {
        if(!appointmentsTable.getSelectionModel().isEmpty()) {
            modifyApp = (Appointment) appointmentsTable.getSelectionModel().getSelectedItem();
            appointmentIndex = appointmentsTable.getSelectionModel().getSelectedIndex();
            Parent root = FXMLLoader.load(getClass().getResource("../Views/modifyAppointment.fxml"));
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 784, 601);
            stage.setTitle("Modify Appointment");
            stage.setScene(scene);
            stage.getScene().getWindow().centerOnScreen();
            stage.show();
        }
        else{
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Nothing Selected");
            errorAlert.setContentText("No Appointment Selected.");
            errorAlert.showAndWait();
        }
    }

    /**
     * deletes the selected appointment from the database as well as the observable list.
     * @param actionEvent on delete appointment
     */
    public void onDeleteAppointment(ActionEvent actionEvent) {
        if(!appointmentsTable.getSelectionModel().getSelectedCells().isEmpty()){
            Appointment a =(Appointment) appointmentsTable.getSelectionModel().getSelectedItem();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Record?");
            alert.setContentText("Are you sure you want to delete this appointment?");
            ButtonType CANCEL = new ButtonType("Cancel");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK){
                int i = appointmentsTable.getSelectionModel().getSelectedIndex();
                DAO.DBAppointment.getAppointmentList().remove(i);
                 i = a.getID();
                DAO.DBAppointment.deleteAppointment(i);
            }
            else
                return;
        }
        //if nothing is selected, print an error message
        else
        {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Nothing Selected");
            errorAlert.setContentText("Nothing Selected to Delete");
            errorAlert.showAndWait();
        }
    }

    /**
     * opens the reports page
     * @param actionEvent   reports
     * @throws IOException  ioexception
     */
    public void onReports(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../Views/reports.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1056, 599);
        stage.setTitle("Reports");
        stage.setScene(scene);
        stage.getScene().getWindow().centerOnScreen();
        stage.show();
    }

    /**
     * exits the program
     * @param actionEvent on exit
     */
    public void onExit(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit?");
        alert.setContentText("Are you sure you want to exit?");
        ButtonType CANCEL = new ButtonType("Cancel");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){
            Stage stage = (Stage) exitDash.getScene().getWindow();
            stage.close();
        }
    }


}
