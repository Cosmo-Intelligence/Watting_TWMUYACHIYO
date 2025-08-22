package ris.showReceipt.model.dto;

public class WaittingTelopDto {

	private String ipaddress = "";
	private String telopKbn = "";
	private String telop1 = "";
	private String telop2 = "";
	private String telop3 = "";

	public String getIpaddress() {
		return ipaddress;
	}
	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	public String getTelopKbn() {
		return telopKbn;
	}
	public void setTelopKbn(String telopKbn) {
		this.telopKbn = telopKbn;
	}

	public String getTelop1() {
		return telop1;
	}
	public void setTelop1(String telop1) {
		this.telop1 = telop1;
	}
	
	public String getTelop2() {
		return telop2;
	}
	public void setTelop2(String telop2) {
		this.telop2 = telop2;
	}
	
	public String getTelop3() {
		return telop3;
	}
	public void setTelop3(String telop3) {
		this.telop3 = telop3;
	}
}
