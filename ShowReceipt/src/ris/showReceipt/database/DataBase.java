package ris.showReceipt.database;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import ris.showReceipt.common.Const;
import ris.showReceipt.model.dto.KensasituInfoDto;
import ris.showReceipt.util.DataTable;

public class DataBase extends DataBaseCore {

	/**
	 * RIS接続
	 * @return
	 */
	public static Connection getRisConnection() {
		return getConnection(Const.DBNAME_RIS);
	}

	/**
	 * 他DB接続
	 * @return
	 */
	public static Connection getDummyConnection(String tns, String user, String pass) {
		return getConnection(Const.DBNAME_DUMMY, tns, user, pass);
	}

	/**
	 * 切断
	 * @param conn
	 */
	public static void closeConnection(Connection conn) {

		try{
			conn.close();
		} catch (Exception e) {
			//NULL;
		}
	}

	/**
	 * 検査室_略称取得
	 * @param kensasitu  :検査室ID配列
	 * @param conn       :接続情報
	 * @return
	 * @throws Exception
	 */
	public static DataTable getRyakuName(String kensasitu[], Connection conn) throws Exception {

		DataTable Dat = null;

		String sql = "";
		ArrayList<Object> arglist = new ArrayList<Object>();

		sql += " select";
		sql += "   *";
		sql += " from";

		for(int i = 0; i < kensasitu.length; i++) {

			if(i >= 1){
				sql += "  ,";
			}

			sql += "   (";
			sql += "     select";
			sql += "       EXAMROOM_RYAKUNAME as " + kensasitu[i];
			sql += "     from";
			sql += "       EXAMROOMMASTER";
			sql += "     where";
			sql += "       EXAMROOM_ID = ?";
			sql += "   )";

			arglist.add(kensasitu[i]);
		}

		Object[] args = new Object[arglist.size()];
		arglist.toArray(args);

		Dat = executeQuery(sql, args, conn);

		return Dat;
	}

	/**
	 * 検中の受付番号取得
	 * @param kensasituParamList  :検査室パラメータ情報リスト
	 * @param todayflg            :本日フラグ
	 * @param conn                :接続情報
	 * @return
	 * @throws Exception
	 */
	public static DataTable getKenchu(ArrayList<KensasituInfoDto.KensasituParam> kensasituParamList, String todayflg, Connection conn) throws Exception {

		DataTable Dat = null;

		String sql = "";
		String where = "";
		ArrayList<Object> arglist = new ArrayList<Object>();

		sql += " select";
		sql += "   *";
		sql += " from";
		sql += "   (";
		sql += "     select";
		//2023.09.19 Mod M.Furuya@Cosmo カラム名の変更 start
		sql += "       PATIENTINFO.RECEIPTNUMBER_HIS RECEIPTNUMBER";
		//sql += "       PATIENTINFO.RECEIPTNUMBER＿HIS RECEIPTNUMBER";
		//2023.09.19 Mod M.Furuya@Cosmo カラム名の変更 end
		sql += "       ,EM.EXAMENDDATE EXAMENDDATE";
		sql += "       ,EM.RECEIPTNUMBER EM_RECEIPTNUMBER";
		//2024.03.13 Add K.Kasama@Cosmo 表示番号条件変更 start
		sql += "       ,PATIENTINFO.KANJA_NYUGAIKBN NYUGAIKBN";
		//2024.03.13 Add K.Kasama@Cosmo 表示番号条件変更 end
		sql += "     from";
		sql += "       PATIENTINFO";
		sql += "       ,(";
		sql += "         select";
		sql += "           KANJA_ID";
		sql += "           ,EXAMENDDATE";
		sql += "           ,RECEIPTNUMBER";
		sql += "         from";
		sql += "           EXMAINTABLE";
		sql += "         where";
		sql += "           STATUS = 20";
		// 本日フラグが[1]の場合は、システム日付と同じ検査日を対象とする
		if ("1".equals(todayflg)) {
			sql += "     and";
			sql += "       KENSA_DATE = TO_NUMBER(TO_CHAR(SYSDATE,'yyyymmdd'))";
		}

		sql += "         and";
		sql += "           (";

		String buff = "";
		// 検査室パラメータ情報件数分、処理する
		for (KensasituInfoDto.KensasituParam kensasituParam : kensasituParamList) {

		    if (!"".equals(buff)) {
		    	buff += "           or";
		    }
		    buff += "         (";
			buff += "           (";
			buff += "               KENSATYPE_ID = ?";
			buff += "             and";
			buff += "               KENSASITU_ID = ?";
			buff += "           )";
			buff += "         )";

			arglist.add(kensasituParam.getKensatypeId());
			arglist.add(kensasituParam.getKensasituId());
		}

		sql += buff;
		sql += "           )";

		sql += "         order by";
		sql += "           EXAMSTARTDATE desc";
		sql += "       ) EM";
		sql  += "     where";
		sql  += "       PATIENTINFO.KANJA_ID = EM.KANJA_ID";
		sql  += "   )";
		
		where += " where";
		where += "   ROWNUM <= 99";

		sql += where;

		Object[] args = new Object[arglist.size()];
		arglist.toArray(args);

		Dat = executeQuery(sql, args, conn);

		return Dat;
	}
	
