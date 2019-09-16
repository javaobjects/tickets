package net.tencent.my12306.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.tencent.my12306.entity.CertType;
import net.tencent.my12306.entity.City;
import net.tencent.my12306.entity.Province;
import net.tencent.my12306.entity.UserType;
import net.tencent.my12306.entity.Users;
import net.tencent.my12306.service.UserService;

/**
 * Servlet implementation class UpdateAdminServlet
 */
@WebServlet(description = "新增管理员用户", urlPatterns = { "/AddUserServlet" })
public class AddUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//1.处理乱码
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		//2. 获取数据
		String username = request.getParameter("username");
		String password = request.getParameter("password");//密码
		String confirm_password = request.getParameter("confirm_password");//确认密码
		String rule = request.getParameter("rule");
		String realname = request.getParameter("realName");
		String sex = request.getParameter("sex");//性别
		String province = request.getParameter("province");//省份
		String cityId = request.getParameter("city");//cityid
		String certtype = request.getParameter("certtype");//证件类型
		String cert = request.getParameter("cert");//证件号码
		String birthday_date = request.getParameter("birthday");//出生日期
		String usertype = request.getParameter("user_type");//旅客类型
		String content = request.getParameter("content");//备注
		
		//3. 数据的非空校验和合法性校验
		StringBuffer sb = UserServlet.validateRegisterForm(username, password, confirm_password,"on");
		
		if(sb.length() > 0) {
			//校验不通过
			request.setAttribute("message", "必填信息为空，请重新注册");
			request.getRequestDispatcher("/admin/userinfo_add.jsp").forward(request, response);
		}else {
			//4. 校验通过,调用底层service的注册方法添加用户到数据库
			Date birthday = null;
			try {
				birthday = new SimpleDateFormat("yyyy-MM-dd").parse(birthday_date);
			} catch (Exception e) {
				e.printStackTrace();
			}

			UserService userService = UserService.getInstance();
			

			/*
			 * 转换方法 需要牢记！！！
			 * 
			 * City c = new City(); 
			 * c.setCityId(city);
			 *  user.setCity(c);//获取城市 String 转 引用类型
			 * 
			 * user.setCerttype(new CertType(Integer.parseInt(cert_type), null));//证件类型
			 * String 转 引用类型
			 */			
			
			Users user = new Users(null,username,password,rule,realname,sex.charAt(0),
					new City(Integer.parseInt(cityId),null,null,new Province(null,province,null)),
					new CertType(Integer.parseInt(certtype),null),
					cert,
					birthday,
					new UserType(Integer.parseInt(usertype),null),content,'1',request.getRemoteAddr(),"");

			// 服务端校验通过之后，注册方法调用之前，应该先判断用户名是否经存在
			/**
			 * 则需要定义判断用户名是否已经存在的方法，
			 * 如果存在则返回新增用户页面， 
			 * 提示用户名已经存在，
			 * 如果不存在则继续新增
			 */

			if (userService.isExistsUserName(username)) {
				// 用户名已存在，回到注册页面
				request.setAttribute("message", "用户名已被占用");
				request.getRequestDispatcher("/admin/userinfo_add.jsp").forward(request, response);
			} else {
				if (userService.register(user)) {

					response.sendRedirect(request.getContextPath() + "/login.jsp?message='注册成功'");

				} else {
					// 注册失败，回到新增用户页面
					request.setAttribute("message", "注册失败");
					request.getRequestDispatcher("/admin/userinfo_add.jsp").forward(request, response);
				}
			}
		}
	}
}
