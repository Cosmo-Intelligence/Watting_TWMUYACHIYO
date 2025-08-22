package ris.showReceipt.model.dto;

import java.util.List;

public class SettingDto extends BaseDto {

	private int reload_sec;
	private String kenchu_msg;
	private String Junbichu_msg;
	private String ukezumi_msg;
	//2025.08.20 Mod Takahashi@COSMO start テロップメッセージ：メンテナンス対応
	private List<String> telop_msg;
	private List<String> kensasitu_array;
	private int telop_flg;
	//2025.08.20 Mod Takahashi@COSMO end テロップメッセージ：メンテナンス対応

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
	//2025.08.20 Mod Takahashi@COSMO start テロップメッセージ：メンテナンス対応
	public List<String>  getTelop_msg() {
		return telop_msg;
	}
	public void setTelop_msg(List<String>  telop_msg) {
		this.telop_msg = telop_msg;
	}
	public int getTelop_flg() {
		return telop_flg;
	}
	public void setTelop_flg(int telop_flg) {
		this.telop_flg = telop_flg;
	}
	//2025.08.20 Mod Takahashi@COSMO end テロップメッセージ：メンテナンス対応
}

