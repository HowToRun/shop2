<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8"></meta>
<base href="${base}/" />
<title>提交订单-球鞋商城</title>
<jsp:include page="base.jsp"></jsp:include>
<script type="text/javascript">
	function finish() {
		$.get("./user/moneyLeftEnough?orderId=${order.id}", function(r) {
			if (r == "0") {
				alert("订单提交失败，账户余额不足!");
			} else {
				$("#form1").submit();
			}
		});
	}
	function finish2() {
		<%--$.get("./user/moneyLeftEnough?orderId=${order.id}", function(r) {--%>
		<%--	if (r == "0") {--%>
		<%--		alert("订单提交失败，账户余额不足!");--%>
		<%--	} else {--%>
		<%--		$("#form1").submit();--%>
		<%--	}--%>
		<%--});--%>

	}
</script>
</head>
<body class="second">
	<div class="brand_list container_2">
		<jsp:include page="header.jsp" />

		<div class="wrapper clearfix">
			<div class="position mt_10">
				<span>您当前的位置：</span> <a href=""> 首页</a> » 成功提交订单
			</div>
			<div class="myshopping m_10">
				<ul class="order_step">
					<li><span class="first">1、查看购物车</span></li>
					<li class="current_prev"><span>2、填写核对订单信息</span></li>
					<li class="last_current"><span>3、成功提交订单</span></li>
				</ul>
			</div>

			<div class="cart_box m_10">
				<div class="title">成功提交订单</div>
				<div class="cont">
					<p class="order_stats">
						 <img width="48px"
							height="51px" alt="" src="images/front/right.gif"><strong
							class="f14">订单已提交</strong>
					</p>

					<div class="stats_box">
						<h3>订单信息</h3>
						<table width="100%" class="form_table t_l orange">
							<col width="75px" />
							<col />

							<tbody>
							<c:if test="${order.orderDetail.payStatus eq 0}"> 
								<tr>
									<th>订单编号：</th>
									<td class="f18 bold red2">${order.id }</td>
								</tr>
							</c:if>
								<tr>
									<th>订单金额：</th>
									<td class="f18 bold red2">￥<b>${order.totalMoney }</b></td>
								</tr>
								<tr>
									<th>支付方式：</th>
									<td class="f18 bold red2">${order.payType }</td>
								</tr>
								<tr>
									<th>配送方式：</th>
									<td class="f18 bold red2">${order.deliveryType}</td>
								</tr>
							</tbody>
						</table>


						<div class="blue_box gray m_10" id='order_detail'>
							<%--style='display: none'>--%>
							<table class="form_table t_l">
								<col width="80px" />
								<col />
								<tbody>
									<tr>
										<td class="t_r">收货人姓名：</td>
										<td>${address.accept}</td>
									</tr>
									<tr>
										<td class="t_r">联系方式：</td>
										<td>${address.phoneNum}</td>
									</tr>
									<tr>
										<td class="t_r">配送方式：</td>
										<td>${order.deliveryType}</td>
									</tr>
									<tr>
										<td class="t_r">收货时间：</td>
										<td>${order.deliveryTime}</td>
									</tr>
								</tbody>
							</table>
						</div>

						<!--不是货到付款并且支付方式为线上支付-->
						<!-- 先检查用户余额是否足够 -->
						
<%-- 					<c:choose> --%>
						<!--待收货  -->
						<c:if test="${order.orderDetail.orderStatus eq 3}">
							<c:if test="${order.payType eq '货到付款'}">
								<a href="./order/updateorderStatus?orderId=${order.id }">
									<input class="ate" type="button" value="确认收货并付款" />
								</a>
							</c:if>
							<c:if test="${order.payType eq '预存款支付'}">
								<a href="./order/updateorderStatus?orderId=${order.id }">
									<input class="ate" type="button" value="确认收货" />
								</a>
							</c:if>

 						</c:if>
 						<!--代付款 -->
 						<c:if test="${order.orderDetail.payStatus eq 0}">
							<c:if test="${order.payType eq '货到付款'}">
								<a href="./order/pay?orderId=${order.id }">
									<input style="width:80px;height: 40px" type="button" onclick="javascript:finish2();" value="完成" />
								</a>
							</c:if>
							<c:if test="${order.payType eq '预存款支付'}">
								<a href="./order/pay?orderId=${order.id }">
									<input class="submit_pay" onclick="javascript:finish();" value="立即支付" />
								</a>
							</c:if>

 						</c:if>
<%-- 						<c:otherwise>  --%>
<%-- 						</c:otherwise> --%>
<%-- 					</c:choose> --%>
					
					</div>
				</div>
			</div>
		</div>
		<jsp:include page="footer.jsp" />
	</div>
</body>
</html>
