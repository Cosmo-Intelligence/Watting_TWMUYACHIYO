package ris.showReceipt.services.update;

import java.sql.Connection;
import java.util.ArrayList;

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

public class UpdateServiceCR {
	
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
			ArrayList<KensasituInfoDto> kensasituInfoList = null;

			try{
				// セッション取得
				config = (Config)SessionControler.getValue(request, SessionControler.SYSTEMCONFIG);
				kensasituInfoList = (ArrayList<KensasituInfoDto>)SessionControler.getValue(request, SessionControler.KENSASITUINFO);
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

			// 検査室情報取得判定
			if (kensasituInfoList == null) {
				dto.setResult(Const.RESULT_NG);
				dto.setErrlevel(Const.ERRLEVEL_ERROR);
				dto.setMsg("検査室情報が取得できませんでした。");
				return false;
			}

			try {
				// 受付番号の桁数
				int digit = Integer.parseInt(config.getReceipt_number_digit());
				// 受付番号を補完する文字
				String comp = config.getComplement_char();
				// 本日検査日検索フラグ
				String todayFlg = config.getTodayFlg();

				ArrayList<String> kenchuList = new ArrayList<String>();

				for (int i = 0; i < kensasituInfoList.size(); i++) {

					KensasituInfoDto kensasituInfoDto = kensasituInfoList.get(i);

					DataTable kenchuDt = null;

					// 検中の受付番号取得
					kenchuDt = DataBase.getKenchu(kensasituInfoDto.getKensasitu_param_list(), todayFlg, risconn);

					if (kenchuDt.getRowCount() > 0) {

						// 検中の受付番号設定
						//kenchuList.add(kenchuDt.getRows().get(0).get("RECEIPTNUMBER").toString());
						kenchuList.add(getReceiptNumber(kenchuDt.getRows().get(0)));
					} else {
						kenchuList.add("");
					}
				}

				// 検中の受付番号補完
				kenchuList = complementReceiptNumber(kenchuList, comp, digit);

				// 検中の受付番号設定
				dto.setKenchu_array(kenchuList);

				DataTable ukezumiDt = null;

				// 受済の受付番号取得
				ukezumiDt = DataBase.getUkezumi(kensasituInfoList, todayFlg, risconn);
				
				// 受済（不在以外）のリストを作成
				ArrayList<String> ukezumiList = getUkezumiList(ukezumiDt);
				
				// 受済（不在以外）の受付番号補完
				ukezumiList = complementReceiptNumber(ukezumiList, comp, digit);

				// 受済（不在以外）の受付番号設定
				dto.setUkezumi_array(ukezumiList);
				
				// 受済（不在）のリストを作成
				ArrayList<String> ukezumiFuzaiList = getUkezumiFuzaiList(ukezumiDt);
				
				// 受済（不在）の受付番号補完
				ukezumiFuzaiList = complementReceiptNumber(ukezumiFuzaiList, comp, digit);
				
				// 受済（不在）の受付番号設定
				dto.setUkezumiFuzai_array(ukezumiFuzaiList);

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
	 * 受済で不在以外の受付番号リスト取得
	 * @param dt :DBから取得した受済（不在以外）の受付番号
	 * @return
	 */
	private ArrayList<String> getUkezumiList(DataTable dt) throws Exception {

		// 検査室_略称リスト情報
		ArrayList<String> result = new ArrayList<String>();

		for (int i = 0; i < dt.getRowCount(); i++) {
			
			DataRow dr = dt.getRows().get(i);
			
			// 不在は読み飛ばす
			if (dr.get("YOBIDASI_STATUS").toString().equals("3")) {
				continue;
			}

			// 受付番号設定
			result.add(getReceiptNumber(dr));
		}

		return result;
	}
	
	/**
	 * 受済で不在の受付番号リスト取得
	 * @param dt :DBから取得した受済（不在）の受付番号
	 * @return
	 */
	private ArrayList<String> getUkezumiFuzaiList(DataTable dt) throws Exception {

		// 検査室_略称リスト情報
		ArrayList<String> result = new ArrayList<String>();

		for (int i = 0; i < dt.getRowCount(); i++) {
			
			DataRow dr = dt.getRows().get(i);
			
			// 不在は読み飛ばす
			if (!dr.get("YOBIDASI_STATUS").toString().equals("3")) {
				continue;
			}

			// 受付番号設定
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
		//2025.08.13 Mod R.Takahashi@Cosmo 受付番号 表示制御変更 start
		//外来の場合
		/*if(nyugaikbn.equals("1"))
		{
			receiptNumber = dr.get("RECEIPTNUMBER").toString();
		}
		//入院の場合は通常の受付番号
		else if(nyugaikbn.equals("2"))
		{*/
			receiptNumber = dr.get("EM_RECEIPTNUMBER").toString();
		//}

		//2024.03.13 Mod K.Kasama@Cosmo 表示番号条件変更 end
		//2025.08.13 Mod R.Takahashi@Cosmo 受付番号 表示制御変更 end

		return receiptNumber;
		
	}
}
