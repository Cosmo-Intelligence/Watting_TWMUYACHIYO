package ris.showReceipt.services.setting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.servlet.http.HttpServletRequest;
import ris.showReceipt.common.Config;
import ris.showReceipt.common.Const;
import ris.showReceipt.common.SessionControler;
import ris.showReceipt.model.dto.KensasituInfoDto;
import ris.showReceipt.model.dto.SettingDto;
import ris.showReceipt.services.config.ConfigServiceMRI;
import ris.showReceipt.services.config.IConfigServiceKensaType;

public class SettingServiceMRI implements ISettingServiceKensaType {
	
	private IConfigServiceKensaType configServiceMRI = new ConfigServiceMRI();
	
	private static Logger logger = LogManager.getLogger(SettingServiceMRI.class);
	
	/**
	 * 処理実行
	 * @param request httpリクエスト
	 * @param dto マスタ情報
	 * @param config 設定情報
	 * @return
	 */
	public boolean Execute(
			HttpServletRequest request,
			SettingDto dto,
			Config config) throws Exception {
		
		try {

			// 準備中メッセージ取得失敗
			if (StringUtils.isEmpty(config.getJunbichu_msg())) {
				dto.setResult(Const.RESULT_NG);
				dto.setErrlevel(Const.ERRLEVEL_ERROR);
				dto.setMsg("準備中メッセージが未設定です。");
			}

			// 準備中メッセージ設定
			dto.setJunbichu_msg(config.getJunbichu_msg());
			
			// 検済対象外遅延時間取得失敗
			if (StringUtils.isEmpty(config.getKenzumi_elapsed_Time())) {
				dto.setResult(Const.RESULT_NG);
				dto.setErrlevel(Const.ERRLEVEL_ERROR);
				dto.setMsg("検済対象外遅延時間が未設定です。");
				return false;
			}
			
			// 検査室パラメータ配列
			String[] kensasituParamArray = config.getKensasitu_param().split(",");
			// 検査室未割当表示検査種別配列
			String[] miwariateDispKensatypeArray = config.getMiwariatedisp_kensatype().split(",");
			// 検査室パラメータ情報配列
			ArrayList<KensasituInfoDto.KensasituParam> kensasituParamList = new ArrayList<KensasituInfoDto.KensasituParam>();
			
			// 検査室パラメータ情報の件数分、1枠の検査室パラメータ情報を作成する
			for (int j = 0; j < kensasituParamArray.length; j++) {

				// 検査室パラメータ情報
				KensasituInfoDto.KensasituParam kensasituParam = new KensasituInfoDto.KensasituParam();

				// 検査室IDと検査室IDに分割
				String[] param = kensasituParamArray[j].split(":");

				// 検査種別IDを設定
				kensasituParam.setKensatypeId(param[0]);
				// 検査室IDを設定
				kensasituParam.setKensasituId(param[1]);

				// 検査種別IDがリストに存在する場合、未割当表示とする
				List<String> miwariateDispKensatypeList = Arrays.asList(miwariateDispKensatypeArray);
				if (miwariateDispKensatypeList.contains(param[0])) {
					kensasituParam.setMiwariateDispFlg("1");
				}

				// 検査室パラメータ情報を追加
				kensasituParamList.add(kensasituParam);
			}
			
			// 検査室情報
			KensasituInfoDto kensasituInfo = new KensasituInfoDto();
			// 検査室パラメータ情報を設定
			kensasituInfo.setKensasitu_param_list(kensasituParamList);
			
			// 検査室情報をセッションへ設定
			SessionControler.setValue(request, SessionControler.KENSASITUINFO, kensasituInfo);

		} catch (Exception ex) {
			logger.error(ex.toString(), ex);
			dto.setResult(Const.RESULT_NG);
			dto.setErrlevel(Const.ERRLEVEL_ERROR);
			dto.setMsg(ex.getMessage());
			return false;
		}

		return true;
	}
	
	@Override
	public IConfigServiceKensaType getConfigKensaType() {
		return this.configServiceMRI;
	}
}
