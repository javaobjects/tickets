package net.tencent.tickets.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import net.tencent.tickets.entity.CertType;
import net.tencent.tickets.entity.City;
import net.tencent.tickets.entity.Province;
import net.tencent.tickets.entity.UserType;
import net.tencent.tickets.entity.Users;
import net.tencent.tickets.util.DBUtils_pool;
import net.tencent.tickets.util.Md5Utils;

public class UsersDao {

	/**
	 * id NUMBER(11) not null, username VARCHAR2(30) not null, password
	 * VARCHAR2(50) not null, rule VARCHAR2(2) default '2' not null, realname
	 * VARCHAR2(50) not null, sex CHAR(1) default '1' not null, city NUMBER(11)
	 * not null, cert_type NUMBER(11) not null, cert VARCHAR2(50) not null,
	 * birthday DATE not null, user_type NUMBER(11), content VARCHAR2(3000),
	 * status CHAR(1) default '1' not null, login_ip VARCHAR2(50), image_path
	 * VARCHAR2(200)
	 */
	private static final String ADD_USER = "insert into tickets_2_user(id,username,password,rule,realname,sex,city,cert_type"
			+ ",cert,birthday,user_type,content,status,login_ip,image_path)"
			+ " values (tickets_2_user_seq.nextval,?,?,?,?,?,?,?,?,?,?,?,'1',?,'')";
//			+ " values (tickets_2_user_seq.nextval,?,?,'2',?,?,?,?,?,?,?,?,'1',?,'')";
		//	+ " values (tab_user_seq.nextval,?,?,'2',?,?,200,1,'440104201910106119',?,1,'备注','1',?,'')";
	private static final String QUERY_USERNAME = "select count(1) as col_count from tickets_2_user where username = ?";
	private static final String QUERY_USER_BY_USERNAME_AND_PASSWORD = "select u.id,u.username,u.password,u.rule,"
			+ "u.realname,u.sex,u.city c_id,u.cert_type"
			+ ",u.cert,u.birthday,u.user_type,u.content,u.status,u.login_ip,u.image_path,"
			+ "c.city,p.province,p.provinceid,ut.content ut_content,ct.content ct_content "
			+ "from tickets_2_user u,tickets_2_city c,tickets_2_province p,tickets_2_usertype ut,tickets_2_certtype ct"
			+ " where u.city=c.id and p.provinceid=c.father "
			+ "and ut.id=u.user_type and ct.id=u.cert_type "
			+ "and u.username=? and u.password=?";
	
	private UsersDao() {}
	
	public static UsersDao userDao;
	
	public static UsersDao getInstance() {
		if(userDao == null) {
			userDao = new UsersDao();
		}
		return userDao;
	}
	
	public int addUser(Users user) {
		int rows = 0;
		Connection conn = null;
		PreparedStatement stmt = null;
		try {

			conn = DBUtils_pool.getConnection();
			stmt = conn.prepareStatement(ADD_USER);
			
			stmt.setString(1, user.getUsername());//用户名
			stmt.setString(2, Md5Utils.md5(user.getPassword()));//密码
			stmt.setString(3, user.getRule());//权限
			stmt.setString(4, user.getRealname());//真实姓名
			stmt.setString(5, user.getSex() + "");//性别
			stmt.setInt(6, user.getCity().getId());//城市 此处关联的居然是id不是cityid
			stmt.setString(7, user.getCerttype().getId().toString());//证件类型
			stmt.setString(8, user.getCert());//证件号码
			stmt.setDate(9, new java.sql.Date(user.getBirthday().getTime()));//生日
			stmt.setInt(10, user.getUsertype().getId());//旅客类型
			stmt.setString(11, user.getContent());//备注
			stmt.setString(12, user.getLoginIp());//Ip地址
			
			rows=stmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils_pool.release(conn, stmt, null);
		}
		return rows;
	}