	/**
	 * 検中（MRI）の受付番号取得
	 * @param kensasituParamList  :検査室パラメータ情報リスト
	 * @param todayflg            :本日フラグ
	 * @param conn                :接続情報
	 * @return
	 * @throws Exception
	 */
	public static DataTable getKenchuMri(ArrayList<KensasituInfoDto.KensasituParam> kensasituParamList, String kenzumiElapsedTime, String todayflg, Connection conn) throws Exception {

		DataTable Dat = null;

		String sql = "";
		String where = "";
		ArrayList<Object> arglist = new ArrayList<Object>();

		sql += " select";
		sql += "   *";
		sql += " from";
		sql += "   (";
		sql += "     select";
		//2023.09.19 Mod M.Furuya@Cosmo カラム名の変更 start
		sql += "       PATIENTINFO.RECEIPTNUMBER_HIS RECEIPTNUMBER";
		//sql += "       PATIENTINFO.RECEIPTNUMBER＿HIS RECEIPTNUMBER";
		//2023.09.19 Mod M.Furuya@Cosmo カラム名の変更 end
		//2024.03.13 Add K.Kasama@Cosmo 表示番号条件変更 start
		sql += "       ,PATIENTINFO.KANJA_NYUGAIKBN NYUGAIKBN";
		//2024.03.13 Add K.Kasama@Cosmo 表示番号条件変更 end
		sql += "       ,EM.RECEIPTNUMBER EM_RECEIPTNUMBER";
		sql += "     from";
		sql += "       PATIENTINFO";
		sql += "       ,(";
		sql += "         select";
		sql += "           KANJA_ID";
		sql += "           ,RECEIPTNUMBER";
		sql += "         from";
		sql += "           EXMAINTABLE";
		sql += "         where";
		sql += "             (";
		sql += "               STATUS = 20";
		sql += "               or";
		sql += "                (";
		sql += "                   STATUS = 90";
		sql += "                   and";
		sql += "                   (SYSDATE - ? / 1440) < EXAMENDDATE";
		sql += "                   and";
		sql += "                   EXAMENDDATE < SYSDATE";
		sql += "                )";
		sql += "             )";
		
		arglist.add(kenzumiElapsedTime);
		
		// 本日フラグが[1]の場合は、システム日付と同じ検査日を対象とする
		if ("1".equals(todayflg)) {
			sql += "     and";
			sql += "       KENSA_DATE = TO_NUMBER(TO_CHAR(SYSDATE,'yyyymmdd'))";
		}

		sql += "         and";
		sql += "           (";

		String buff = "";
		// 検査室パラメータ情報件数分、処理する
		for (KensasituInfoDto.KensasituParam kensasituParam : kensasituParamList) {

		    if (!"".equals(buff)) {
		    	buff += "           or";
		    }
		    buff += "         (";
			buff += "           (";
			buff += "               KENSATYPE_ID = ?";
			buff += "             and";
			buff += "               KENSASITU_ID = ?";
			buff += "           )";
			buff += "         )";

			arglist.add(kensasituParam.getKensatypeId());
			arglist.add(kensasituParam.getKensasituId());
		}

		sql += buff;
		sql += "           )";

		sql += "         order by";
		sql += "           EXAMSTARTDATE desc";
		sql += "       ) EM";
		sql  += "     where";
		sql  += "       PATIENTINFO.KANJA_ID = EM.KANJA_ID";
		sql  += "   )";
		
		where += " where";
		where += "   ROWNUM <= 99";

		sql += where;

		Object[] args = new Object[arglist.size()];
		arglist.toArray(args);

		Dat = executeQuery(sql, args, conn);

		return Dat;
	}

