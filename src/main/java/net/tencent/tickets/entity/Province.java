package net.tencent.tickets.entity;

/**
 * 
* <p>Title: Province</p>  
* <p>
*	Description: 
*	省份实体类
* </p> 
* @author xianxian 
* @date 2019年9月2日
 */
public class Province {

	/**id*/
	private Integer id;
	/**省份id 字符串类型**/
	private String provinceNum;
	/**省份名称 对应数据库的province字段**/
	private String provinceName;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getProvinceNum() {
		return provinceNum;
	}
	public void setProvinceNum(String provinceNum) {
		this.provinceNum = provinceNum;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public Province(Integer id, String provinceNum, String provinceName) {
		super();
		this.id = id;
		this.provinceNum = provinceNum;
		this.provinceName = provinceName;
	}
	public Province() {
		super();
	}
	@Override
	public String toString() {
		return "Province [id=" + id + ", provinceNum=" + provinceNum + ", provinceName=" + provinceName + "]";
	}
	
	
	
}
