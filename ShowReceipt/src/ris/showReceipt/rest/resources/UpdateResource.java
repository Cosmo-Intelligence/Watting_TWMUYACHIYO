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
import ris.showReceipt.model.dto.UpdateDto;
import ris.showReceipt.services.update.UpdateServiceCR;
import ris.showReceipt.services.update.UpdateServiceMRI;
import ris.showReceipt.util.Util;

@Path("/update")
public class UpdateResource {

	private static Logger logger = LogManager.getLogger(SettingResource.class);

	@Context
	private ServletContext context;

	@Path("/cr")
	@GET
	@Produces("application/json;charset=UTF-8")
	public Response doGetCr(
			@Context HttpServletRequest request) throws Exception {

		logger.debug("☆★☆受付番号更新リクエスト---開始");

		// マスタ情報
		UpdateDto master = new UpdateDto();
		
		// 受付番号更新情報取得サービス
		UpdateServiceCR updateService = new UpdateServiceCR();

		// 実行
		updateService.Execute(request, master, context);

		// JSON変換
		String json = Util.getJson(master);

		logger.debug("☆★☆受付番号更新リクエスト---JSON:" + json);

		logger.debug("☆★☆受付番号更新リクエスト---終了");

		return Response.ok().entity(json).build();
	}
	
	@Path("/mri")
	@GET
	@Produces("application/json;charset=UTF-8")
	public Response doGetMri(
			@Context HttpServletRequest request) throws Exception {

		logger.debug("☆★☆受付番号更新リクエスト---開始");

		// マスタ情報
		UpdateDto master = new UpdateDto();
		
		// 受付番号更新情報取得サービス
		UpdateServiceMRI updateService = new UpdateServiceMRI();

		// 実行
		updateService.Execute(request, master, context);

		// JSON変換
		String json = Util.getJson(master);

		logger.debug("☆★☆受付番号更新リクエスト---JSON:" + json);

		logger.debug("☆★☆受付番号更新リクエスト---終了");

		return Response.ok().entity(json).build();
	}

}