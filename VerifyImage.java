package com.xiaoyuer.pay.verifycode;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VerifyImage extends HttpServlet {

	private static final long serialVersionUID = 1L;

	// 设置备选汉字，剔除一些不雅的汉字
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		VerifyCode verifyCode = new VerifyCode();
		String textCode = verifyCode.generateTextCode();
		request.getSession(true).setAttribute(
				VerifyCode.SIMPLE_CAPCHA_SESSION_KEY, textCode);// 将字符串写入session

		BufferedImage bim = verifyCode.generateImageCode(textCode);

		// 设置浏览器不缓存本页
		response.setHeader("Cache-Control", "no-cache");
		// 设置图片类型
		response.setContentType("image/jpg");
		// 输出验证码给客户端
		ImageIO.write(bim, "JPEG", response.getOutputStream());

	}

}
