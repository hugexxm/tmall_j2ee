<%--
  Created by IntelliJ IDEA.
  User: timuya
  Date: 2021/9/5
  Time: 16:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false"%>

<script>
    $(function(){
        $("div.productsAsideCategorys div.row a").each(function(){
            var v = Math.round(Math.random() *6);
            if(v == 1)
                $(this).css("color","#87CEFA");
        });
    });

</script>
<c:forEach items="${cs}" var="c"> <!-- 取出每一个分类 -->
    <div cid="${c.id}" class="productsAsideCategorys">

        <c:forEach items="${c.productsByRow}" var="ps">  <!-- 取出每一个products集合 ，每一行-->
            <div class="row show1">
                <c:forEach items="${ps}" var="p">  <!-- 取出每一个product ， 每一个-->
                    <c:if test="${!empty p.subTitle}">
                        <a href="foreproduct?pid=${p.id}">
                            <c:forEach items="${fn:split(p.subTitle, ' ')}" var="title" varStatus="st">
                                <c:if test="${st.index==0}">
                                    ${title} <!-- 显示第一个title -->
                                </c:if>
                            </c:forEach>
                        </a>
                    </c:if>
                </c:forEach>
                <div class="seperator"></div>
            </div>
        </c:forEach>
    </div>
</c:forEach>

