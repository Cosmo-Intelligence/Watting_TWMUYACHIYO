package ris.showReceipt.services.setting;

import java.net.InetAddress;
import java.sql.Connection;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import ris.showReceipt.common.Config;
import ris.showReceipt.common.Const;
import ris.showReceipt.common.SessionControler;
import ris.showReceipt.database.DataBase;
import ris.showReceipt.model.dto.SettingDto;
import ris.showReceipt.model.dto.WaittingTelopDto;
import ris.showReceipt.util.DataTable;

public class SettingService {
	
	private ISettingServiceKensaType settingServiceKensaType;
	
	private static Logger logger = LogManager.getLogger(SettingService.class);

	public SettingService(ISettingServiceKensaType settingServiceKensaType) {
		
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
			ServletContext context) throws Exception {

		boolean result = true;
		
		try {

			// IPアドレス取得
			//String ipAddr = request.getRemoteAddr();
			InetAddress localhost = InetAddress.getLocalHost();
			String ipAddr = localhost.getHostAddress();

			//セッションの破棄
			SessionControler.clearSession(request);

			//セッション作成
			SessionControler.createSession(request);

			//設定ファイル取り込み
			Config config = new Config(settingServiceKensaType.getConfigKensaType()).getConfig(context, ipAddr);
			
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
			
			WaittingTelopDto waittingTelop = null;
			
			try {
				
				DataTable telopDt = DataBase.getWaittingTelop(ipAddr, risconn);
				waittingTelop= setWaittingTelop(telopDt);
			}finally {
				DataBase.closeConnection(risconn);
			}
			//2025.08.20 Add Takahashi@COSMO end テロップメッセージ：メンテナンス対応
			
			// 更新周期取得失敗
			if (StringUtils.isEmpty(config.getReload_sec())) {
				dto.setResult(Const.RESULT_NG);
				dto.setErrlevel(Const.ERRLEVEL_ERROR);
				dto.setMsg("更新周期が未設定です。");
				return false;
			}

			// 更新周期設定
			dto.setReload_sec(Integer.parseInt(config.getReload_sec()));

			// 検中メッセージ取得失敗
			if (StringUtils.isEmpty(config.getKenchu_msg())) {
				dto.setResult(Const.RESULT_NG);
				dto.setErrlevel(Const.ERRLEVEL_ERROR);
				dto.setMsg("検中メッセージが未設定です。");
			}

			// 検中メッセージ設定
			dto.setKenchu_msg(config.getKenchu_msg());

			// 受済メッセージ取得失敗
			if (StringUtils.isEmpty(config.getUkezumi_msg())) {
				dto.setResult(Const.RESULT_NG);
				dto.setErrlevel(Const.ERRLEVEL_ERROR);
				dto.setMsg("受済メッセージが未設定です。");
				return false;
			}

			// 受済メッセージ設定
			dto.setUkezumi_msg(config.getUkezumi_msg());

			// 検査室パラメータ情報取得失敗
			if (StringUtils.isEmpty(config.getKensasitu_param())) {
				dto.setResult(Const.RESULT_NG);
				dto.setErrlevel(Const.ERRLEVEL_ERROR);
				dto.setMsg("検査室パラメータ情報が未設定です。");
				return false;
			}
			
			//2025.08.15 Mod Takahashi@COSMO start テロップメッセージ：メンテナンス対応
			// テロップメッセージ取得失敗
			if (StringUtils.isEmpty(waittingTelop.getTelop1())
				||	StringUtils.isEmpty(waittingTelop.getTelop2())
				||  StringUtils.isEmpty(waittingTelop.getTelop3())) {
				dto.setResult(Const.RESULT_NG);
				dto.setErrlevel(Const.ERRLEVEL_ERROR);
				dto.setMsg("テロップメッセージが未設定です。");
			}
			
			// テロップメッセージ設定
			String[] telopMsgNameArray = {waittingTelop.getTelop1(),waittingTelop.getTelop2(),waittingTelop.getTelop3()};
			dto.setTelop_msg(Arrays.asList(telopMsgNameArray));
			
			
			// テロップメッセージ表示フラグ取得失敗
			if (StringUtils.isEmpty(waittingTelop.getTelopKbn())) {
				dto.setResult(Const.RESULT_NG);
				dto.setErrlevel(Const.ERRLEVEL_ERROR);
				dto.setMsg("テロップメッセージ表示フラグが未設定です。");
			}
			
			// テロップメッセージ表示フラグ設定
			dto.setTelop_flg(Integer.parseInt(waittingTelop.getTelopKbn()));
			//2025.08.15 Mod Takahashi@COSMO end テロップメッセージ：メンテナンス対応

			// 受付番号の桁数取得失敗
			if (StringUtils.isEmpty(config.getReceipt_number_digit())) {
				dto.setResult(Const.RESULT_NG);
				dto.setErrlevel(Const.ERRLEVEL_ERROR);
				dto.setMsg("受付番号の桁数が未設定です。");
				return false;
			}

			// 受付番号を補完する文字取得失敗
			if (StringUtils.isEmpty(config.getComplement_char())) {
				dto.setResult(Const.RESULT_NG);
				dto.setErrlevel(Const.ERRLEVEL_ERROR);
				dto.setMsg("受付番号を補完する文字が未設定です。");
				return false;
			}

			// 本日検査日検索フラグ取得失敗
			if (StringUtils.isEmpty(config.getTodayFlg())) {
				dto.setResult(Const.RESULT_NG);
				dto.setErrlevel(Const.ERRLEVEL_ERROR);
				dto.setMsg("本日検査日検索フラグが未設定です。");
				return false;
			}

			// 本日検査日検索フラグ不正値入力時
			if (!"0".equals(config.getTodayFlg()) && !"1".equals(config.getTodayFlg())) {
				dto.setResult(Const.RESULT_NG);
				dto.setErrlevel(Const.ERRLEVEL_ERROR);
				dto.setMsg("本日検査日検索フラグは、0または1を設定してください。");
				return false;
			}

			// 検査種別固有の処理
			result = settingServiceKensaType.Execute(request, dto, config);
			
			// 設定ファイル情報をセッションへ設定
			SessionControler.setValue(request, SessionControler.SYSTEMCONFIG, config);

		} catch (Exception ex) {
			logger.error(ex.toString(), ex);
			dto.setResult(Const.RESULT_NG);
			dto.setErrlevel(Const.ERRLEVEL_ERROR);
			dto.setMsg(ex.getMessage());
			return false;
		}

		return result;
	}
	
	//2025.08.20 Mod Takahashi@COSMO start テロップメッセージ：メンテナンス対応
	/**
	 * テロップメッセージ情報取得
	 * @param dt     :テロップメッセージ詳細情報
	 * @return
	 * @throws Exception
	 */
	private WaittingTelopDto setWaittingTelop(DataTable dt) throws Exception {

		WaittingTelopDto order = new WaittingTelopDto();

		// オーダ情報設定
		if (dt.getRowCount() >= 1) {
			order.setIpaddress(dt.getRows().get(0).get("IPADDRESS").toString());
			order.setTelopKbn(dt.getRows().get(0).get("TEROPPUKBN").toString());
			order.setTelop1(dt.getRows().get(0).get("TEROPPU1").toString());
			order.setTelop2(dt.getRows().get(0).get("TEROPPU2").toString());
			order.setTelop3(dt.getRows().get(0).get("TEROPPU3").toString());
		}

		return order;
	}
	//2025.08.20 Mod Takahashi@COSMO end テロップメッセージ：メンテナンス対応
}
