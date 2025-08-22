package ris.showReceipt.services.telopSetting;

import java.net.InetAddress;
import java.sql.Connection;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import ris.showReceipt.common.Const;
import ris.showReceipt.database.DataBase;
import ris.showReceipt.model.dto.SettingDto;
import ris.showReceipt.services.setting.ISettingServiceKensaType;

public class TelopMsgSettingService {
	
	private ISettingServiceKensaType settingServiceKensaType;
	
	private static Logger logger = LogManager.getLogger(TelopMsgSettingService.class);
	
	public TelopMsgSettingService(ISettingServiceKensaType settingServiceKensaType) {
		
		this.settingServiceKensaType = settingServiceKensaType;
	}
	
	/**
	 * 処理実行
	 * @param request  :httpリクエスト
	 * @param dto      :マスタ情報
	 * @return
	 */
	public boolean Execute(
			HttpServletRequest request,
			SettingDto dto,
			ServletContext context,
			String telopMsg1,
			String telopMsg2,
			String telopMsg3,
			String telopMsgFlg) throws Exception {

		boolean result = true;
		
		try {

			// IPアドレス取得
			//String ipAddr = request.getRemoteAddr();
			InetAddress localhost = InetAddress.getLocalHost();
			String ipAddr = localhost.getHostAddress();
			
			//2025.08.20 Add Takahashi@COSMO start テロップメッセージ：メンテナンス対応
			// コネクション取得
			Connection risconn = DataBase.getRisConnection();

			// コネクション取得判定
			if (risconn == null) {
				dto.setResult(Const.RESULT_NG);
				dto.setErrlevel(Const.ERRLEVEL_ERROR);
				dto.setMsg("データベースへ接続できませんでした。");
				return false;
			}
			
			try {
				if(!(StringUtils.isEmpty(telopMsg1) && StringUtils.isEmpty(telopMsg2) && StringUtils.isEmpty(telopMsg3))) {
					// テロップメッセージ更新
					result = DataBase.telopMsgUpdate(telopMsg1, telopMsg2, telopMsg3, ipAddr, risconn);
					// テロップメッセージ更新失敗時
					if (!result) {
						// ロールバック
						risconn.rollback();
		
						dto.setResult(Const.RESULT_NG);
						dto.setErrlevel(Const.ERRLEVEL_WARN);
						dto.setMsg("テロップメッセージが更新できませんでした。");
						return false;
					}
					
					// コミット
					risconn.commit();
				}
				
				if(!(StringUtils.isEmpty(telopMsgFlg))) {
					// テロップメッセージ更新
					result = DataBase.telopMsgFlgUpdate(telopMsgFlg, ipAddr, risconn);
					// テロップメッセージ更新失敗時
					if (!result) {
						// ロールバック
						risconn.rollback();
		
						dto.setResult(Const.RESULT_NG);
						dto.setErrlevel(Const.ERRLEVEL_WARN);
						dto.setMsg("テロップメッセージ区分が更新できませんでした。");
						return false;
					}
					
					// コミット
					risconn.commit();
				}
			}finally {
				DataBase.closeConnection(risconn);
			}	
			
			//2025.08.20 Add Takahashi@COSMO end テロップメッセージ：メンテナンス対応
		
		} catch (Exception ex) {
			logger.error(ex.toString(), ex);
			dto.setResult(Const.RESULT_NG);
			dto.setErrlevel(Const.ERRLEVEL_ERROR);
			dto.setMsg(ex.getMessage());
			return false;
		}

		return result;
	}
}
