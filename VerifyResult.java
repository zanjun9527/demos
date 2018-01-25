package com.xiaoyuer.pay.verifycode;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;

public class VerifyResult extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		JSONObject jsonResult = new JSONObject();
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		Object SIMPLE_CAPCHA_SESSION_KE = request.getSession().getAttribute(
				VerifyCode.SIMPLE_CAPCHA_SESSION_KEY);
		if (SIMPLE_CAPCHA_SESSION_KE != null) {
			String validateC = ((String) SIMPLE_CAPCHA_SESSION_KE)
					.toLowerCase();// session中的验证码
			String veryCode = (request.getParameter("code")).toLowerCase();// 用户输入的验证码，这里接收参数的时候注意中文的乱码问题
			if (veryCode != null) {
				if (validateC.equals(veryCode)) {
					jsonResult.put("result", "1");
				} else {
					jsonResult.put("result", "0");
				}
			} else {
				jsonResult.put("result", "-1");
			}
		} else {
			jsonResult.put("result", "-2");
		}

		response.setContentType("application/x-javascript;charset=utf-8");
		PrintWriter pw;
		try {
			pw = response.getWriter();
			pw.print(jsonResult.toJSONString());
			pw.flush();
			pw.close();
		} catch (IOException e) {
		}
	}
}
