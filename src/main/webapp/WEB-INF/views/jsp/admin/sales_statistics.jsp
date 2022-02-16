<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<!DOCTYPE html >
<html>
<head>
<base href="${base}/" />
<title>后台管理</title>
<meta charset="utf-8" />
<link rel="stylesheet" href="css/admin.css" />
<script type="text/javascript" src="js/jquery-2.0.3.js"></script>
<script type="text/javascript">
	function goPage(p) {
		$("#page").val(p);
		$("#form1").submit();
	}
	$(function() {
		$("#category").val("${goods.categoryId}");
		$("#name").val("${goods.name}");
	});
	
	function delGoods(id) {
		if(confirm("您确认要删除该商品吗？")){
			location.href="./goods/delete?goodsId="+id;
		}
	}
</script>
</head>
<body>
	<div class="container">
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
				<div class="searchbar">
					<form action="./goods/salesStatistics" method="get" id="form1">
						<input type="hidden" name="page" id="page" value="" />
						开始时间：<input class="small" name="startDate" id="startDate" type="text" value="">
						结束时间：<input class="small" name="endDate" id="endDate" type="text" value="">
						分类：<input class="small" name="category" id="category" type="text" value="">
						<button class="btn" type="submit">
							<span class="sel">筛 选</span>
						</button>
					</form>
				</div>

				<div class="field">
					<table class="list_table">
						<col width="40px" />
						<col width="400px" />
						<col width="120px" />
						<col width="70px" />
						<col width="70px" />
						<col width="70px" />
						<thead>
							<tr>
								<th></th>
								<th>商品名称</th>
								<th>销售数量</th>
								<th>订单数量</th>
								<th>单价</th>
								<th>销售收入</th>
<%--								<th>操作</th>--%>
							</tr>
						</thead>
					</table>
				</div>
			</div>
			<form action="" method="post" name="orderForm">
				<div class="content">
					<table class="list_table">
						<col width="40px" />
						<col width="400px" />
						<col width="120px" />
						<col width="70px" />
						<col width="70px" />
						<col width="70px" />
						<tbody>
							<c:forEach items="${pageBean.data}" var="SalesStatistics">
								<tr>
									<td>
<%--										<input name="id[]" type="checkbox" value="1" />--%>
									</td>
									<td><a href="" target="_blank" title="${SalesStatistics.name}">${SalesStatistics.name}</a></td>
									<td>${SalesStatistics.saleNum}</td>
									<td>${SalesStatistics.orderNum}</td>
									<td>${SalesStatistics.price2}</td>
									<td>${SalesStatistics.saleMoney}</td>
<%--									<td><a href="./goods/update?goodsId=${goods.id}">--%>
<%--											<img class="operator" src="images/admin/icon_edit.gif" alt="编辑" /></a>--%>
<%--										<a href="javascript:void(0)" onclick="delGoods('${goods.id}')">--%>
<%--											<img class="operator" src="images/admin/icon_del.gif" alt="删除" /></a></td>--%>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</form>
			<div class='pages_bar'>
				<a href='javascript:goPage(1)' id="first">首页</a>
				<c:forEach begin="1" end="${pageBean.totalPage}" var="p">
					<a href="javascript:goPage('${p}')">${p}</a>
				</c:forEach>
				<select onchange="goPage(this.value)">
					<c:forEach begin="1" end="${pageBean.totalPage}" var="p">
						<option id="indeP" value="${p}">${p}</option>
					</c:forEach>
				</select> <a href='javascript:goPage(${pageBean.totalPage})' id="last">尾页</a>
				<span>当前第${pageBean.page}页/共${pageBean.totalPage}页</span>
			</div>
		</div>
		<div id="separator"></div>
	</div>
</body>
</html>