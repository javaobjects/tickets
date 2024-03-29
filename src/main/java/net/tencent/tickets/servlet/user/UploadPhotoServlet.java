package net.tencent.tickets.servlet.user;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import net.tencent.tickets.entity.Users;
import net.tencent.tickets.service.UserService;

/**
 * 负责处理上传照片的servlet
 */
@WebServlet("/UploadPhotoServlet")
@MultipartConfig
public class UploadPhotoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*
		 * 1.后台使用servlet3.0技术上传照片
		 * 2.上传照片需要指定服务器上照片保存的地址，photos文件夹保存照片
		 * 3.所以要拿到照片的存储路径，方便回显数据
		 */
		
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		

		// 获取上传的文件集合
		//Collection<Part> parts = request.getParts();
		// 上传单个文件
	
			// Servlet3.0将multipart/form-data的POST请求封装成Part，通过Part对上传的文件进行操作。
			// Part part = parts[0];//从上传的文件集合中获取Part对象
			Part part = request.getPart("uploadFile");// 通过表单file控件(<input type="file" name="file">)的名字直接获取Part对象
			// Servlet3没有提供直接获取文件名的方法,需要从请求头中解析出来
			// 获取请求头，请求头的格式：form-data; name="file"; filename="snmp4j--api.zip"
			String header = part.getHeader("content-disposition");
			
			// 获取文件名
			String fileName = getFileName(header);
			
			// 存储路径 存到了服务器的路径 客户端的文件夹是看不到的 布署完成后可直接使用
			String saveServletPath = request.getServletContext().getRealPath("/photos") + File.separator + fileName;
			
			// 把文件写到指定路径
			//UUID.randomUUID()+".jpg"
			part.write(saveServletPath);

			//把这个照片路径保存到数据库
			HttpSession session = request.getSession();
			Users user_session = (Users)session.getAttribute("user");
			UserService userService = UserService.getInstance();
			Users user = userService.login(user_session.getUserName(), user_session.getUserPassword());
			

			//判断是否上传成功
			if(UserService.getInstance().saveImage(user.getId(),fileName)) {
				//回到更新用户信息页面，让用户看到自己的照片
				response.sendRedirect("ToUpdateUserServlet");
			}else {
				PrintWriter pw = response.getWriter();
				pw.println("<script>alert('上传照片失败，请稍后再试！');</script>");
				pw.flush();
				pw.close();
			}
			
	}
	/**
	 * 
	 * <p>Title: getFileName</p>  
	 * <p>
	 *	Description: 
	 *	根据请求头解析出文件名 请求头的格式：火狐和google浏览器下：form-data; name="file";
	 * filename="snmp4j--api.zip" IE浏览器下：form-data; name="file";
	 * filename="E:\snmp4j--api.zip"
	 * </p> 
	 * @param header 请求头
	 * @return 文件名
	 */
	public String getFileName(String header) {
		/**
		 * String[] tempArr1 =
		 * header.split(";");代码执行完之后，在不同的浏览器下，tempArr1数组里面的内容稍有区别
		 * 火狐或者google浏览器下：
		 * tempArr1={form-data,name="file",filename="snmp4j--api.zip"}
		 * IE浏览器下：tempArr1={form-data,name="file",filename="E:\snmp4j--api.zip"}
		 */
		String[] tempArr1 = header.split(";");
		/**
		 * 火狐或者google浏览器下：tempArr2={filename,"snmp4j--api.zip"}
		 * IE浏览器下：tempArr2={filename,"E:\snmp4j--api.zip"}
		 */
		String[] tempArr2 = tempArr1[2].split("=");
		// 获取文件名，兼容各种浏览器的写法
		String fileName = tempArr2[1].substring(tempArr2[1].lastIndexOf("\\") + 1).replaceAll("\"", "");
		return fileName;
	}
	
}
