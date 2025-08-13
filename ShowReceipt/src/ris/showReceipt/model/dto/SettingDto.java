package ris.showReceipt.model.dto;

import java.util.List;

public class SettingDto extends BaseDto {

	private int reload_sec;
	private String kenchu_msg;
	private String Junbichu_msg;
	private String ukezumi_msg;
	private String telop_msg;
	private List<String> kensasitu_array;

	public int getReload_sec() {
		return reload_sec;
	}
	public void setReload_sec(int reload_sec) {
		this.reload_sec = reload_sec;
	}
	public String getKenchu_msg() {
		return kenchu_msg;
	}
	public void setKenchu_msg(String kenchu_msg) {
		this.kenchu_msg = kenchu_msg;
	}
	public String getJunbichu_msg() {
		return Junbichu_msg;
	}
	public void setJunbichu_msg(String junbichu_msg) {
		Junbichu_msg = junbichu_msg;
	}
	public String getUkezumi_msg() {
		return ukezumi_msg;
	}
	public void setUkezumi_msg(String ukezumi_msg) {
		this.ukezumi_msg = ukezumi_msg;
	}
	public List<String> getKensasitu_array() {
		return kensasitu_array;
	}
	public void setKensasitu_array(List<String> kensasitu_array) {
		this.kensasitu_array = kensasitu_array;
	}
	public String getTelop_msg() {
		return telop_msg;
	}
	public void setTelop_msg(String telop_msg) {
		this.telop_msg = telop_msg;
	}
}

