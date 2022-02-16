<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html >
<html>
<head>
<base href="${base}/" />
<title>后台管理</title>
<meta charset="utf-8" />
<link rel="stylesheet" href="css/admin.css" />

<%--<link href="js/bootstrap-3.3.4-dist/css/bootstrap.min.css"--%>
	<%--rel="stylesheet">--%>
<%--<script src="js/bootstrap-3.3.4-dist/jquery-2.1.4.min.js"></script>--%>
<%--<script src="js/bootstrap-3.3.4-dist/js/bootstrap.min.js"></script>--%>
<script type="text/javascript" src="js/jquery-2.0.3.js"></script>
<script type="text/javascript">
	function delCategory(id) {
		if(confirm("您确认要删除该商品种类？")){
		    location.href="./category/delete?categoryId="+id;
		}
    }
</script>

</head>
<body>
	<div class="container1">
		<div id="header">
			<jsp:include page="header.jsp"></jsp:include>
		</div>
		<div id="admin_left">
			<ul class="submenu">
				<jsp:include page="left.jsp"></jsp:include>
			</ul>
			<div id="copyright"></div>
		</div>

		<div id="admin_right">
			<div class="headbar">
				<div class="field">
					<table class="list_table">
						<col width="50px" />
						<col width="100px" />
						<col width="100px" />
						<col width="100px" />
						<thead>
							<tr>
								<th>序号</th>
								<th>分类名称</th>
								<th>商品数量</th>
								<th>操作</th>
							</tr>
						</thead>
					</table>
				</div>
			</div>

			<div class="content1">
				<table id="list_table" class="list_table">
					<col width="50px" />
					<col width="100px" />
					<col width="100px" />
					<col width="100px" />
					<tbody>
						<c:forEach items="${categories}" var="category" varStatus="s">
							<tr>
								<td>${s.count}</td>
									
								<td>${category.name}</td>
								<td>${category.goodsNum}</td>
								<td><a class="update" data-update="${category.id}" data-name="${category.name}"
										data-toggle="modal" data-target="#myModal">
										<img class="operator" src="images/admin/icon_edit.gif" alt="修改"
										title="修改" /></a>
									<a href="javascript:void(0)" onclick="delCategory('${category.id}')">
										<img class="operator" src="images/admin/icon_del.gif" alt="删除" title="删除" /></a></td>
									<%--<a href="./category/delete?categoryId=${category.id}"><img--%>
										<%--class="operator" src="images/admin/icon_del.gif" alt="删除"--%>
										<%--title="删除" /></a></td>--%>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>

		</div>
		<div id="separator"></div>
	</div>

	<!-- 模态框（Modal） -->
<!-- 	<div class="modal fade" id="myModal" tabindex="-1" role="dialog" -->
<!-- 		aria-labelledby="myModalLabel" aria-hidden="true"> -->
<!-- 		<div class="modal-dialog"> -->
<!-- 			<div class="modal-content"> -->
<!-- 				<div class="modal-header"> -->
<!-- 					<button type="button" class="close" data-dismiss="modal" -->
<!-- 						aria-hidden="true">&times;</button> -->
<!-- 					<h4 class="modal-title" id="myModalLabel">修改商品类别</h4> -->
<!-- 				</div> -->
<!-- 				<div class="modal-body"> -->
<!-- 					<div style="padding: 10px 100px 10px;"> -->
<!-- 						<form id="updateCategoryForm" action="./category/refresh" -->
<!-- 							class="bs-example bs-example-form" role="form"> -->
<!-- 							<div class="input-group"> -->
<!-- 							<span class="input-group-addon" id="name"></span> -->
<!-- 								<span class="input-group-addon">修改为</span> -->
<!-- 								<input id="updateCategory" type="text" class="form-control" -->
<%-- 									name="category.name"> <label id="catemsg">${msg }</label> --%>
<!-- 							</div> -->
<!-- 							<br> -->
<!-- 							<div class="modal-footer"> -->
<!-- 								<button type="submit" class="btn btn-primary" onclick="tijiao()">提交更改</button> -->
<!-- 							</div> -->
<!-- 						</form> -->
<!-- 					</div> -->
<!-- 				</div> -->

<!-- 			</div> -->
<!-- 		</div> -->
</body>

<script type="text/javascript" src="js/jquery.validate-1.13.1.js"></script>
<script type="text/javascript">

	$(function() {
		//获取class为caname的元素 
		$(".update").click(function() {
			var td = $(this).parent().parent().children("td").eq(1);
			//console.log($(this).parent().parent().children("td").eq(1).text());
			var txt = td.text();
			var categoryId = $(this).attr("data-update");
			var input = $("<input type='text'value='" + txt + "'/>");
			td.html(input);
			input.click(function() {
				return false;
			});
			//获取焦点 
			input.trigger("focus");
			//文本框失去焦点后提交内容，重新变为文本 
			input.blur(function() {
			
				var newtxt = $(this).val();
				//判断文本有没有修改 
				if (newtxt != txt) {
					td.html(newtxt);
					 var caid = $.trim(td.prev().text()); 
					 //ajax异步更改数据库,加参数date是解决缓存问题 
					 var url = "category/update?updateCategory=" + newtxt + "&categoryId=" + categoryId;					 
					 //数据库的修改就在一般处理程序中完成 
					 $.get(url, function(data) { 
					 if(data=="1"){ 
					 	alert("该类别已存在！"); 
					 	td.html(txt); 
					 	return; 
					 } 
					 //alert(data); 
					 	td.html(newtxt); 
					 }); 
					
				} else {
					td.html(newtxt);
				}
			});
		});
	});
</script>

</html>
