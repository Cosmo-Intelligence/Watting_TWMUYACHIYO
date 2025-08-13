package ris.showReceipt.common;

import java.math.BigDecimal;
import java.sql.Timestamp;

//import ris.lib.core.util.CommonString;

public class Const {

	/**
	 * 処理結果 "OK"
	 */
	public static final String RESULT_OK = "OK";
	/**
	 * 処理結果 "NG"
	 */
	public static final String RESULT_NG = "NG";

	/**
	 * エラーレベル "WARN"
	 */
	public static final String ERRLEVEL_WARN = "WARN";
	/**
	 * エラーレベル "ERROR"
	 */
	public static final String ERRLEVEL_ERROR = "ERROR";

	/**
	 * RISDBリソース
	 */
	public static final String DBNAME_RIS = "java:comp/env/jdbc/oracle/rris";

	/**
	 * 他DBリソース
	 */
	public static final String DBNAME_DUMMY = "java:comp/env/jdbc/oracle/dummy";

	/**
	 * 設定ファイルパス（ディレクトリ）
	 */
	public static final String CONFIG_DIRECTORY = "/WEB-INF/";

	/**
	 * 設定ファイルパス（拡張子）
	 */
	public static final String CONFIG_FILE = ".config.xml";

	/**
	 * かなローマ変換用テキストファイルパス
	 */
	public static final String KANAROMATEXT = "/WEB-INF/KanaRomaList.txt";

	/**
	 *  検査ステータス
	 */
	/*
	public static final String STATUS_UNREGISTERED     = CommonString.STATUS_UNREGISTERED;     // 未受付
	public static final String STATUS_ISLATE           = CommonString.STATUS_ISLATE;           // 遅刻
	public static final String STATUS_ISCALLING        = CommonString.STATUS_ISCALLING;        // 呼出中
	public static final String STATUS_ISREGISTERED     = CommonString.STATUS_ISREGISTERED;     // 受付済
	public static final String STATUS_INOPERATION      = CommonString.STATUS_INOPERATION;      // 実施中
	public static final String STATUS_REST             = CommonString.STATUS_REST;             // 保留
	public static final String STATUS_RECALLING        = CommonString.STATUS_RECALLING;        // 再呼出
	public static final String STATUS_REREGISTERED     = CommonString.STATUS_REREGISTERED;     // 再受付
	public static final String STATUS_ISFINISHED       = CommonString.STATUS_ISFINISHED;       // 実施済
	public static final String STATUS_STOP             = CommonString.STATUS_STOP;             // 中止
	public static final String STATUS_DELETE           = CommonString.STATUS_DELETE;           // 削除
	public static final String STATUS_DELETE_SAVEPOINT = CommonString.STATUS_DELETE_SAVEPOINT; // 削除ステータス検索条件保存位置(100桁目)
*/

	/**
	 *  オーダ一覧 ソート条件
	 */
	public static final String ORDERLIST_SORT_KANASIMEI = "0"; // 名前順
	public static final String ORDERLIST_SORT_BYOUTOU   = "1"; // 病棟順

	/**
	 * MPPS取得設定値
	 */
	public static final String MPPS_GET_OFF = "0"; // MPPS取得しない
	public static final String MPPS_GET_ON  = "1"; // MPPS取得する

	/**
	 *  改行コード
	 */
	public static final String LINE_FEED = "\n";

	/**
	 *  部位情報 区切り文字
	 */
	public static final String SLASH = "／";

	/**
	 *  timestamp型最小値
	 */
	public static Timestamp TIMESTAMP_MINVALUE = Timestamp.valueOf("0001-01-01 00:00:00");

	/**
	 *  int型最小値
	 */
	public static Integer INT_MINVALUE = Integer.MIN_VALUE;

	/**
	 *  decimal型最小値
	 */
	public static BigDecimal DECIMAL_MINVALUE = BigDecimal.valueOf(new Double("-79228162514264337593543950335"));

	/**
	 *  double型最小値
	 */
	public static double DOUBLE_MINVALUE = Double.MIN_VALUE;

	/**
	 *  RISDBユーザ名
	 */
	public static final String RISUSER = "RRIS";
}