	/**
	 * 
	 * <p>Title: queryUsername</p>  
	 * <p>
	 *	Description: 
	 * 查询用户名是否存在
	 * </p> 
	 * @param username
	 * @return
	 */
	public boolean queryUsername(String username) {
		Boolean result = false;
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {

			conn = DBUtils_pool.getConnection();
			stmt = conn.prepareStatement(QUERY_USERNAME);
			stmt.setString(1,username);

			rs = stmt.executeQuery();
			if(rs.next()) {
				int tmp = rs.getInt("col_count");
				if(tmp > 0) {
					result = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils_pool.release(conn, stmt, null);
		}
		return result;
	}

	/**
	 * 
	 * <p>Title: queryUserByUsernameAndPassword</p>  
	 * <p>
	 *	Description: 
	 *	根据用户名和密码查询用户信息
	 * </p> 
	 * @param username
	 * @param password
	 * @return
	 */
	public Users queryUserByUsernameAndPassword(String username, String password) {
		
		Users user = null;
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs=null;
		try {

			conn = DBUtils_pool.getConnection();
			stmt = conn.prepareStatement(QUERY_USER_BY_USERNAME_AND_PASSWORD);
			stmt.setString(1,username);
			stmt.setString(2,password);
			rs = stmt.executeQuery();
			if(rs.next())
			{
				user = new Users();
				/*
				 * id,username,password,rule,realname,sex,city,cert_type"
			+ ",cert,birthday,user_type,content,status,login_ip,image_path
				 */
				user.setId(rs.getInt("id"));
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				user.setRule(rs.getString("rule"));
				user.setRealname(rs.getString("realname"));
				//补全另外10个数据
				user.setSex(rs.getString("sex").charAt(0));
				user.setCity(new City(rs.getInt("c_id"),null, rs.getString("city"), new Province(null, rs.getString("provinceid"), rs.getString("province"))));
				user.setCerttype(new CertType(rs.getInt("cert_type"), rs.getString("ct_content")));
				user.setCert(rs.getString("cert"));
				user.setBirthday(rs.getDate("birthday"));
				user.setUsertype(new UserType(rs.getInt("user_type"),rs.getString("ut_content")));
				user.setContent(rs.getString("content"));
				user.setStatus(rs.getString("status").charAt(0));
				user.setLoginIp(rs.getString("login_ip"));
				user.setImagePath(rs.getString("image_path"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils_pool.release(conn, stmt, rs);
		}
		
		return user;
	}
	
	public int updateUser(Users user) {
		int rows=0;
		Connection conn=null;
		PreparedStatement stmt=null;
		
		try {
			//这些待更新的数据：真实姓名 性 别   城市 证件类型 证件号码 出生日期 旅客类型 备注
			String update_user_sql="update tickets_2_user set realname=?,sex=?,city=?,cert_type=?,cert=?,"
					+ "birthday=?,user_type=?,content=? where id=?";
			conn=DBUtils_pool.getConnection();
			stmt=conn.prepareStatement(update_user_sql);
			stmt.setString(1, user.getRealname());
			stmt.setString(2, user.getSex() + "");
			stmt.setInt(3, user.getCity().getId());
			stmt.setInt(4,user.getCerttype().getId());
			stmt.setString(5,user.getCert());
			stmt.setDate(6, new java.sql.Date(user.getBirthday().getTime()));
			stmt.setInt(7,user.getUsertype().getId());
			stmt.setString(8,user.getContent());
			stmt.setInt(9, user.getId());
			
			rows=stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBUtils_pool.release(conn, stmt, null);
		}
		return rows;
	}
	/**
	 * 
	 * <p>Title: find</p>  
	 * <p>
	 *	Description: 
	 *	根据id和旧的密码查询数据库，看能否找到用户，找到则旧密码输入正确
	 * </p> 
	 * @param id
	 * @param password_old
	 * @return
	 */
	public boolean find(Integer id, String password_old) {
		Boolean result=false;
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs=null;
		try {

			conn = DBUtils_pool.getConnection();
			stmt = conn.prepareStatement("select * from tickets_2_user where id=? and password=?");
			stmt.setInt(1,id);
			stmt.setString(2, password_old);
			rs=stmt.executeQuery();
			if(rs.next())
			{
				result=true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils_pool.release(conn, stmt, rs);
		}

		return result;
	}

	public void updatePassword(Integer id, String password_new) {
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			//这些待更新的数据：真实姓名 性 别   城市 证件类型 证件号码 出生日期 旅客类型 备注
			String update_user_sql="update tickets_2_user set password=? where id=?";
			conn = DBUtils_pool.getConnection();
			stmt = conn.prepareStatement(update_user_sql);
			stmt.setString(1,password_new);
			stmt.setInt(2,id);
			
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBUtils_pool.release(conn, stmt, null);
		}
	}


	/**
	 * 
	 * <p>Title: queryUserByCondition</p>  
	 * <p>
	 *	Description: 
	 * 根据给定条件查询用户信息：组合查询
	 * </p> 
	 * @param username
	 * @param certtype
	 * @param cert
	 * @param usertype
	 * @param sex
	 * @return
	 */
	public List<Users> queryUserByCondition(String username, int certtype,
			String cert, int usertype, char sex) {
		List<Users> users=new ArrayList<>();
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs=null;
		try {

			conn = DBUtils_pool.getConnection();
			
			StringBuffer query_user=new StringBuffer("select u.id,u.username,u.sex,u.cert,"
					+ "ct.id ct_id,ct.content ct_content,"
					+ "ut.id ut_id,ut.content ut_content "
					+ "from tickets_2_user u,tickets_2_usertype ut,tickets_2_certtype ct "
					+ "where ut.id=u.user_type and ct.id=u.cert_type and sex='"+sex+
					"' and cert_type="+certtype+" and user_type="+usertype);
			if(username!=null&& !"".equals(username.trim()))
			{
				query_user.append(" and username like '%"+username.trim()+"%'");
			}
			if(cert!=null && !"".equals(cert.trim()))
			{
				query_user.append(" and cert='"+cert+"'");
			}
			
			stmt = conn.prepareStatement(query_user.toString());
			
			
			rs=stmt.executeQuery();
			Users user=null;
			while(rs.next())
			{
				user=new Users();
				
				user.setId(rs.getInt("id"));
				user.setUsername(rs.getString("username"));
				user.setSex(rs.getString("sex").charAt(0));
				user.setCerttype(new CertType(rs.getInt("ct_id"), rs.getString("ct_content")));
				user.setCert(rs.getString("cert"));
				user.setUsertype(new UserType(rs.getInt("ut_id"),rs.getString("ut_content")));
				
				users.add(user);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils_pool.release(conn, stmt, rs);
		}

		return users;
	}

	public void insertImage(Integer id, String fileName) {

		Connection conn=null;
		PreparedStatement stmt=null;
		
		try {
			//这些待更新的数据：真实姓名 性 别   城市 证件类型 证件号码 出生日期 旅客类型 备注
			String update_user_sql="update tickets_2_user set image_path=? where id=?";
			conn=DBUtils_pool.getConnection();
			stmt=conn.prepareStatement(update_user_sql);
			stmt.setString(1, fileName);
			stmt.setInt(2, id);
			
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBUtils_pool.release(conn, stmt, null);
		}
	}

}