<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8" />
<base href="${base}/" />
<title>首页_球鞋商城</title>
<jsp:include page="base.jsp" />
</head>

<body class="index">
	<div class="container">
		<jsp:include page="header.jsp"></jsp:include>
		<jsp:include page="navbar.jsp"></jsp:include>
		<jsp:include page="search.jsp"></jsp:include>
		<div id="hei" style="visibility:hidden;">${sessionScope.user.password}</div>
		<div class="wrapper clearfix">
			<div class="sidebar f_r">
				<!--热卖商品-->
				<div class="hot box m_10">
					<div class="title">
						<h2>热卖商品</h2>
					</div>
					<div class="cont clearfix">
						<ul class="prolist">
                            <!--此处热门商品为空-->
                            <c:forEach items="${hotGoodses}" var="goods">
								<li><a href="./goods/view?goodsId=${goods.id}" target="_blank">
                                        <img src="${goods.thumbnail}" width="80" height="80" alt="" /></a>
									<p class="pro_title">
										<a title="${goods.name}" href="./goods/view?goodsId=${goods.id}" target="_blank">${goods.name}</a></p>
									<p class="brown"><b>￥${goods.price2}</b></p></li>
							</c:forEach>
						</ul>
					</div>
				</div>
				<!--热卖商品-->
			</div>


			<h2></h2>
			<div class="main f_l">
				<!--最新商品 start-->
				<div class="box yellow m_10">
					<div class="title title3">
						<h2>
							<img src="images/front/new_product.gif" alt="最新商品" width="160"
								height="36" />
						</h2>
					</div>
					<div class="cont clearfix">
						<ul class="prolist">

							<%--<c:if test="${goodsesLasted==null}">--%>
                                <%--<h2>goodsesLasted = null</h2>--%>
                            <%--</c:if>--%>

							<c:forEach items="${goodsesLasted}" var="goods">
								<li style="overflow: hidden">
									<!--图片
										加上" target="_blank" "则跳转到新页面-->
									<a href="./goods/view?goodsId=${goods.id}" target="_blank"><img
									<%--<a href="./goods/view?goods.id=${goods.id}" target="_blank"><img--%>
										src="${goods.thumbnail}" width="170" height="170" alt="" /></a>
									<p class="pro_title">
										<!--标题-->
										<a title="" href="./goods/view?goodsId=${goods.id}" target="_blank">${goods.name}</a>
									</p>
									<p class="brown">
										惊喜价：<b>￥${goods.price2}</b>
									</p>
									<p class="light_gray">
										市场价：<s>￥${goods.price1}</s>
									</p></li>
							</c:forEach>
						</ul>
					</div>
				</div>
				<!--最新商品 end-->

                <!--商品分类展示 start-->
                <!--商品分类统计-->
                <div class="category box">
                    <div class="title2">
                        <h2>
                            <img src="images/front/category.gif" alt="商品分类" width="155" height="36" />
                        </h2>
                    </div>
                </div>

                <table id="index_category" class="sort_table m_10" width="100%">
                    <tr>
                        <td><c:forEach items="${categories}" var="category">
                            <a href="./goods/listByCate?goodsCategoryId=${category.id}&order=sellnum" target="_blank">
                                    ${category.name}（${category.goodsNum}）</a>|
                        </c:forEach></td>
                    </tr>
                </table>


                <!--分类显示商品-->
                <c:forEach items="${categories}" var="category">
					<div class="box m_10 green" name="showGoods">
						<div class="title title3">
							<h2>
								<a href=""><strong>${category.name}</strong></a>
							</h2>
							<%--<a class="more" href="./goods/listByCate?goods.categoryId=${category.id}&order=sellnum">更多商品...</a>--%>
							<a class="more" href="./goods/listByCate?goodsCategoryId=${category.id}&order=sellnum" target="_blank">更多商品...</a>
						</div>

						<div class="cont clearfix">
							<ul class="prolist">
                                <c:forEach items="${category.goodses}" var="goods">
									<li><a href="./goods/view?goodsId=${goods.id}" target="_blank">
                                        <img src="${goods.thumbnail}" width="175" height="175" alt="" title=""></a>
										<p class="pro_title">
											<a title="${goods.name}" href="./goods/view?goodsId=${goods.id}"
                                               target="_blank">${goods.name}</a>
										</p>
										<p class="brown">
											惊喜价：<b>￥${goods.price2}</b>
										</p>
										<p class="light_gray">
											市场价：<s>￥${goods.price1}</s>
										</p></li>
								</c:forEach>
							</ul>
						</div>
					</div>
				</c:forEach>
                <!-- 分类显示商品 end-->


              <!--限时秒杀start-->
