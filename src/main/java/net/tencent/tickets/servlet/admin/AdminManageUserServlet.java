package net.tencent.tickets.servlet.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jxl.Workbook;
import jxl.write.Label;//lib文件夹内导入对应的jar
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import net.tencent.tickets.entity.Users;
import net.tencent.tickets.service.UserService;
import net.tencent.tickets.util.PageUtil;


/**
 * <p>Title: AdminManageUserServlet</p>  
 * <p>
 *	Description: 
 * 管理员模块对用户进行管理处理的servlet
 * 管的事有（添加用户，修改用户，删除用户，查询用户，对查询结果进行分页的请求）
 * </p> 
 * @author xianxian 
 * @date 2019年9月4日
 */
@WebServlet("/AdminManageUserServlet")
public class AdminManageUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//处理乱码
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String operator = request.getParameter("operator");
		
		
		if("toQueryUserView".equals(operator))
		{
			toQueryUserView(request,response);
		}else if("queryUser".equals(operator))
		{
			queryUser(request,response);
		}else if("queryUserByPage".equals(operator))
		{
			queryUserByPage(request,response);
		}else if("exportExcel".equals(operator))
		{
			exportExcel(request,response);
		}
	}

	/**
	 * 
	 * <p>Title: exportExcel</p>  
	 * <p>
	 *	Description: 
	 *	导出excel报表的方法
	 * </p> 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void exportExcel(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException{
		
		HttpSession session = request.getSession();
		
		@SuppressWarnings("unchecked")
		List<Users> users = (List<Users>)session.getAttribute("users");
		
		if(users == null || users.size() == 0)
		{
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().println("<script>alert('请先查询');</script>");
		}else
		{
		response.setHeader("Content-disposition", "attachment; filename="
				+ new String("用户".getBytes("GB2312"), "8859_1") + 
				".xls");
				response.setHeader("pragma", "no-cache");
				response.setContentType("application/msexcel");
				ServletOutputStream os = response.getOutputStream();
				WritableWorkbook workbook = Workbook.createWorkbook(os);
				
				WritableSheet ws = workbook.createSheet("用户列表", 0);
				
				try {
					//首先写表头：id username
					ws.addCell(new Label(0, 0, "id"));
					ws.addCell(new Label(1, 0, "用户名"));
					ws.addCell(new Label(2, 0, "性别"));
					ws.addCell(new Label(3, 0, "证件类型"));
					ws.addCell(new Label(4, 0, "证件号码"));
					ws.addCell(new Label(5, 0, "旅客类型"));
					
					
					for(int row = 1;row <= users.size();row++)
					{
						Users user = users.get(row-1);
						ws.addCell(new Label(0, row, user.getId()+""));
						ws.addCell(new Label(1, row, user.getUserName()));
						ws.addCell(new Label(2, row, user.getUserSex() == 49 ? "男" : "女"));
						ws.addCell(new Label(3, row, user.getCertType().getContent()));
						ws.addCell(new Label(4, row, user.getUserCert()));
						ws.addCell(new Label(5, row, user.getUserType().getContent()));
					}
					
					workbook.write();
				} catch (Exception e) {
					e.printStackTrace();
				}finally
				{
					try {
						workbook.close();
					} catch (WriteException e) {
						e.printStackTrace();
					}
				}
		}	
	}

	/**
	 * 
	 * <p>Title: queryUserByPage</p>  
	 * <p>
	 *	Description: 
	 * 根据页码查询对应页码数据的方法
	 * 1.servlet可以获取到哪些数据？
	 * 进来的数据有哪些：username  cert_type cert user_type sex  pageCount
	 * 2.页码需要哪些数据？
	 * 查询结果的第2页数据，
	 * 总页数，页码（这里是动态传入的，），
	 * username  cert_type cert user_type sex  pageCount要回显，
	 * </p> 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void queryUserByPage(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException{
		//获取表单数据
		String username = request.getParameter("username");
		
		String cert_type = request.getParameter("cert_type");
		String cert = request.getParameter("cert");
		
		String user_type = request.getParameter("user_type");
		String sex = request.getParameter("sex");
		String pageCount = request.getParameter("pageCount");
		String pageNumber = request.getParameter("pageNumber");
		
		//因为数据已经查出来了，在session中，这里只是分页，查询对应页码的数据
		HttpSession session=request.getSession();
		List<Users> users = (List<Users>)session.getAttribute("users");
		/*
		 * 出去的数据有哪些：查询结果的第一页数据，
		 * 总页数，页码（这里是1，），username  
		 * cert_type cert user_type sex  pageCount要回显
		 */
		//回传刚刚输入的查询条件
		
		request.setAttribute("username", username);
		request.setAttribute("cert_type", cert_type);
		request.setAttribute("cert", cert);
		request.setAttribute("user_type", user_type);
		request.setAttribute("sex", sex);
		request.setAttribute("pageCount", pageCount);
		
		//对查询结果进行分页，把查询结果放入session，把第一页的数据传给页面即可
		/*
		 * 查询结果的第一页数据，总页数，页码（这里是1，）
		 * 
		 * 需要这三个数据怎么办？定义一个工具类，定义相关的算法，获取这些数据
		 * 这个工具类就是大名鼎鼎的PageUtil.java
		 */
		PageUtil pageUtil = new PageUtil(users, Integer.parseInt(pageCount), Integer.parseInt(pageNumber));
		request.setAttribute("userList", pageUtil.getUsers_page());//把查询结果users传给userlist.jsp页面
		request.setAttribute("pagesum", pageUtil.getPagesum());//总页数
		request.setAttribute("pageNumber", pageUtil.getPageNumber());//页码
		request.getRequestDispatcher("/admin/userlist.jsp").forward(request, response);
	}


	/**
	 * 
	 * <p>Title: queryUser</p>  
	 * <p>
	 *	Description: 
	 * 查询用户信息的方法
	 * 
	 * 进来的数据有哪些：username  cert_type cert user_type sex  pageCount
	 * 
	 * 出去的数据有哪些：查询结果的第一页数据，总页数，页码（这里是1，），
	 * username  cert_type cert user_type sex  pageCount要回显，
	 * </p> 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void queryUser(HttpServletRequest request,
			HttpServletResponse response)throws ServletException, IOException {
		//获取表单数据
		String userName = request.getParameter("userName");
		String sex = request.getParameter("sex");
		String cert_type = request.getParameter("cert_type");// 证件类型
		String cert = request.getParameter("cert");// 证件号码
		String user_type = request.getParameter("user_type");// 旅客类型

		String pageCount = request.getParameter("pageCount");// 每页显示多少条信息

		// 怎么查询servlet是不管的，全部交给service，servlet只管调用
		UserService userService = UserService.getInstance();

		List<Users> users = userService.getUserByCondition(userName, Integer.parseInt(cert_type), cert,
				Integer.parseInt(user_type), sex.charAt(0));
		

		/*
		 * 出去的数据有哪些：查询结果的第一页数据，总页数，页码（这里是1，），username cert_type cert user_type sex
		 * pageCount要回显
		 */
		// 回传刚刚输入的查询条件
		request.setAttribute("userName", userName);
		request.setAttribute("sex", sex);
		request.setAttribute("cert_type", cert_type);
		request.setAttribute("cert", cert);
		request.setAttribute("user_type", user_type);
		request.setAttribute("pageCount", pageCount);

		// 对查询结果进行分页，把查询结果放入session，把第一页的数据传给页面即可

		HttpSession session = request.getSession();
		session.setAttribute("users", users);
		/*
		 * 查询结果的第一页数据，总页数，页码（这里是1，）
		 * 
		 * 需要这三个数据怎么办？定义一个工具类，定义相关的算法，获取这些数据 这个工具类就是大名鼎鼎的PageUtil.java
		 */
		PageUtil pageUtil = new PageUtil(users, Integer.parseInt(pageCount), 1);
		request.setAttribute("userList", pageUtil.getUsers_page());// 把查询结果users传给userlist.jsp页面
		request.setAttribute("pagesum", pageUtil.getPagesum());// 总页数
		request.setAttribute("pageNumber", pageUtil.getPageNumber());// 页码
		request.getRequestDispatcher("/admin/userlist.jsp").forward(request, response);
	}

	/**
	 * <p>Title: toQueryUserView</p>  
	 * <p>
	 *	Description: 
	 *  去往查询用户页面的方法
	 * </p> 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void toQueryUserView(HttpServletRequest request,
			HttpServletResponse response)throws ServletException, IOException {
		//由于不需要带礼物（传递数据）给页面，所以直接跳转过去
		request.getRequestDispatcher("/admin/userlist.jsp").forward(request, response);
	}
}
