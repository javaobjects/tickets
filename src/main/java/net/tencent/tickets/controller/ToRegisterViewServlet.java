package net.tencent.tickets.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.tencent.tickets.service.ProvinceService;

/**
 * 去往注册页面的servlet
 */
@WebServlet("/ToRegisterViewServlet")
public class ToRegisterViewServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		//把所有省份数据传给页面
		request.setAttribute("provinces", ProvinceService.getInstance().getAllProvince());
		request.getRequestDispatcher("/user_register.jsp").forward(request, response);
	}
}
