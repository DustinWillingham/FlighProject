package myFlight;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

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

public class BookedFlightsController implements Initializable {

	@FXML
	private TableView<BookedFlightsTable> table;
	@FXML
	private TableColumn<BookedFlightsTable, Integer> colFlightIDB;
	@FXML
	private TableColumn<BookedFlightsTable, String> colFlightDateB;
	@FXML
	private TableColumn<BookedFlightsTable, String> colFlightTimeB;
	@FXML
	private TableColumn<BookedFlightsTable, String> colFromCityB;
	@FXML
	private TableColumn<BookedFlightsTable, String> colToCityB;

	String userEmail;
	
	public void usersEmail(String email) {
		Loginset sendEmail = new Loginset();
		sendEmail.setUserEmail(email);
		userEmail = sendEmail.getUserEmail();
	}
	
	public List<BookedFlightsTable> getAllBookedFlightInfo() {

		List mm = new LinkedList();
		String userEmail = BookFlightsController.userEmail;
		
		try {
			Connection conn = DBConnector.getConnection();
			PreparedStatement pst = conn.prepareStatement("select * from BookedFlights WHERE CustomerEmail ='" + userEmail + "'");
			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				String CustomerEmail = rs.getString("CustomerEmail");
				int flightID = rs.getInt("FlightID");
				String flightDate = rs.getString("FlightDate");
				String flightTime = rs.getString("FlightTime");
				String fromCity = rs.getString("FromCity");
				String toCity = rs.getString("ToCity");

				mm.add(new BookedFlightsTable(flightID, flightDate, flightTime, fromCity, toCity));
			}
		} catch (Exception e) {

		}

		return mm;

	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

	public void handleb(ActionEvent event) {

		colFlightIDB.setCellValueFactory(new PropertyValueFactory<BookedFlightsTable, Integer>("flightID"));
		colFlightDateB.setCellValueFactory(new PropertyValueFactory<BookedFlightsTable, String>("flightDate"));
		colFlightTimeB.setCellValueFactory(new PropertyValueFactory<BookedFlightsTable, String>("flightTime"));
		colFromCityB.setCellValueFactory(new PropertyValueFactory<BookedFlightsTable, String>("fromCity"));
		colToCityB.setCellValueFactory(new PropertyValueFactory<BookedFlightsTable, String>("toCity"));
		table.getItems().setAll(getAllBookedFlightInfo());
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
