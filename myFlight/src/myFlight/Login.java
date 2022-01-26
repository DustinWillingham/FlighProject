package myFlight;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;

public class Login extends myFlightMain{
	
	@FXML
	private TextField UserPassword;
	
	@FXML
	public Label loginLbl;
	
	public String enteredPassword;

	//When Login is pressed gets the entered email and password, Sends them to check Login, also authenticates if admin.
	public void Login(ActionEvent event) {	
		String enteredEmail = UserEmail.getText();
		String enteredPassword = UserPassword.getText();
		VerifyLogin(enteredEmail, enteredPassword);
		BookFlightsController callClass = new BookFlightsController();
		callClass.usersEmail(enteredEmail);
		UserPassword.setText("");
		UserEmail.setText("");
	}
	

	
	

	//Changes to register scene using Register button on Login screen
	public void loginRegisterButton(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("Register.fxml"));
			Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root, 600, 400);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
		  }
	}
	
	public void forgotPasswordButton(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("ForgotPassword.fxml"));
			Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root, 600, 400);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
		  }
	}
	
	public void VerifyLogin(String enteredEmail, String enteredPassword) {
		try {
			Connection conn = DBConnector.getConnection();
			//queries the database and gets the password from the Login table
			PreparedStatement CheckLogin = (PreparedStatement) conn
					.prepareStatement("SELECT Password FROM Login WHERE CustomerEmail ='" + enteredEmail + "' ");
			//queries the database and checks the admin table for a yes or no
			PreparedStatement adminCheck = (PreparedStatement) conn
					.prepareStatement("SELECT Admin FROM Login WHERE CustomerEmail ='" + enteredEmail + "' ");
			ResultSet rs = CheckLogin.executeQuery();
			ResultSet res = adminCheck.executeQuery();
						
			if (res.next() && rs.next()) {	
				String admin = res.getString("Admin");
				String psw = rs.getString("Password");
					
				//if username and password match and admin is set to yes, sends to Admin UI
				if (admin.equals("Yes") && psw.equals(enteredPassword)) {
					AdminLoginComplete();
				}
				
				//if username and password match and admin is set to no, it sends user to Customer UI
				else if (psw.equals(enteredPassword) && admin.equals("No")) {
					LoginComplete();
				} 
				
			} 
		} catch (Exception e) {
		  }
	}
	
	//Loads Admin UI
	public void AdminLoginComplete() {
		try {
			Stage login = new Stage();
			Parent root1 = FXMLLoader.load(getClass().getResource("AdminInterface.fxml"));
			Scene returnToLogin = new Scene(root1, 800, 600);
			returnToLogin.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			login.setScene(returnToLogin);
			login.show();
			
		} catch (Exception f) {	
		  }
	}
	
	//Loads Customer UI
	public void LoginComplete() {
		try {
			Stage login = new Stage();
			Parent root = FXMLLoader.load(getClass().getResource("CustomerInterface.fxml"));
			Scene returnToLogin = new Scene(root, 800, 600);
			returnToLogin.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			login.setScene(returnToLogin);
			login.show();
		} catch (Exception f) {

		}
	}
	
	
	//Sets the Login label after register is complete
	public void registerComplete() {
		loginLbl.setText("Registration Successful");
	}
}
