package ris.showReceipt.model.dto;

import ris.showReceipt.common.Const;

public class BaseDto {

	private String result = Const.RESULT_OK;
	private String msg = "";
	private String errlevel = "";

	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}

	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getErrlevel() {
		return errlevel;
	}
	public void setErrlevel(String errlevel) {
		this.errlevel = errlevel;
	}
}
