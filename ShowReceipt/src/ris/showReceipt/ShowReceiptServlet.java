package ris.showReceipt;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ShowReceiptServlet extends HttpServlet {
	  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  
		  // URLを取得
		  String requestURL = request.getRequestURL().toString();
		  // 最後の"/"より後ろの値に".html"を付与
		  String kensaTypeHtml = requestURL.substring(requestURL.lastIndexOf("/") + 1) + ".html";
		  
		  response.sendRedirect(kensaTypeHtml);
	  }
}
