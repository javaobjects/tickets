package net.tencent.tickets.servlet.user;

import java.io.IOException;
import java.io.PrintWriter;
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
import net.tencent.tickets.service.UserService;
import net.tencent.tickets.service.UserTypeService;


@WebServlet("/UpdateUserServlet")
public class UpdateUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//1.处理乱码
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		//2.获取这些待更新的数据：真实姓名 性 别   城市 证件类型 证件号码 出生日期 旅客类型 备注：
		String id = request.getParameter("id");
		String userName = request.getParameter("userName");//前端只读取不取数据
		
		String realname = request.getParameter("realname");
		String sex = request.getParameter("sex");
//		String provinceNum = request.getParameter("provinceNum");
		String cityNum = request.getParameter("city");
		String certType = request.getParameter("certType");
		String cert = request.getParameter("cert");
		String birthday = request.getParameter("birthday");
		String usertype = request.getParameter("userType");
		String content = request.getParameter("userContent");
		
		//3.把数据封装到User对象中
		Date birth = null;
		try{
			birth = new SimpleDateFormat("yyyy-MM-dd").parse(birthday);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		CertTypeService certTypeService = CertTypeService.getInstance();
		UserTypeService userTypeService = UserTypeService.getInstance();
		CityService cityService = CityService.getInstance();
		
		CertType cert_type = certTypeService.queryCertTypeById(certType);
		UserType user_type = userTypeService.queryUserTypeById(usertype);
		
		City city = cityService.queryCityByCityNum(cityNum);
		
		HttpSession session = request.getSession();
		Users session_user = (Users) session.getAttribute("user");
		
		Users user = new Users(
				Integer.parseInt(id), 
				session_user.getUserName(), 
				session_user.getUserPassword(), 
				session_user.getUserRule(),
				realname, 
				sex.charAt(0), 
				city, 
				cert_type, 
				cert, 
				birth, 
				user_type, 
				content, 
				'1', //用户状态有效
				request.getRemoteAddr(), //ip地址
				session_user.getUserImagePath()//图片路径
				);
		
		//4.调用底层UserService中的更新方法更新用户信息
		UserService userService = UserService.getInstance();
		boolean result = userService.updateUser(user);
		
		if (result) {
			// 重定向到ToUpdateUserServlet即可:再次获取更新后的用户信息然后去往更新页面展示，让用户看到更新后的效果
			// 同步更新session中的用户信息
			session.setAttribute("user", userService.login(session_user.getUserName(), session_user.getUserPassword()));

			response.sendRedirect("ToUpdateUserServlet");
		} else {
			response.setContentType("text/html;charset=utf-8");
			PrintWriter pw = response.getWriter();
			pw.println("<script>alert('更新失败.请稍后再试!');</script>");
		}
	}
}
