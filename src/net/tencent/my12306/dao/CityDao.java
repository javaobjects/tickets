package net.tencent.my12306.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import net.tencent.my12306.entity.City;
import net.tencent.my12306.util.DBUtils;

public class CityDao {
	
	private static final String QUERY_CITY_BY_PROVINCEID = "select id,cityid,city from my12306_2_city where father=?";

	/**
	 * 获取指定省份的所有城市信息
	 */
	public List<City> queryCityByProvinceid(String provinceId)
	{
		List<City> cities=new ArrayList<>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs=null;
		try {

			conn = DBUtils.getConnection();
			stmt = conn.prepareStatement(QUERY_CITY_BY_PROVINCEID);
			stmt.setString(1,provinceId);
			rs=stmt.executeQuery();
			while(rs.next())
			{
				City c=new City();
				
				c.setCityId(rs.getString("cityid"));
				c.setId(rs.getInt("id"));
				c.setCityName(rs.getString("city"));
				
				cities.add(c);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.release(conn, stmt, rs);
		}
		return cities;
	}
	
	private CityDao(){}
	
	private static CityDao cityDao;
	
	public static CityDao getInstance()
	{
		if(cityDao==null)
		{
			cityDao=new CityDao();
		}
		return cityDao;
	}

}