package ris.showReceipt.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class SessionControler {

	public static final String SYSTEMCONFIG = "SYSTEMCONFIG";
	public static final String KENSASITUINFO = "KENSASITUINFO";
	public static final String KENSASITUFUZAIINFO = "KENSASITUFUZAIINFO";
	//public static final String KENSATYPE = "KENSATYPE";
	//public static final String KENSASITU = "KENSASITU";
	//public static final String NULLFLG   = "NULLFLG";


	public static HttpSession createSession(HttpServletRequest request){
		return request.getSession(true);
	}

	public static HttpSession getSession(HttpServletRequest request){
		return request.getSession(false);
	}

	public static Object getValue(HttpServletRequest request,String name){
		HttpSession session = request.getSession(false);

		return session.getAttribute(name);
	}

	public static void setValue(HttpServletRequest request,String name, Object value){
		HttpSession session = request.getSession(false);

		session.setAttribute(name,value);
	}

	public static void removeValue(HttpServletRequest request,String name){
		HttpSession session = request.getSession(false);

		session.removeAttribute(name);
	}

	public static void clearSession(HttpServletRequest request){

		HttpSession session = request.getSession(false);

		if(session == null){
			return;
		}

		session.invalidate();

		return;
	}
}
