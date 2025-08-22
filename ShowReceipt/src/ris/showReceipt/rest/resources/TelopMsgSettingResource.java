package ris.showReceipt.rest.resources;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import ris.showReceipt.model.dto.SettingDto;
import ris.showReceipt.services.setting.SettingServiceCR;
import ris.showReceipt.services.telopSetting.TelopMsgSettingService;
import ris.showReceipt.util.Util;

@Path("/telopsetting")
public class TelopMsgSettingResource {

	private static Logger logger = LogManager.getLogger(TelopMsgSettingResource.class);

	@Context
	private ServletContext context;

	@Path("/cr")
	@GET
	@Produces("application/json;charset=UTF-8")
	public Response doGetCr(
			@FormParam("telopMsg1") String telopMsg1,
			@FormParam("telopMsg2") String telopMsg2,
			@FormParam("telopMsg3") String telopMsg3,
			@FormParam("telopMsgFlg") String telopMsgFlg,
			@Context HttpServletRequest request) throws Exception {

		logger.debug("☆★☆設定ファイル更新リクエスト---開始");

		// マスタ情報
		SettingDto setting = new SettingDto();
		
		// 設定ファイル取得サービス
		TelopMsgSettingService telopMsgSettingService = new TelopMsgSettingService(new SettingServiceCR());

		// 実行
		telopMsgSettingService.Execute(request, setting, context,telopMsg1,telopMsg2,telopMsg3,telopMsgFlg);

		// JSON変換
		String json = Util.getJson(setting);

		logger.debug("☆★☆設定ファイル更新リクエスト---JSON:" + json);

		logger.debug("☆★☆設定ファイル更新リクエスト---終了");

		return Response.ok().entity(json).build();
	}
	
}