<%--				<div class="comment box m_10" id="sha">--%>
<%--					<div class="title title3">--%>
<%--						<h2>--%>
<%--							<div id = "miaosha" style="height: 36px;width: 155px;color:#F64007 " >限时秒杀</div>--%>
<%--							<div id = "daojishi"--%>
<%--								 style="float:left;height: 36px;width: 200px;--%>
<%--								 margin-left: 134px;margin-top: -31px;font-size: 20px;--%>
<%--								 background-color: #87FF7E;line-height: 36px"></div>--%>
<%--							&lt;%&ndash;<img src="images/front/comment.gif" alt="限时秒杀" width="155"--%>
<%--								height="36" />&ndash;%&gt;--%>
<%--						</h2>--%>
<%--					</div>--%>
<%--					<div class="cont clearfix" >--%>
<%--						<c:forEach begin="6" end="7" varStatus="s">--%>
<%--							<dl class="no_bg">--%>
<%--								<dt>--%>
<%--									<a href=""><img src='images/goods/apple${s.index}.jpg' width="66"--%>
<%--										height="66"></a>--%>
<%--								</dt>--%>
<%--								<dd>--%>
<%--									<a class="qwe123" href="javascript:;" onclick="miaosha(${s.index})"></a>--%>
<%--								</dd>--%>
<%--								<dd>--%>
<%--									<span class="grade"><i style="width: 42px"></i></span>--%>
<%--								</dd>--%>
<%--								<dd class="com_c">限时秒杀哦！！！赶紧抢</dd>--%>
<%--							</dl>--%>
<%--						</c:forEach>--%>
<%--					</div>--%>
<%--				</div>--%>
				<!--限时秒杀 end-->
			</div>
		</div> 
		<jsp:include page="help.jsp"></jsp:include>
		<jsp:include page="footer.jsp"></jsp:include>
	</div>
	<script >
		$(document).ready(function(){
			showTime();
			var timer = setInterval(showTime,1000);
			function showTime(){
				var end = Date.parse('2020/06/30');
				// 获取当前时间
				var now = Date.now();
				// 计算差值
				var offset = Math.floor((end - now)/1000);//毫秒

				if(offset <= 0){
					// 清除定时器
					$("#sha").hide();
					clearInterval(timer);
				}

				var sec = offset%60;
				var min = Math.floor(offset/60)%60;
				var hour = Math.floor(offset/60/60)%24;
				var day = Math.floor(offset/60/60/24);

				sec = sec<10? '0'+sec : sec;
				min = min<10? '0'+min : min;
				hour = hour<10? '0'+hour : hour;
				day = day<10? '0'+day : day;

				$("#daojishi").html(day + '天' + hour + '时' + min + '分' + sec + '秒');


				//document.querySelector('p').innerHTML = day + '天' + hour + '时' + min + '分' + sec + '秒';
			}

				$(".qwe123").each(function (index) {
				if(index===1){
					$(this).html("华为 HUAWEI Mate 30 5G 麒麟990 4000万超感光徕卡影像双超级快充8GB+128GB星河银5G全网通游戏手机");
				}else {
					$(this).html("华为 HUAWEI P30 超感光徕卡三摄麒麟980AI智能芯片全面屏屏内指纹版手机8GB+128GB亮黑色全网通双4G手机");
				}

				
			})

		});

		function miaosha(eg){
			console.log($("#hei").html());
			debugger
		if($("#hei").html()==""){
			alert("您不是会员不能参与秒杀，请先登录！")
			window.location.href="./user/toLogin"
		}else{
			if (eg=="6"){
				
				window.location.href="./goods/view?goodsId=44ade9cbc2c04bb8bf6666342e54b285";
		             
			}else{
				window.location.href="./goods/view?goodsId=44ade9cbc2c04bb8bf6666342e54b285";
			}
			
		}
			
	}

	</script>

</body>
</html>