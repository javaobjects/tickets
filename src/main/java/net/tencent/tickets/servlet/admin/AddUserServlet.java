package net.tencent.tickets.servlet.admin;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.tencent.tickets.entity.CertType;
import net.tencent.tickets.entity.City;
import net.tencent.tickets.entity.UserType;
import net.tencent.tickets.entity.Users;
import net.tencent.tickets.service.CertTypeService;
import net.tencent.tickets.service.CityService;
import net.tencent.tickets.service.ProvinceService;
import net.tencent.tickets.service.UserService;
import net.tencent.tickets.service.UserTypeService;


@WebServlet(description = "新增管理员用户", urlPatterns = { "/AddUserServlet" })
public class AddUserServlet extends HttpServlet {

	/**
	 * serialVersionUID
	*/
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//1.处理乱码
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		//2. 获取数据
		String userName = request.getParameter("userName");
		String passWord = request.getParameter("passWord");//密码
		String confirm_password = request.getParameter("confirm_passWord");//确认密码
		String rule = request.getParameter("rule");
		String realName = request.getParameter("realName");
		String sex = request.getParameter("sex");//性别
		String provinceNum = request.getParameter("province");//省份
		String cityNum = request.getParameter("city");//cityid
		String certTypeId = request.getParameter("certType");//证件类型
		String cert = request.getParameter("cert");//证件号码
		String birthday_date = request.getParameter("birthday");//出生日期
		String userTypeId = request.getParameter("user_type");//旅客类型
		String content = request.getParameter("content");//备注
				
		//4. 校验通过,调用底层service的注册方法添加用户到数据库
		Date birthday = null;
		try {
			birthday = new SimpleDateFormat("yyyy-MM-dd").parse(birthday_date);
		} catch (Exception e) {
			e.printStackTrace();
		}

		UserService userService = UserService.getInstance();
		CityService cityService = CityService.getInstance();
		CertTypeService certTypeService = CertTypeService.getInstance();
		UserTypeService userTypeService = UserTypeService.getInstance();
		
		City city = cityService.queryCityByCityNum(cityNum);
		CertType certType = certTypeService.queryCertTypeById(certTypeId);
		UserType userType = userTypeService.queryUserTypeById(userTypeId);
		
		Users user = new Users(
				null,//id自动生成
				userName,
				passWord,
				rule,
				realName,
				sex.charAt(0),
				city,
				certType,
				cert,
				birthday,
				userType,
				content,
				'1',//用户状态有效
				request.getRemoteAddr(),//ip地址
				""//图片路径
				);
		
		
		if (userService.register(user)) {
			/**
			 * 由于记住咯session所以进来时是两层界面 所以此处需要清除session再跳login.jsp
			 */
			HttpSession session = request.getSession();
			session.invalidate();// 销毁session，会马上重新创建一个新的session

			// 1.把cookie清除掉
//			Cookie username_cookie = new Cookie("username", null);
//			username_cookie.setMaxAge(5 * 60);
//			username_cookie.setPath(request.getContextPath() + "/");

//			Cookie password_cookie = new Cookie("password", null);
//			password_cookie.setMaxAge(5 * 60);
//			password_cookie.setPath(request.getContextPath() + "/");

//			Cookie rule_cookie = new Cookie("rule", null);
//			rule_cookie.setMaxAge(5 * 60);
//			rule_cookie.setPath(request.getContextPath() + "/");

//			response.addCookie(username_cookie);
//			response.addCookie(password_cookie);
//			response.addCookie(rule_cookie);
			// 2.返回登录页面
			String mes = URLEncoder.encode("注册成功", "utf-8");
			response.sendRedirect(request.getContextPath() + "/login.jsp?message=" + mes);

		} else {
			// 注册失败，回到新增用户页面
			request.setAttribute("message", "注册失败,请稍后再试");
			//为防止省份为空白需要把所有省份再传一次
			request.setAttribute("provinces", ProvinceService.getInstance().getAllProvince());
			request.getRequestDispatcher("/admin/userinfo_add.jsp").forward(request, response);
		}
	}
}
