package ris.showReceipt.model.dto;

import java.util.ArrayList;

public class KensasituInfoDto {

	private String kensasitu_name = "";

	private ArrayList<KensasituParam> kensasitu_param_list;

	public String getKensasitu_name() {
		return kensasitu_name;
	}

	public void setKensasitu_name(String kensasitu_name) {
		this.kensasitu_name = kensasitu_name;
	}

	public ArrayList<KensasituParam> getKensasitu_param_list() {
		return kensasitu_param_list;
	}

	public void setKensasitu_param_list(ArrayList<KensasituParam> kensasitu_param_list) {
		this.kensasitu_param_list = kensasitu_param_list;
	}

	public static class KensasituParam {
		private String kensatypeId = "";
		private String kensasituId = "";
		private String miwariateDispFlg = "0";

		public String getKensatypeId() {
			return kensatypeId;
		}

		public void setKensatypeId(String kensatypeId) {
			this.kensatypeId = kensatypeId;
		}

		public String getKensasituId() {
			return kensasituId;
		}

		public void setKensasituId(String kensasituId) {
			this.kensasituId = kensasituId;
		}

		public String getMiwariateDispFlg() {
			return miwariateDispFlg;
		}

		public void setMiwariateDispFlg(String miwariateDispFlg) {
			this.miwariateDispFlg = miwariateDispFlg;
		}
	}
}
