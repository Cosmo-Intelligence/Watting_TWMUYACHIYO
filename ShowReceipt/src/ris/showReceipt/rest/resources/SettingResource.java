package ris.showReceipt.rest.resources;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import ris.showReceipt.model.dto.SettingDto;
import ris.showReceipt.services.setting.SettingService;
import ris.showReceipt.services.setting.SettingServiceCR;
import ris.showReceipt.services.setting.SettingServiceMRI;
import ris.showReceipt.util.Util;

@Path("/setting")
public class SettingResource {

	private static Logger logger = LogManager.getLogger(SettingResource.class);

	@Context
	private ServletContext context;

	@Path("/cr")
	@GET
	@Produces("application/json;charset=UTF-8")
	public Response doGetCr(
			@Context HttpServletRequest request) throws Exception {

		logger.debug("☆★☆設定ファイル情報取得リクエスト---開始");

		// マスタ情報
		SettingDto setting = new SettingDto();
		
		// 設定ファイル取得サービス
		SettingService settingService = new SettingService(new SettingServiceCR());

		// 実行
		settingService.Execute(request, setting, context);

		// JSON変換
		String json = Util.getJson(setting);

		logger.debug("☆★☆設定ファイル情報取得リクエスト---JSON:" + json);

		logger.debug("☆★☆設定ファイル情報取得リクエスト---終了");

		return Response.ok().entity(json).build();
	}
	
	@Path("/mri")
	@GET
	@Produces("application/json;charset=UTF-8")
	public Response doGetMri(
			@Context HttpServletRequest request) throws Exception {

		logger.debug("☆★☆設定ファイル情報取得リクエスト---開始");

		// マスタ情報
		SettingDto setting = new SettingDto();
		
		// 設定ファイル取得サービス
		SettingService settingService = new SettingService(new SettingServiceMRI());

		// 実行
		settingService.Execute(request, setting, context);

		// JSON変換
		String json = Util.getJson(setting);

		logger.debug("☆★☆設定ファイル情報取得リクエスト---JSON:" + json);

		logger.debug("☆★☆設定ファイル情報取得リクエスト---終了");

		return Response.ok().entity(json).build();
	}
}
