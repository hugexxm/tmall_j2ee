<%--
  Created by IntelliJ IDEA.
  User: timuya
  Date: 2021/9/5
  Time: 21:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script>
    $(function(){
        $("input.sortBarPrice").keyup(function(){

            var begin = $("input.beginPrice").val();
            var end = $("input.endPrice").val();

            if(begin.length == 0 || end.length == 0){
                $("div.productUnit").show();
                return;
            }

            begin = parseInt(begin);
            if(isNaN(begin))
                begin= 1;
            if(begin<=0)
                begin = 1;
            $(this).val(begin);

            end = parseInt(end);
            if(isNaN(end))
                end= 1;
            if(end<=0)
                end = 1;
            $(this).val(end);

            if(begin > end)
                return;

            console.log(begin);
            console.log(end);
            $("div.productUnit").hide();
            $("div.productUnit").each(function(){
                var price = $(this).attr("price");
                price = new Number(price);

                if(price<=end && price>=begin)
                    $(this).show();
            });
        });
    });
</script>
<div class="categorySortBar">

    <table class="categorySortBarTable categorySortTable">
        <tr>                       <!-- param.sort 跟  request.getParameter("sort") 一样-->
            <td <c:if test="${'all'==param.sort||empty param.sort}">class="grayColumn"</c:if> > <!-- 这里来判断，该td是否有这个class属性。这个属性代表着高亮 -->
                <a href="?cid=${c.id}&sort=all">综合
                    <span class="glyphicon glyphicon-arrow-down"></span>
                </a>
            </td>
            <td <c:if test="${'review'==param.sort}">class="grayColumn"</c:if> >
                <a href="?cid=${c.id}&sort=review">人气
                    <span class="glyphicon glyphicon-arrow-down"></span>
                </a>
            </td>
            <td <c:if test="${'date'==param.sort}">class="grayColumn"</c:if>>
                <a href="?cid=${c.id}&sort=date">新品
                    <span class="glyphicon glyphicon-arrow-down"></span>
                </a>
            </td>
            <td <c:if test="${'saleCount'==param.sort}">class="grayColumn"</c:if>>
                <a href="?cid=${c.id}&sort=saleCount">销量
                    <span class="glyphicon glyphicon-arrow-down"></span>
                </a>
            </td>
            <td <c:if test="${'price'==param.sort}">class="grayColumn"</c:if>>
                <a href="?cid=${c.id}&sort=price">价格
                    <span class="glyphicon glyphicon-resize-vertical"></span>
                </a>
            </td>
        </tr>
    </table>

    <table class="categorySortBarTable">
        <tr>
            <td><input class="sortBarPrice beginPrice" type="text" placeholder="请输入"></td>
            <td class="grayColumn priceMiddleColumn">-</td>
            <td><input class="sortBarPrice endPrice" type="text" placeholder="请输入"></td>
        </tr>
    </table>

</div>