	/**
	 * 受済の受付番号取得
	 * @param kensasituInfoDtoList :検査室情報リスト
	 * @param todayflg             :本日フラグ
	 * @param conn                 :接続情報
	 * @return
	 * @throws Exception
	 */
	public static DataTable getUkezumi(List<KensasituInfoDto> kensasituInfoDtoList, String todayflg, Connection conn) throws Exception {

		DataTable Dat = null;

		String sql = "";
		String where = "";
		String orderBy = "";
		ArrayList<Object> arglist = new ArrayList<Object>();

		sql += " select";
		sql += "   *";
		sql += " from";
		sql += "   (";
		sql += "     select";
		//2023.09.19 Mod M.Furuya@Cosmo カラム名の変更 start
		sql += "       PATIENTINFO.RECEIPTNUMBER_HIS RECEIPTNUMBER";
		//sql += "       PATIENTINFO.RECEIPTNUMBER＿HIS RECEIPTNUMBER";
		//2023.09.19 Mod M.Furuya@Cosmo カラム名の変更 end
		//2024.03.13 Add K.Kasama@Cosmo 表示番号条件変更 start
		sql += "       ,PATIENTINFO.KANJA_NYUGAIKBN NYUGAIKBN";
		//2024.03.13 Add K.Kasama@Cosmo 表示番号条件変更 end
		sql += "       ,EM.YOBIDASI_STATUS YOBIDASI_STATUS";
		sql += "       ,EM.RECEIPTDATE RECEIPTDATE";
		sql += "       ,EM.RECEIPTNUMBER EM_RECEIPTNUMBER";
		sql += "     from";
		sql += "       PATIENTINFO";
		sql += "       ,(";
		sql += "           select";
		sql += "             KANJA_ID";
		sql += "             ,YOBIDASI_STATUS";
		sql += "             ,RECEIPTDATE";
		sql += "             ,RECEIPTNUMBER";
		sql += "           from";
		sql += "             EXMAINTABLE";

		sql += "           where";
		sql += "             STATUS = 10";
		//sql += "           and";
		//sql += "             RECEIPTNUMBER IS NOT NULL";
		// 本日フラグが[1]の場合は、システム日付と同じ検査日を対象とする
		if ("1".equals(todayflg)) {
			sql += "           and";
			sql += "             KENSA_DATE = TO_NUMBER(TO_CHAR(SYSDATE,'yyyymmdd'))";
		}
		sql += "           and";
		sql += "             (";

		String buff = "";
		// 検査室情報件数分、処理する
		for (KensasituInfoDto kensasituInfoDto : kensasituInfoDtoList) {

			// 検査室パラメータ情報を取得
			ArrayList<KensasituInfoDto.KensasituParam> kensasituParamList = kensasituInfoDto.getKensasitu_param_list();

			// 検査室パラメータ情報件数分、処理する
			for (KensasituInfoDto.KensasituParam kensasituParam : kensasituParamList) {

			    if (!"".equals(buff)) {
			        buff += "               or";
			    }
			    buff += "               (";
				buff += "                 (";
				buff += "                     KENSATYPE_ID = ?";
				buff += "                   and";
				buff += "                     KENSASITU_ID = ?";
				buff += "                 )";
				arglist.add(kensasituParam.getKensatypeId());
				arglist.add(kensasituParam.getKensasituId());

				if ("1".equals(kensasituParam.getMiwariateDispFlg())) {
					buff += "                   or";
					buff += "                 (";
					buff += "                     KENSATYPE_ID = ?";
					buff += "                   and";
					buff += "                     KENSASITU_ID IS NULL";
					buff += "                 )";

					arglist.add(kensasituParam.getKensatypeId());
				}
				buff += "               )";
			}
		}

		sql  += buff;
		sql  += "           )";
		sql  += "         ) EM";
		sql  += "       where";
		sql  += "         PATIENTINFO.KANJA_ID = EM.KANJA_ID";
		sql  += "   )";

		where += " where";
		where += "   ROWNUM <= 99";

		sql += where;
		
		orderBy += " order by";
		orderBy += "   RECEIPTDATE desc";
		
		sql += orderBy;

		Object[] args = new Object[arglist.size()];
		arglist.toArray(args);

		Dat = executeQuery(sql, args, conn);

		return Dat;
	}
	
