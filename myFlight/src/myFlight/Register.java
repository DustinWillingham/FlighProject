package myFlight;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;


public class Register{


	@FXML
	private TextField registerEmail;

	@FXML
	private TextField registerPassword;

	@FXML
	private TextField registerFirst;

	@FXML
	private TextField registerLast;

	@FXML
	private TextField registerSSN;

	@FXML
	private TextField registerAddress;

	@FXML
	private TextField registerCity;

	@FXML
	private TextField registerState;

	@FXML
	private TextField registerZip;

	@FXML
	private Button loginRegister;

	@FXML
	private Label registerStatus;

	@FXML
	private ChoiceBox<String> registerSecurityQ;

	@FXML
	private TextField registerSecurityA;

	@FXML
	private Button registerButton;
	
	private String[] questions = {"What is your dog's name?", "What is your mother's maiden name?", 
			"What elementary school did you attend?"};
	

	//Register 
	public void RegisterButton(ActionEvent event) {

		try {
			
			Connection conn = DBConnector.getConnection();
			PreparedStatement pst = (PreparedStatement) conn.prepareStatement(
					"INSERT INTO Login(CustomerEmail, Password, SecurityQuestion, SecurityQuestionAnswer,"
					+ "CustomerSSN, CustomerFirst, CustomerLast, CustomerStreet, CustomerCity, CustomerZip, CustomerState) "
					+ "values (?,?,?,?,?,?,?,?,?,?,?)");
		
			PreparedStatement exisitingEmail =  (PreparedStatement) conn.prepareStatement(
					"SELECT idLogin FROM Login WHERE CustomerEmail ='" + registerEmail.getText() + "' ");
			
			String myQuestion = registerSecurityQ.getValue();
			
			ResultSet rs = exisitingEmail.executeQuery();
			
			if(rs.next()) {
				registerStatus.setText("Email already exist");
			
			}
			
			
			if (registerPassword.getText().length() >= 8) {

				pst.setString(1, registerEmail.getText());
				pst.setString(2, registerPassword.getText());
				pst.setString(3, myQuestion);
				pst.setString(4, registerSecurityA.getText());
				pst.setString(5, registerSSN.getText());
				pst.setString(6, registerFirst.getText());
				pst.setString(7, registerLast.getText());
				pst.setString(8, registerAddress.getText());
				pst.setString(9, registerCity.getText());
				pst.setString(10, registerZip.getText());
				pst.setString(11, registerState.getText());
				pst.executeUpdate();

				ReturnToLogin(event);

			} else {
				registerStatus.setText("Password Does Not Meet Requirements");
			}
		}

		catch (Exception e) {
			System.out.println(e);
		}

	}

	public void RegisterLoginButton(ActionEvent event) {
		ReturnToLogin(event);
	}
	
	public void ReturnToLogin(ActionEvent returnToLogin) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
			Stage stage = (Stage)((Node)returnToLogin.getSource()).getScene().getWindow();
			Scene scene = new Scene(root, 600, 400);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
		  }
	}

}


