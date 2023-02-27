<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.List,
    net.tencent.tickets.entity.Province,
    java.util.Iterator,
    net.tencent.tickets.entity.Users"%>
     <!-- 使用jstl:java standard tag library(单词缩写)
 1.需要先导入jstl.jar包 2.页面通过指令引入标签  3.使用标签 -->   
  <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户信息修改</title>
<link href="<%=request.getContextPath()%>/css/css.css" rel="stylesheet" type="text/css">

</head>

<body class="write_bg">
<form name="form1" method="post" action="<%=request.getContextPath()%>/UpdateUserServlet" id="edit_form" >

<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td height="30"><br>
            <br></td>
  </tr>
</table>
<table width="835" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td height="20" colspan="2" align="center" ></td>
  </tr>
  <tr>
    <td width="64" height="11" class="text_blod_title"></td>
    <td width="786" height="30" align="left" class="text_blod_title">修改个人信息</td>
  </tr>
  <tr>
    <td height="15" colspan="2" ><img src="<%=request.getContextPath() %>/images/line1.jpg" width="835" height="6"></td>
  </tr>
  <tr>
    <td colspan="2"><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="64"></td>
        <td width="772" height="25" align="left" class="text_cray">注：标有
         <span class="text_red">*</span> 处，均为必填项</td>
      </tr>
      <tr>
        <td height="20" colspan="2"></td>
        </tr>
    </table>
        <table width="700" border="0" align="center" cellpadding="0" cellspacing="0">
          <tr>
            <td height="15" colspan="4" align="left" class="text_title">详细信息</td>
          </tr>
          <tr>
            <td height="10" colspan="4" ></td>
          </tr>
          
		<tr>
			<td width="20" align="center" class="text_red1">
				<input type="hidden" value="${userinfo.id}" name="id" />
			</td>
			<td width="100" height="40" align="left" class="text_cray1">登录名：</td>
			<td width="350" align="left" class="text_cray1"><input
				name="username" type="text" disabled="true" class="text_cray"
				id="textfield22" value="${userinfo.username}"
				readonly="readonly" /></td>
			<td width="230" colspan="-1" rowspan="7" align="center"
				background="<%=request.getContextPath()%>/images/bg_point_write.gif"
				class="text_cray1">
				<img src="<%=request.getContextPath()%>/photos/${userinfo.imagePath}" width="120">
					<table width="90%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td height="15"></td>
						</tr>
						<tr>
							<td height="7" align="center" class="text_cray">上传照片</td>
						</tr>
						<tr>
							<td height="8"></td>
						</tr>
						<tr>
							<td align="center">
								<input name="uploadFile" type="file" class="text_cray" size="20" />
								<input type="button" value="上传" id="btn_uploadFile" 
								style="position: relative;top: -21px;left: 70px;"/>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		<script>
		document.querySelector("#btn_uploadFile").onclick = () => {
			//表单提交，上传照片，告诉我是成功还是失败，最好回显照片
			//1.获取表单元素
			let form = document.querySelector("#edit_form");
			//2.修改表单的属性：支持进行二进制数据的提交
			form.encoding = "multipart/form-data";
			//3.指定处理上传图片请求的servlet
			form.action = "UploadPhotoServlet";
			//4.表单提交
			form.submit();
			//以下代码将表单属性还原
			//需要修改表单的enctype属性，js中的代码如下：
			form.encoding = "application/x-www-form-urlencoded";
			form.action = "UpdateUserServlet";
		}
          
         </script>
          
          <tr>
            <td width="20" ></td>
            <td width="100" height="40" align="left" class="text_cray1">真实姓名：</td>
            <td align="left" class="text_cray1">
            <input name="realname" type="text"  class="text_cray" id="textfield2" value="${userinfo.realname}" /></td>
          </tr>
          <tr>
            <td width="20" ></td>
            <td width="100" height="30" align="left" class="text_cray1">性 别：</td>
            <td align="left" class="text_cray1">
              <input type="radio" name="sex" value="1" ${userinfo.sex==49?"checked":""} />
          <span class="text_cray">
              <label>男</label>
              <input type="radio" name="sex" value="2" ${userinfo.sex==50?"checked":""} />
              <label>女</label>
              </span>
              <label></label>
<span><label></label>
</span> 
</td>
 </tr>
          <tr>
            <td width="20" align="center" class="text_red">*</td>
            <td width="100" height="40" align="left" class="text_cray1">省份：</td>
            <td align="left" class="text_cray1">
            
 <%-- <input name="province" type="text"  class="text_cray" id="textfield2" value="${userinfo.city.province.provinceName}"/>  --%>
     <%  Users user=(Users)request.getAttribute("userinfo");
     	
     %>       
  <select name="provinceid" class="text_cray" onchange="getCity()" id="province">
  	<%
  	List<Province> provinces =(List<Province>)request.getAttribute("provinces");
  	Iterator it=provinces.iterator();
  	while(it.hasNext())
  	{
  		Province p=(Province)it.next();
  		%>
  		<option value="<%=p.getProvinceNum()%>" 
  		<%=p.getProvinceNum().equals(user.getCity().getProvince().getProvinceNum())?"selected":"" %>>
  		<%=p.getProvinceName() %></option>
  		<% 
  	}
  	%>
  
  </select>    
            	
            	          
            	</td>
          </tr>
          <tr>
            <td width="20" align="center" class="text_red">*</td>
            <td width="100" height="40" align="left" class="text_cray1">城市：</td>
            <td align="left" class="text_cray1">
            
     <%-- <input name="city" type="text" class="text_cray" id="textfield2" value="${userinfo.city.cityName}"/>  --%> 
            <select name="city" class="text_cray" id="city">
            <c:forEach items="${cities}" var="c">
            <option value="${c.id}" ${c.id.equals(userinfo.city.id)?"selected":""}>${c.cityName}</option>
            </c:forEach>
            </select>
            
           	</td>
          </tr>
          <script>