	/**
	 * 受済（MRI）の受付番号取得
	 * @param kensasituInfoDtoList :検査室情報リスト
	 * @param todayflg             :本日フラグ
	 * @param conn                 :接続情報
	 * @return
	 * @throws Exception
	 */
	public static DataTable getUkezumiMri(List<KensasituInfoDto> kensasituInfoDtoList, String todayflg, Connection conn) throws Exception {

		DataTable Dat = null;

		String sql = "";
		String where = "";
		String orderBy = "";
		ArrayList<Object> arglist = new ArrayList<Object>();

		sql += " select";
		sql += "   *";
		sql += " from";
		sql += "   (";
		sql += "     select";
		//2023.09.19 Mod M.Furuya@Cosmo カラム名の変更 start
		sql += "       PATIENTINFO.RECEIPTNUMBER_HIS RECEIPTNUMBER";
		//sql += "       PATIENTINFO.RECEIPTNUMBER＿HIS RECEIPTNUMBER";
		//2023.09.19 Mod M.Furuya@Cosmo カラム名の変更 end
		//2024.03.13 Add K.Kasama@Cosmo 表示番号条件変更 start
		sql += "       ,PATIENTINFO.KANJA_NYUGAIKBN NYUGAIKBN";
		//2024.03.13 Add K.Kasama@Cosmo 表示番号条件変更 end
		sql += "       ,EM.YOBIDASI_STATUS YOBIDASI_STATUS";
		sql += "       ,EM.RECEIPTDATE RECEIPTDATE";
		sql += "       ,EM.RECEIPTNUMBER EM_RECEIPTNUMBER";
		sql += "     from";
		sql += "       PATIENTINFO";
		sql += "       ,(";
		sql += "           select";
		sql += "             KANJA_ID";
		sql += "             ,YOBIDASI_STATUS";
		sql += "             ,RECEIPTDATE";
		sql += "             ,RECEIPTNUMBER";
		sql += "           from";
		sql += "             EXMAINTABLE";

		sql += "           where";
		sql += "             STATUS = 10";
		sql += "           and";
		sql += "             (";
		sql += "               CHECK_FLG10 <> '1'";
		sql += "               or";
		sql += "               CHECK_FLG10 IS NULL";
		sql += "             )";
		if ("1".equals(todayflg)) {
			sql += "           and";
			sql += "             KENSA_DATE = TO_NUMBER(TO_CHAR(SYSDATE,'yyyymmdd'))";
		}
		sql += "           and";
		sql += "             (";

		String buff = "";
		// 検査室情報件数分、処理する
		for (KensasituInfoDto kensasituInfoDto : kensasituInfoDtoList) {

			// 検査室パラメータ情報を取得
			ArrayList<KensasituInfoDto.KensasituParam> kensasituParamList = kensasituInfoDto.getKensasitu_param_list();

			// 検査室パラメータ情報件数分、処理する
			for (KensasituInfoDto.KensasituParam kensasituParam : kensasituParamList) {

			    if (!"".equals(buff)) {
			        buff += "               or";
			    }
			    buff += "               (";
				buff += "                 (";
				buff += "                     KENSATYPE_ID = ?";
				buff += "                   and";
				buff += "                     KENSASITU_ID = ?";
				buff += "                 )";
				arglist.add(kensasituParam.getKensatypeId());
				arglist.add(kensasituParam.getKensasituId());

				if ("1".equals(kensasituParam.getMiwariateDispFlg())) {
					buff += "                   or";
					buff += "                 (";
					buff += "                     KENSATYPE_ID = ?";
					buff += "                   and";
					buff += "                     KENSASITU_ID IS NULL";
					buff += "                 )";

					arglist.add(kensasituParam.getKensatypeId());
				}
				buff += "               )";
			}
		}

		sql  += buff;
		sql  += "           )";
		sql  += "         ) EM";
		sql  += "       where";
		sql  += "         PATIENTINFO.KANJA_ID = EM.KANJA_ID";
		sql  += "   )";

		where += " where";
		where += "   ROWNUM <= 99";

		sql += where;
		
		orderBy += " order by";
		orderBy += "   RECEIPTDATE desc";
		
		sql += orderBy;

		Object[] args = new Object[arglist.size()];
		arglist.toArray(args);

		Dat = executeQuery(sql, args, conn);

		return Dat;
	}
	
