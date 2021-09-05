<%--
  Created by IntelliJ IDEA.
  User: timuya
  Date: 2021/9/5
  Time: 16:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false"%>

<title>模仿天猫官网</title>

<div class="homepageDiv">
    <%@include file="categoryAndcarousel.jsp"%>
    <%@include file="homepageCategoryProducts.jsp"%>
</div>



<!--
1. categoryAndcarousel.jsp
分类和轮播
1.1 categoryMenu.jsp
竖状分类菜单
1.2 productsAsideCategorys.jsp
竖状分类菜单右侧的推荐产品列表
1.3 carousel.jsp
轮播
2. homepageCategoryProducts.jsp
主题的17种分类以及每种分类对应的5个产品
-->