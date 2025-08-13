package ris.showReceipt.model.dto;

import java.util.ArrayList;

public class UpdateDto extends BaseDto {

	private ArrayList<String> kenchu_array;
	private ArrayList<String> ukezumi_array;
	private ArrayList<String> ukezumiFuzai_array;
	private ArrayList<String> junbichu_array;

	public ArrayList<String> getKenchu_array() {
		return kenchu_array;
	}
	public void setKenchu_array(ArrayList<String> kenchu_array) {
		this.kenchu_array = kenchu_array;
	}
	public ArrayList<String> getUkezumi_array() {
		return ukezumi_array;
	}
	public void setUkezumi_array(ArrayList<String> ukezumi_array) {
		this.ukezumi_array = ukezumi_array;
	}
	public ArrayList<String> getUkezumiFuzai_array() {
		return ukezumiFuzai_array;
	}
	public void setUkezumiFuzai_array(ArrayList<String> ukezumiFuzai_array) {
		this.ukezumiFuzai_array = ukezumiFuzai_array;
	}
	public ArrayList<String> getJunbichu_array() {
		return junbichu_array;
	}
	public void setJunbichu_array(ArrayList<String> junbichu_array) {
		this.junbichu_array = junbichu_array;
	}
}