	/**
	 * 準備中の受付番号取得
	 * @param kensasituParamList  :検査室パラメータ情報リスト
	 * @param todayflg            :本日フラグ
	 * @param conn                :接続情報
	 * @return
	 * @throws Exception
	 */
	public static DataTable getJunbichu(ArrayList<KensasituInfoDto.KensasituParam> kensasituParamList, String todayflg, Connection conn) throws Exception {

		DataTable Dat = null;

		String sql = "";
		String where = "";
		ArrayList<Object> arglist = new ArrayList<Object>();

		sql += " select";
		sql += "   *";
		sql += " from";
		sql += "   (";
		sql += "     select";
		//2023.09.19 Mod M.Furuya@Cosmo カラム名の変更 start
		sql += "       PATIENTINFO.RECEIPTNUMBER_HIS RECEIPTNUMBER";
		//sql += "       PATIENTINFO.RECEIPTNUMBER＿HIS RECEIPTNUMBER";
		//2023.09.19 Mod M.Furuya@Cosmo カラム名の変更 end
		//2024.03.13 Add K.Kasama@Cosmo 表示番号条件変更 start
		sql += "       ,PATIENTINFO.KANJA_NYUGAIKBN NYUGAIKBN";
		//2024.03.13 Add K.Kasama@Cosmo 表示番号条件変更 end
		sql += "       ,EM.RECEIPTNUMBER EM_RECEIPTNUMBER";
		sql += "     from";
		sql += "       PATIENTINFO";
		sql += "       ,(";
		sql += "         select";
		sql += "           KANJA_ID";
		sql += "           ,RECEIPTNUMBER";
		sql += "         from";
		sql += "           EXMAINTABLE";
		sql += "         where";
		sql += "           STATUS < 20";
		sql += "         and";
		sql += "           CHECK_FLG10 = '1'";
		// 本日フラグが[1]の場合は、システム日付と同じ検査日を対象とする
		if ("1".equals(todayflg)) {
			sql += "     and";
			sql += "       KENSA_DATE = TO_NUMBER(TO_CHAR(SYSDATE,'yyyymmdd'))";
		}

		sql += "         and";
		sql += "           (";

		String buff = "";
		// 検査室パラメータ情報件数分、処理する
		for (KensasituInfoDto.KensasituParam kensasituParam : kensasituParamList) {

		    if (!"".equals(buff)) {
		    	buff += "           or";
		    }
		    buff += "         (";
			buff += "           (";
			buff += "               KENSATYPE_ID = ?";
			buff += "             and";
			buff += "               KENSASITU_ID = ?";
			buff += "           )";
			//2023.09.05 M.Furuya del 未割当も表示するように対処 Start
			//buff += "         )";
			//2023.09.05 M.Furuya del 未割当も表示するように対処 End

			arglist.add(kensasituParam.getKensatypeId());
			arglist.add(kensasituParam.getKensasituId());
			
			//2023.09.05 M.Furuya add 未割当も表示するように対処 Start
			if ("1".equals(kensasituParam.getMiwariateDispFlg())) {
				buff += "                   or";
				buff += "                 (";
				buff += "                     KENSATYPE_ID = ?";
				buff += "                   and";
				buff += "                     KENSASITU_ID IS NULL";
				buff += "                 )";

				arglist.add(kensasituParam.getKensatypeId());
			}
			buff += "               )";
			//2023.09.05 M.Furuya add 未割当も表示するように対処 End
		}

		sql += buff;
		sql += "           )";

		sql += "         order by";
		sql += "           EXAMSTARTDATE desc";
		sql += "       ) EM";
		sql  += "     where";
		sql  += "       PATIENTINFO.KANJA_ID = EM.KANJA_ID";
		sql  += "   )";
		
		where += " where";
		where += "   ROWNUM <= 99";

		sql += where;

		Object[] args = new Object[arglist.size()];
		arglist.toArray(args);

		Dat = executeQuery(sql, args, conn);

		return Dat;
	}
}
