package myFlight;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CustomerInterfaceController {

	
	public void availableFlights(ActionEvent bookFlight) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("BookFlights.FXML"));
			Stage stage = (Stage)((Node)bookFlight.getSource()).getScene().getWindow();
			Scene scene= new Scene(root, 800, 600);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {

		  }
	}

	public void bookedFlights(ActionEvent bookedFlights) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("BookedFlights.FXML"));
			Stage stage = (Stage)((Node)bookedFlights.getSource()).getScene().getWindow();
			Scene scene= new Scene(root, 800, 600);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {

		}
	}
	

	
	public void backToLogin(ActionEvent event) {
		Platform.exit();
	}
	
}
