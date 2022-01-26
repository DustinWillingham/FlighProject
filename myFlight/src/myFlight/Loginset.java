package myFlight;

public class Loginset {
	
	private String userEmail;

	public Loginset() {
		
	}
	
	public Loginset(String userEmail){
		this.userEmail = userEmail;
	}
	
	public String getUserEmail() {
		return userEmail;
	}
	
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
}
