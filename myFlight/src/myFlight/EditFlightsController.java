package myFlight;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class EditFlightsController implements Initializable {

	
	@FXML
	private TableView<flightTable> table;
	@FXML
	private TableColumn<flightTable, Integer> colFlightID;
	@FXML
	private TableColumn<flightTable, String> colFromCity;
	@FXML
	private TableColumn<flightTable, String> colToCity;
	@FXML
	private TableColumn<flightTable, String> colFlightDate;
	@FXML
	private TableColumn<flightTable, String> colFlightTime;
	@FXML
	private TableColumn<flightTable, Integer> colSeats;
	@FXML
	private TextField searchFrom;
	@FXML
	private TextField searchTo;
	@FXML
	private TextField searchDate;
	@FXML
	private TextField searchTime;
	@FXML
	private TextField searchID;
	@FXML
	private TextField editFromCity;
	@FXML
	private TextField editToCity;
	@FXML
	private TextField editFlightDate;
	@FXML
	private TextField editFlightTime;
	@FXML
	private TextField editTotalSeats;
	
	
    private final ObservableList<flightTable> datalist = FXCollections.observableArrayList();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		
		try {
			Connection conn = DBConnector.getConnection();
			PreparedStatement pst = conn.prepareStatement("select * from Flight");
			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				int flightID = rs.getInt("FlightID");
				String fromCity = rs.getString("FromCity");
				String toCity = rs.getString("ToCity");
				String flightDate = rs.getString("FlightDate");
				String flightTime = rs.getString("FlightTime");
				int numberOfSeats = rs.getInt("numberOfSeatsTotal");
				
				PreparedStatement ps = conn.prepareStatement("select " +flightID + " from BookedFlights");
				ResultSet rst = ps.executeQuery();
				int numberOfPassengers = 0;
				
				if (rst.next()) {
					++numberOfPassengers;
				}
				
				int seatsLeft = numberOfSeats - numberOfPassengers;
				
				datalist.add(new flightTable(flightID, fromCity, toCity, flightDate, flightTime, seatsLeft));
			
			} 
		} 
		  catch (Exception e) {

		  }

		colFlightID.setCellValueFactory(new PropertyValueFactory<>("flightID"));
		colFromCity.setCellValueFactory(new PropertyValueFactory<>("fromCity"));
		colToCity.setCellValueFactory(new PropertyValueFactory<>("toCity"));
		colFlightDate.setCellValueFactory(new PropertyValueFactory<>("flightDate"));
		colFlightTime.setCellValueFactory(new PropertyValueFactory<>("flightTime"));
		colSeats.setCellValueFactory(new PropertyValueFactory<>("seatsLeft"));
		
		table.setItems(datalist);
		
		FilteredList<flightTable> filteredData = new FilteredList<>(datalist, b -> true);
		
		searchFrom.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(flightTable -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				
				String lowerCaseFilter = newValue.toLowerCase();
				
				if (flightTable.getFromCity().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				}
				
				else 
					return false;
				});
		});
		
		searchTo.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(flightTable -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				
				String lowerCaseFilter = newValue.toLowerCase();
				
				if (flightTable.getToCity().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				}
				
				else 
					return false;
				});
		});
		
		searchDate.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(flightTable -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				
				String lowerCaseFilter = newValue.toLowerCase();
				
				if (flightTable.getFlightDate().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				}
				
				else 
					return false;
				});
		});
		
		searchTime.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(flightTable -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				
				String lowerCaseFilter = newValue.toLowerCase();
				
				if (flightTable.getFlightTime().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				}
				
				else 
					return false;
				});
		});
		
		searchID.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(flightTable -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				
				int newValueInt = Integer.parseInt(newValue);
				
				if (flightTable.getFlightID() == newValueInt) {
					return true;
				}
				
				else 
					return false;
				});
		});
		
		
		SortedList<flightTable> sortedData = new SortedList<>(filteredData);
		
		sortedData.comparatorProperty().bind(table.comparatorProperty());
		
		table.setItems(sortedData);
		
		
	}
	
	public void AddFlightButton(ActionEvent event) {
		
		try {
			Connection conn = DBConnector.getConnection();
			PreparedStatement pst = (PreparedStatement) conn.prepareStatement(
					"INSERT INTO Flight(FromCity, ToCity, FlightDate, FlightTime, numberOfSeatsTotal) " 
					+ "values (?,?,?,?,?)");
			
			pst.setString(1, editFromCity.getText());
			pst.setString(2, editToCity.getText());
			pst.setString(3, editFlightDate.getText());
			pst.setString(4, editFlightTime.getText());
			pst.setString(5, editTotalSeats.getText());
			pst.executeUpdate();
			
			editFromCity.setText("");
			editToCity.setText("");
			editFlightDate.setText("");
			editFlightTime.setText("");
			editTotalSeats.setText("");
			datalist.addAll(datalist);
		}
		
		catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public void DeleteFlightButton(ActionEvent event) {
		
		flightTable selectedFlight = table.getSelectionModel().getSelectedItem();
		int selectedFlightId = selectedFlight.getFlightID();
		
		try {
			Connection conn = DBConnector.getConnection();
			PreparedStatement pst = (PreparedStatement) conn.prepareStatement(
					"DELETE From flight WHERE FlightID = ?");
			
			pst.setInt(1, selectedFlightId);
			pst.executeUpdate();
		}
		
		catch (Exception e) {
			System.out.println(e);
		}
	}

	
	public void ReturnToAdminInterface(ActionEvent event) {
		try {
		Parent root = FXMLLoader.load(getClass().getResource("AdminInterface.FXML"));
		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		Scene scene= new Scene(root, 800, 600);
		stage.setScene(scene);
		stage.show();
		} catch (Exception e) {

		}
	}


	
	
}
