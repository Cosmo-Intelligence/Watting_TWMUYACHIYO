package ris.showReceipt.services.update;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import ris.showReceipt.common.Config;
import ris.showReceipt.common.Const;
import ris.showReceipt.common.SessionControler;
import ris.showReceipt.database.DataBase;
import ris.showReceipt.model.dto.KensasituInfoDto;
import ris.showReceipt.model.dto.UpdateDto;
import ris.showReceipt.util.DataRow;
import ris.showReceipt.util.DataTable;

public class UpdateServiceMRI {
	
	private static Logger logger = LogManager.getLogger(UpdateServiceCR.class);
	
	/**
	 * 処理実行
	 * @param request  :httpリクエスト
	 * @param dto      :マスタ情報
	 * @return
	 */
	public boolean Execute(
			HttpServletRequest request,
			UpdateDto dto,
			ServletContext context) throws Exception {

		try {
			// コネクション取得
			Connection risconn = DataBase.getRisConnection();

			// コネクション取得判定
			if (risconn == null) {
				dto.setResult(Const.RESULT_NG);
				dto.setErrlevel(Const.ERRLEVEL_ERROR);
				dto.setMsg("データベースへ接続できませんでした。");
				return false;
			}

			Config config = null;
			KensasituInfoDto kensasituInfoDto = null;

			try{
				// セッション取得
				config = (Config)SessionControler.getValue(request, SessionControler.SYSTEMCONFIG);
				kensasituInfoDto = (KensasituInfoDto)SessionControler.getValue(request, SessionControler.KENSASITUINFO);
			} catch (Exception ex) {
				dto.setResult(Const.RESULT_NG);
				dto.setErrlevel(Const.ERRLEVEL_ERROR);
				dto.setMsg("セッション情報が取得できませんでした。");
				return false;
			}

			// 設定ファイル情報取得判定
			if (config == null) {
				dto.setResult(Const.RESULT_NG);
				dto.setErrlevel(Const.ERRLEVEL_ERROR);
				dto.setMsg("システム設定情報が取得できませんでした。");
				return false;
			}

			try {
				
				// 受付番号の桁数
				int digit = Integer.parseInt(config.getReceipt_number_digit());
				// 受付番号を補完する文字
				String comp = config.getComplement_char();
				// 本日検査日検索フラグ
				String todayFlg = config.getTodayFlg();
				
				// 検中の受付番号設定 //////////
				
				// 検中の受付番号取得
				DataTable kenchuDt = DataBase.getKenchuMri(kensasituInfoDto.getKensasitu_param_list(), config.getKenzumi_elapsed_Time(), todayFlg, risconn);
				
				// 検中のリストを作成
				ArrayList<String> kenchuList = getNumberList(kenchuDt);
				
				// 検中の受付番号補完
				kenchuList = complementReceiptNumber(kenchuList, comp, digit);
				
				// 検中の受付番号設定
				dto.setKenchu_array(kenchuList);
				
				// 準備中の受付番号設定 //////////

				// 準備中の受付番号取得
				DataTable junbichuDt = DataBase.getJunbichu(kensasituInfoDto.getKensasitu_param_list(), todayFlg, risconn);
				
				// 準備中のリストを作成
				ArrayList<String> junbichuList = getNumberList(junbichuDt);
				
				// 準備中の受付番号補完
				junbichuList = complementReceiptNumber(junbichuList, comp, digit);
				
				// 準備中の受付番号設定
				dto.setJunbichu_array(junbichuList);
				
				// 受済の受付番号設定 //////////
				
				// 受済の受付番号取得
				DataTable ukezumiDt = DataBase.getUkezumiMri(Arrays.asList(kensasituInfoDto), todayFlg, risconn);
				
				// 受済のリストを作成
				ArrayList<String> ukezumiList = getNumberList(ukezumiDt);
				
				// 受済の受付番号補完
				ukezumiList = complementReceiptNumber(ukezumiList, comp, digit);
				
				// 受済の受付番号設定
				dto.setUkezumi_array(ukezumiList);
				
			} finally {
				DataBase.closeConnection(risconn);
			}

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
	 * 受付番号リスト取得
	 * @param dt :DBから取得した受付番号
	 * @return
	 */
	private ArrayList<String> getNumberList(DataTable dt) throws Exception {

		ArrayList<String> result = new ArrayList<String>();

		for (int i = 0; i < dt.getRowCount(); i++) {
			
			DataRow dr = dt.getRows().get(i);
			
			result.add(getReceiptNumber(dr));
		}

		return result;
	}
	
	/**
	 * 受付番号を補完
	 * @param list
	 * @param comp
	 * @param digit
	 * @return
	 */
	private ArrayList<String> complementReceiptNumber(ArrayList<String> list, String comp, int digit) {

		ArrayList<String> result = new ArrayList<String>();

		for (int i = 0; i < list.size(); i++) {

			if (list.get(i).length() < digit && list.get(i).length() > 0) {
				result.add(padding(list.get(i),digit,comp));
			} else {
				result.add(list.get(i));
			}
		}
		return result;
	}

	/**
	 * 指定の桁数まで指定した文字で補完
	 * @param value
	 * @param digit
	 * @param pad
	 * @return
	 */
	private String padding(String value, int digit, String pad) {

		String result = "";

		for (int i = value.length(); i < digit; i++) {
			result += pad;
		}

		result += value;

		return result;
	}
	
	/**
	 * 患者情報または実績メインの受付番号を取得する
	 * @param dr 行データ
	 * @return 受付番号
	 */
	private String getReceiptNumber(DataRow dr) {

		//2024.03.13 Mod K.Kasama@Cosmo 表示番号条件変更 start
		//String receiptNumber = dr.get("RECEIPTNUMBER").toString();
		/*if (StringUtils.isEmpty(receiptNumber)) {

			receiptNumber = dr.get("EM_RECEIPTNUMBER").toString();
			
			if (StringUtils.isEmpty(receiptNumber)) {
				
				receiptNumber = "";
			}
		}*/

		String receiptNumber = "";
		String nyugaikbn = dr.get("NYUGAIKBN").toString();

		//外来の場合
		if(nyugaikbn.equals("1"))
		{
			receiptNumber = dr.get("RECEIPTNUMBER").toString();
		}
		//入院の場合は通常の受付番号
		else if(nyugaikbn.equals("2"))
		{
			receiptNumber = dr.get("EM_RECEIPTNUMBER").toString();
		}
		//2024.03.13 Mod K.Kasama@Cosmo 表示番号条件変更 end

		return receiptNumber;
		
	}
}
