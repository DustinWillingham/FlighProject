package myFlight;

import java.awt.Label;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import java.sql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class BookFlightsController implements Initializable {

	

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
	
	static String userEmail;

	public List<flightTable> getAllFlightInfo() {
		
		
		List ll = new LinkedList();
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
				
				
				ll.add(new flightTable(flightID, fromCity, toCity, flightDate, flightTime, seatsLeft));
			}

		} catch (Exception e) {

		}

		return ll;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		

	}

	
	
	public void usersEmail(String email) {
		Loginset sendEmail = new Loginset();
		sendEmail.setUserEmail(email);
		userEmail = sendEmail.getUserEmail();
	}
	

	
	public void handle(ActionEvent event) {

		colFlightID.setCellValueFactory(new PropertyValueFactory<flightTable, Integer>("flightID"));
		colFromCity.setCellValueFactory(new PropertyValueFactory<flightTable, String>("fromCity"));
		colToCity.setCellValueFactory(new PropertyValueFactory<flightTable, String>("toCity"));
		colFlightDate.setCellValueFactory(new PropertyValueFactory<flightTable, String>("flightDate"));
		colFlightTime.setCellValueFactory(new PropertyValueFactory<flightTable, String>("flightTime"));
		colSeats.setCellValueFactory(new PropertyValueFactory<flightTable, Integer>("seatsLeft"));
		table.getItems().setAll(getAllFlightInfo());
	}
	
	public void bookAFlight(ActionEvent event) {

		flightTable selectedFlight = table.getSelectionModel().getSelectedItem();
		int selectedFlightId = selectedFlight.getFlightID();
		String selectedFromCity = selectedFlight.getFromCity();
		String selectedToCity = selectedFlight.getToCity();
		String selectedFlightDate = selectedFlight.getFlightDate();
		String selectedFlightTime = selectedFlight.getFlightTime();
		
		
		try {
			Connection conn = DBConnector.getConnection();
			PreparedStatement pst;
			pst = conn.prepareStatement("INSERT INTO bookedflights "
					+ "(CustomerEmail, FlightID, FlightDate, FlightTime, FromCity, ToCity)"
					+ "VALUES (?,?,?,?,?,?)");
			
			pst.setString(1, userEmail);
			pst.setInt(2, selectedFlightId);
			pst.setString(3, selectedFlightDate);
			pst.setString(4, selectedFlightTime);
			pst.setString(5, selectedFromCity);
			pst.setString(6, selectedToCity);
			pst.executeUpdate();
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
	}

	public void ReturnToCustomerInterface(ActionEvent event) {
		try {
		Parent root = FXMLLoader.load(getClass().getResource("CustomerInterface.FXML"));
		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		Scene scene= new Scene(root, 800, 600);
		stage.setScene(scene);
		stage.show();
		} catch (Exception e) {

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