//实例化ajax引擎对象，定义全局变量
var xhr;
function getCity()
{
	//1.获取省份id
	var pid=document.getElementById("province").value;
	
	//2.实例化ajax引擎对象，定义全局变量
	xhr = null;
		if (window.XMLHttpRequest) {// code for all new browsers
			xhr = new XMLHttpRequest();
		} else if (window.ActiveXObject) {// code for IE5 and IE6
			xhr= new ActiveXObject("Microsoft.XMLHTTP");
		}else {
			//alert("Your browser does not support XMLHTTP.");
		}
	//3.调用open方法创建连接
	xhr.open("get","GetCityServlet?pid="+pid,true);
	//4.指定回调函数
	xhr.onreadystatechange=displayCity;
	//5.发送请求
	xhr.send();
	}
	
	//获取服务端响应的信息，把数据取出来放入城市下拉框
	function displayCity()
	{
		 if(xhr.readyState==4)
			{
				if(xhr.status==200)
				{
					//alert("ok");
					//获取响应的xml文档
				  var doc=xhr.responseXML;
					var city_all=doc.getElementsByTagName("city");//这是一个存放所有city的数组
					
					var city_object=document.getElementById("city");//拿到城市下拉框
					city_object.options.length=0;//将城市下拉框清零
					//alert("ok");
					for(var i=0;i<city_all.length;i++)
					{
							var city=city_all[i];//拿到数组中的city对象
							var id=city.childNodes[0].firstChild.nodeValue;
							var name=city.childNodes[1].firstChild.nodeValue;
							//alert(id+"---"+name);
							//给城市下拉框添加选项，其实就是拿到选项然后给选项赋值
							city_object.options[city_object.options.length]=new Option(name,id);
					}  
				}	
			} 
		
	}
	

</script>
           <tr>
            <td width="20" align="center" class="text_red">*</td>
            <td width="100" height="40" align="left" class="text_cray1">证件类型：</td>
            <td align="left" class="text_cray1">
             <select class="text_cray" name="certtype" id="cardType">
                      <option value="1" ${userinfo.certtype.id==1?"selected":""}><span>二代身份证</span>				</option>
                      <option value="2" ${userinfo.certtype.id==2?"selected":""}><span>港澳通行证</span>				</option>
                      <option value="3" ${userinfo.certtype.id==3?"selected":""}><span>台湾通行证</span>				</option>
                      <option value="4" ${userinfo.certtype.id==4?"selected":""}><span>护照</span>				</option>
                  </select>	           </td>
          </tr>
          <tr>
            <td width="20" align="center" class="text_red">*</td>
            <td width="100" height="40" align="left" class="text_cray1">证件号码：</td>
            <td align="left" class="text_cray1"><input name="cert" type="text"  class="text_cray" id="textfield6"  value="${userinfo.cert}"/></td>
          </tr>
          <tr>
            <td width="20" align="center" class="text_red">*</td>
            <td width="100" height="40" align="left" class="text_cray1">出生日期：</td>
            <td colspan="2" align="left" class="text_cray1"><input name="birthday" type="text" class="text_cray" id="textfield7" value="${userinfo.birthday}"/></td>
          </tr>
          <tr>
            <td width="20" height="35" ></td>
            <td width="100" height="40" align="left" class="text_cray1">旅客类型：</td>
            <td height="35" colspan="2" align="left" class="text_cray1"><select class="text_cray" id="passengerType" name="usertype">
              <option value="1" ${userinfo.usertype.id==1?"selected":""}>成人</option>
                <option value="2" ${userinfo.usertype.id==2?"selected":""}>儿童</option>
                <option value="3" ${userinfo.usertype.id==3?"selected":""}>学生</option>
                <option value="4" ${userinfo.usertype.id==4?"selected":""}>残疾军人、伤残人民警察</option>
            </select>            </td>
          </tr>
          <tr>
            <td height="10" colspan="4" align="center"></td>
          </tr>
          <tr>
            <td width="20" ></td>
            <td width="100" height="80" align="left" class="text_cray1">备注：</td>
            <td height="80" colspan="2" align="left" class="text_cray1"><textarea name="content" rows="8" class="text_cray" style="width:100%">${userinfo.content}</textarea>            </td>
          </tr>
        </table><br>
        <table width="100%" border="0" cellspacing="0">
          <tr>
            <td></td>
          </tr>
        </table>
        <table width="700" border="0" align="center" cellpadding="0" cellspacing="0">
          <tr>
            <td width="164"></td>
            <td width="99" height="30" align="center">
            	<input name="button" type="submit" class="buttj" id="button"value="">
            </td>
            <td width="98" >
            </td>
            <td width="97" align="center">
            	<input name="button2" type="reset" class="butcz" id="button2"value="">
           	</td>
            <td width="92" ></td>
          </tr>
        </table>
  </table>
<table width="100%" border="0" cellspacing="0">
  <tr>
    <td height="20"></td>
  </tr>
</table>
<table width="100%" border="0" cellspacing="0">
  <tr>
    <td height="2" background="<%=request.getContextPath()%>/images/bottom_point.gif"></td>
  </tr>
  <tr>
    <td height="25" align="center" background="<%=request.getContextPath()%>/images/bottom_ny_bg.gif" class="text_cray">copyright@12306 购票网</td>
  </tr>
</table>
</td>
  </tr>
</table>
</form>
</body>
</html>