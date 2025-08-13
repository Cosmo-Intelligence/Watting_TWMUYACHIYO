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
import ris.showReceipt.services.config.ConfigServiceCR;
import ris.showReceipt.services.config.IConfigServiceKensaType;

public class SettingServiceCR implements ISettingServiceKensaType{
	
	private ConfigServiceCR configServiceCR = new ConfigServiceCR();
	
	private static Logger logger = LogManager.getLogger(SettingServiceCR.class);
	
	/**
	 * 処理実行
	 * @param request httpリクエスト
	 * @param dto マスタ情報
	 * @param config 設定情報
	 * @return
	 */
	@Override
	public boolean Execute(
			HttpServletRequest request,
			SettingDto dto,
			Config config) throws Exception {

		try {

			// 検査室名称取得失敗
			if (StringUtils.isEmpty(config.getKensasitu_name())) {
				dto.setResult(Const.RESULT_NG);
				dto.setErrlevel(Const.ERRLEVEL_ERROR);
				dto.setMsg("検査室名称が未設定です。");
				return false;
			}

			// 検査室名称配列
			String[] kensasituNameArray = config.getKensasitu_name().split(",");
			// 検査室パラメータ配列
			String[] kensasituParamArray = config.getKensasitu_param().split(",");
			// 検査室未割当表示検査種別配列
			String[] miwariateDispKensatypeArray = config.getMiwariatedisp_kensatype().split(",");

			// 検査室名称の件数と、検査室パラメータの件数が一致していない場合は設定不正
			if (kensasituNameArray.length != kensasituParamArray.length) {
				dto.setResult(Const.RESULT_NG);
				dto.setErrlevel(Const.ERRLEVEL_ERROR);
				dto.setMsg("検査室名称の件数と検査室パラメータの件数が一致していません。");
				return false;
			}
			
			// 検査室情報配列
			ArrayList<KensasituInfoDto> kensasituInfoList = null;
			
			try {
				// 検査室情報配列
				kensasituInfoList = createKensasituInfoList(kensasituNameArray, kensasituParamArray, miwariateDispKensatypeArray);
			} catch (Exception ex) {
				dto.setResult(Const.RESULT_NG);
				dto.setErrlevel(Const.ERRLEVEL_ERROR);
				dto.setMsg("検査室名称、検査種別ID、検査室IDの組み合わせが不正です。");
				return false;
			}

			// 検査室名称リストを設定
			dto.setKensasitu_array(Arrays.asList(kensasituNameArray));

			// 検査室情報をセッションへ設定
			SessionControler.setValue(request, SessionControler.KENSASITUINFO, kensasituInfoList);

		} catch (Exception ex) {
			logger.error(ex.toString(), ex);
			dto.setResult(Const.RESULT_NG);
			dto.setErrlevel(Const.ERRLEVEL_ERROR);
			dto.setMsg(ex.getMessage());
			return false;
		}

		return true;
	}

	/**
	 * 検査室情報リスト作成
	 * @param kensasituNameArray 検査室名称配列
	 * @param kensasituParamArray 検査室パラメータ配列
	 * @param miwariateDispKensatypeArray 検査室未割当表示検査種別配列
	 * @return 検査室情報配列
	 */
	private ArrayList<KensasituInfoDto> createKensasituInfoList(
			String[] kensasituNameArray, 
			String[] kensasituParamArray,
			String[] miwariateDispKensatypeArray) throws Exception {
		
		// 検査室情報配列
		ArrayList<KensasituInfoDto> kensasituInfoList = new ArrayList<KensasituInfoDto>();

		// 検査室名称の件数分、検査室情報を作成する
		for (int i = 0; i < kensasituNameArray.length; i++) {

			// 検査室情報
			KensasituInfoDto kensasituInfo = new KensasituInfoDto();

			// 検査室名称を設定
			kensasituInfo.setKensasitu_name(kensasituNameArray[i]);

			// 検査種別配列
			String[] kensatypeArray = kensasituParamArray[i].split("\\|");

			// 検査室パラメータ情報配列
			ArrayList<KensasituInfoDto.KensasituParam> kensasituParamList = new ArrayList<KensasituInfoDto.KensasituParam>();

			// 検査室パラメータ情報の件数分、1枠の検査室パラメータ情報を作成する
			for (int j = 0; j < kensatypeArray.length; j++) {

				// 検査室パラメータ情報
				KensasituInfoDto.KensasituParam kensasituParam = new KensasituInfoDto.KensasituParam();

				// 検査室IDと検査室IDに分割
				String[] param = kensatypeArray[j].split(":");

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

			// 検査室パラメータ情報を設定
			kensasituInfo.setKensasitu_param_list(kensasituParamList);
			// 検査室情報を追加
			kensasituInfoList.add(kensasituInfo);
		}

		return kensasituInfoList;
	}

	@Override
	public IConfigServiceKensaType getConfigKensaType() {
		return this.configServiceCR;
	}
}
