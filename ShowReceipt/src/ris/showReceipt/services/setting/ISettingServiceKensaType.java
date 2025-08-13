package ris.showReceipt.services.setting;

import jakarta.servlet.http.HttpServletRequest;
import ris.showReceipt.common.Config;
import ris.showReceipt.model.dto.SettingDto;
import ris.showReceipt.services.config.IConfigServiceKensaType;

public interface ISettingServiceKensaType {

	public boolean Execute(
			HttpServletRequest request,
			SettingDto dto,
			Config config) throws Exception;
	
	public IConfigServiceKensaType getConfigKensaType();
	
}
