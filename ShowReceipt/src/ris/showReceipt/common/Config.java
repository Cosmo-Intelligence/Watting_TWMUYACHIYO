package ris.showReceipt.common;

import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import jakarta.servlet.ServletContext;
import ris.showReceipt.services.config.IConfigServiceKensaType;
import ris.showReceipt.util.Util;

public class Config implements Serializable {
	
	private IConfigServiceKensaType configService;

	private static final long serialVersionUID = 1L;

	private static Logger logger = LogManager.getLogger(Config.class);

	private String reload_sec;
	private String kenchu_msg;
	private String junbichu_msg;
	private String ukezumi_msg;
	private String kensasitu_name;
	private String kensasitu_param;
	private String receipt_number_digit;
	private String complement_char;
	private String todayFlg;
	private String miwariatedisp_kensatype;
	private String telop_msg;
	private String kenzumi_elapsed_Time;
	//2025.08.20 Mod Takahashi@COSMO start テロップメッセージ：メンテナンス対応
	private String telop_flg;
	
	
	public Config(IConfigServiceKensaType configService) {
		this.configService = configService;
	}
	
	public Config() {
	}
	//2025.08.20 Mod Takahashi@COSMO end テロップメッセージ：メンテナンス対応

	public String getReload_sec() {
		return reload_sec;
	}

	public void setReload_sec(String reload_sec) {
		this.reload_sec = reload_sec;
	}

	public String getKenchu_msg() {
		return kenchu_msg;
	}

	public void setKenchu_msg(String kenchu_msg) {
		this.kenchu_msg = kenchu_msg;
	}

	public String getJunbichu_msg() {
		return junbichu_msg;
	}

	public void setJunbichu_msg(String junbichu_msg) {
		this.junbichu_msg = junbichu_msg;
	}

	public String getUkezumi_msg() {
		return ukezumi_msg;
	}

	public void setUkezumi_msg(String ukezumi_msg) {
		this.ukezumi_msg = ukezumi_msg;
	}

	public String getKensasitu_name() {
		return kensasitu_name;
	}

	public void setKensasitu_name(String kensasitu_name) {
		this.kensasitu_name = kensasitu_name;
	}

	public String getKensasitu_param() {
		return kensasitu_param;
	}

	public void setKensasitu_param(String kensasitu_param) {
		this.kensasitu_param = kensasitu_param;
	}

	public String getReceipt_number_digit() {
		return receipt_number_digit;
	}

	public void setReceipt_number_digit(String receipt_number_digit) {
		this.receipt_number_digit = receipt_number_digit;
	}

	public String getComplement_char() {
		return complement_char;
	}

	public void setComplement_char(String complement_char) {
		this.complement_char = complement_char;
	}

	public String getTodayFlg() {
		return todayFlg;
	}

	public void setTodayFlg(String todayFlg) {
		this.todayFlg = todayFlg;
	}

	public String getMiwariatedisp_kensatype() {
		return miwariatedisp_kensatype;
	}

	public void setMiwariatedisp_kensatype(String miwariatedisp_kensatype) {
		this.miwariatedisp_kensatype = miwariatedisp_kensatype;
	}

	public String getTelop_msg() {
		return telop_msg;
	}

	public void setTelop_msg(String telop_msg) {
		this.telop_msg = telop_msg;
	}

	public String getKenzumi_elapsed_Time() {
		return kenzumi_elapsed_Time;
	}

	public void setKenzumi_elapsed_Time(String kenzumi_elapsed_Time) {
		this.kenzumi_elapsed_Time = kenzumi_elapsed_Time;
	}
	
	public String getTelop_flg() {
		return telop_flg;
	}

	public void setTelop_flg(String telop_flg) {
		this.telop_flg = telop_flg;
	}

	/**
	 * 設定ファイル情報取得
	 * @param ctx
	 * @return
	 */
	public Config getConfig(ServletContext ctx, String ipAddr) throws Exception {

 		String file = Const.CONFIG_DIRECTORY + ipAddr+ "_" + configService.getKensaType() +  Const.CONFIG_FILE;

 		HashMap<String,String> map = new HashMap<String,String>();
 		
		try {

			URL url = ctx.getResource(file);
			logger.debug(file + " = " + url);

			InputStream stream = ctx.getResourceAsStream(file);

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(stream);
			Element root = doc.getDocumentElement();

			for (int i = 0; i < root.getChildNodes().getLength(); i++) {
				logger.debug(root.getChildNodes().item(i).getNodeName() + ":" + root.getChildNodes().item(i).getTextContent());
				map.put(root.getChildNodes().item(i).getNodeName(), Util.toNullString(root.getChildNodes().item(i).getTextContent()));
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
		
		// 検査種別ごとの設定を読み込む
 		Config config = configService.load(map, this);

		return config;
	}
	
	/**
	 * 検査種別ID、検査室ID取得
	 * @param kensasituInfo
	 * @return
	 */
	public static String[][] spliter(String info) throws Exception{

		// 検査種別ID:検査室ID値取得
		String[] array1 = info.split(",");

		String[][] retArray = new String[array1[0].split(":").length][array1.length];

		for (int i = 0; i < array1.length; i++) {
			// 検査種別ID・検査室ID分割
			String[] array2 = array1[i].split(":");

			for (int j = 0; j < array2.length; j++) {
				retArray[j][i] = array2[j];
			}
		}

		return retArray;
	}

	/**
	 * 検査室未割当フラグ配列の生成
	 * @param kensatype
	 * @param nullFlg
	 * @return
	 */
	public static String[] flgArray(String[] kensatype, String[][] nullFlg) {

		String[] result = new String[kensatype.length];

		for (int i = 0; i < kensatype.length; i++) {
			for (int j = 0; j < nullFlg[0].length; j++) {

				if (kensatype[i].equals(nullFlg[0][j])) {

					result[i] = nullFlg[1][j];
				}
			}

			if (result[i] == null) {

				result[i] = "0";
			}
		}

		return result;
	}
}
