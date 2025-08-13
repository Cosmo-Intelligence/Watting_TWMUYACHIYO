package ris.showReceipt.services.config;

import java.util.HashMap;

import ris.showReceipt.common.Config;

public class ConfigServiceMRI implements IConfigServiceKensaType {
	
	public final String kensaType = "MRI";

	@Override
	public Config load(HashMap<String, String> map, Config config) throws Exception {

		config.setKensasitu_param(map.get("KensaSituParam"));
		config.setReload_sec(map.get("ReloadSec"));
		config.setKenchu_msg(map.get("KenchuMsg"));
		config.setJunbichu_msg(map.get("JunbichuMsg"));
		config.setUkezumi_msg(map.get("UkezumiMsg"));
		config.setReceipt_number_digit(map.get("ReceiptNumberDigit"));
		config.setComplement_char(map.get("ComplementChar"));
		config.setTodayFlg(map.get("TodayFlg"));
		config.setMiwariatedisp_kensatype(map.get("MiwariateDispKensaType"));
		config.setTelop_msg(map.get("TelopMsg"));
		config.setKenzumi_elapsed_Time(map.get("KenzumiElapsedTime"));
		
		return config;
	}
	
	@Override
	public String getKensaType() {

		return this.kensaType;
	}
}
