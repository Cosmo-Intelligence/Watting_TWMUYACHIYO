package ris.showReceipt.services.config;

import java.util.HashMap;

import ris.showReceipt.common.Config;

public class ConfigServiceCR implements IConfigServiceKensaType{
	
	public final String kensaType = "CR";
	
	@Override
	public Config load(HashMap<String, String> map, Config config) throws Exception {
		
		config.setKensasitu_name(map.get("KensaSituName"));
		config.setKensasitu_param(map.get("KensaSituParam"));
		config.setReload_sec(map.get("ReloadSec"));
		config.setKenchu_msg(map.get("KenchuMsg"));
		config.setUkezumi_msg(map.get("UkezumiMsg"));
		config.setReceipt_number_digit(map.get("ReceiptNumberDigit"));
		config.setComplement_char(map.get("ComplementChar"));
		config.setTodayFlg(map.get("TodayFlg"));
		config.setMiwariatedisp_kensatype(map.get("MiwariateDispKensaType"));
		//2025.08.20 Del Takahashi@COSMO start テロップメッセージ：メンテナンス対応
		//config.setTelop_msg(map.get("TelopMsg"));
		//2025.08.20 Del Takahashi@COSMO start テロップメッセージ：メンテナンス対応
		return config;
	}

	@Override
	public String getKensaType() {

		return this.kensaType;
	}
}
