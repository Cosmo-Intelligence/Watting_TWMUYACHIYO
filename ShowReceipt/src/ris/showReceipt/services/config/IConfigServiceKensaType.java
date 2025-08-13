package ris.showReceipt.services.config;

import java.util.HashMap;

import ris.showReceipt.common.Config;

public interface IConfigServiceKensaType {
	
	public Config load(HashMap<String,String> map, Config config) throws Exception;
	
	public String getKensaType();
}
