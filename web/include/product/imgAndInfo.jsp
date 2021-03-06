<%--
  Created by IntelliJ IDEA.
  User: timuya
  Date: 2021/9/5
  Time: 20:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false"%>

<script>

    $(function(){
        var stock = ${p.stock};
        $(".productNumberSetting").keyup(function(){
            var num= $(".productNumberSetting").val();
            num = parseInt(num);
            if(isNaN(num))
                num= 1;
            if(num<=0)
                num = 1;
            if(num>stock)
                num = stock;
            $(".productNumberSetting").val(num);
        });

        $(".increaseNumber").click(function(){
            var num= $(".productNumberSetting").val();
            num++;
            if(num>stock)
                num = stock;
            $(".productNumberSetting").val(num);
        });
        $(".decreaseNumber").click(function(){
            var num= $(".productNumberSetting").val();
            --num;
            if(num<=0)
                num=1;
            $(".productNumberSetting").val(num);
        });

        $(".addCartLink").click(function(){
            var page = "forecheckLogin";
            $.get(
                page,
                function(result){
                    if("success"==result){
                        var pid = ${p.id};
                        var num= $(".productNumberSetting").val();
                        var addCartpage = "foreaddCart";
                        $.get(
                            addCartpage,
                            {"pid":pid,"num":num},
                            function(result){
                                var jsonResult = JSON.parse(result);
                                var ifSuccess = jsonResult.ifSuccess;
                                var cartTotalItemNumber = jsonResult.cartTotalItemNumber;
                                if("success"==ifSuccess){
                                    $(".cartTotal").html(cartTotalItemNumber);
                                    //location.reload();
                                    // $(".addCartButton").html("??????????????????");
                                    // $(".addCartButton").attr("disabled","disabled");
                                    // $(".addCartButton").css("background-color","lightgray");
                                    // $(".addCartButton").css("border-color","lightgray");
                                    // $(".addCartButton").css("color","black");

                                }
                                else{

                                }
                            }
                        );
                    }
                    else{
                        $("#loginModal").modal('show');
                    }
                }
            );
            return false;
        });
        $(".buyLink").click(function(){
            var page = "forecheckLogin";
            $.get(
                page,
                function(result){
                    if("success"==result){
                        var num = $(".productNumberSetting").val();
                        location.href= $(".buyLink").attr("href")+"&num="+num; // ???????????????????????????????????????????????????
                    }
                    else{
                        $("#loginModal").modal('show');
                    }
                }
            );
            return false;
        });

        $("button.loginSubmitButton").click(function(){
            var name = $("#name").val();
            var password = $("#password").val();

            if(0==name.length||0==password.length){
                $("span.errorMessage").html("?????????????????????");
                $("div.loginErrorMessageDiv").show();
                return false;
            }

            var page = "foreloginAjax";
            $.get(
                page,
                {"name":name,"password":password},
                function(result){
                    if("success"==result){
                        location.reload(); // ???????????????????????????????????????????????????????????????
                    }
                    else{
                        $("span.errorMessage").html("??????????????????");
                        $("div.loginErrorMessageDiv").show();
                    }
                }
            );

            return true;
        });

        $("img.smallImage").mouseenter(function(){
            var bigImageURL = $(this).attr("bigImageURL");
            $("img.bigImg").attr("src",bigImageURL);
        }); // ???????????????????????????????????????????????????????????????????????????

        $("img.bigImg").load(
            function(){
                $("img.smallImage").each(function(){
                    var bigImageURL = $(this).attr("bigImageURL");
                    img = new Image();
                    img.src = bigImageURL;

                    img.onload = function(){
                        console.log(bigImageURL);
                        $("div.img4load").append($(img));
                    };
                });
            }
        );
    });

</script>

<div class="imgAndInfo">

    <div class="imgInimgAndInfo">
        <img src="img/productSingle/${p.firstProductImage.id}.jpg" class="bigImg">
        <div class="smallImageDiv">
            <c:forEach items="${p.productSingleImages}" var="pi"> <!-- ??????????????????????????????????????????????????????????????????????????????id -->
                <img src="img/productSingle_small/${pi.id}.jpg" bigImageURL="img/productSingle/${pi.id}.jpg" class="smallImage">
            </c:forEach>
        </div>
        <div class="img4load hidden" ></div>
    </div>

    <div class="infoInimgAndInfo">

        <div class="productTitle">
            ${p.name}
        </div>
        <div class="productSubTitle">
            ${p.subTitle}
        </div>

        <div class="productPrice">
            <div class="juhuasuan">
                <span class="juhuasuanBig" >?????????</span>
                <span>?????????????????????????????????<span class="juhuasuanTime">1???19??????</span>????????????</span>
            </div>
            <div class="productPriceDiv">
                <div class="gouwujuanDiv"><img height="16px" src="img/site/gouwujuan.png">
                    <span> ???????????????????????????</span>

                </div>
                <div class="originalDiv">
                    <span class="originalPriceDesc">??????</span>
                    <span class="originalPriceYuan">??</span>
                    <span class="originalPrice">

                        <fmt:formatNumber type="number" value="${p.orignalPrice}" minFractionDigits="2"/>
                    </span>
                </div>
                <div class="promotionDiv">
                    <span class="promotionPriceDesc">????????? </span>
                    <span class="promotionPriceYuan">??</span>
                    <span class="promotionPrice">
                        <fmt:formatNumber type="number" value="${p.promotePrice}" minFractionDigits="2"/>
                    </span>
                </div>
            </div>
        </div>
        <div class="productSaleAndReviewNumber">
            <div>?????? <span class="redColor boldWord"> ${p.saleCount }</span></div>
            <div>???????????? <span class="redColor boldWord"> ${p.reviewCount}</span></div>
        </div>
        <div class="productNumber">
            <span>??????</span>
            <span>
                <span class="productNumberSettingSpan">
                <input class="productNumberSetting" type="text" value="1">
                </span>
                <span class="arrow">
                    <a href="#nowhere" class="increaseNumber">
                    <span class="updown">
                            <img src="img/site/increase.png">
                    </span>
                    </a>

                    <span class="updownMiddle"> </span>
                    <a href="#nowhere"  class="decreaseNumber">
                    <span class="updown">
                            <img src="img/site/decrease.png">
                    </span>
                    </a>

                </span>

            ???</span>
            <span>??????${p.stock}???</span>
        </div>
        <div class="serviceCommitment">
            <span class="serviceCommitmentDesc">????????????</span>
            <span class="serviceCommitmentLink">
                <a href="#nowhere">????????????</a>
                <a href="#nowhere">????????????</a>
                <a href="#nowhere">????????????</a>
                <a href="#nowhere">?????????????????????</a>
            </span>
        </div>

        <div class="buyDiv">
            <!-- ?????????,???????????????????????? num ?????? -->
            <a class="buyLink" href="forebuyone?pid=${p.id}"><button class="buyButton">????????????</button></a>
            <!-- ???????????????Ajax -->
            <a href="#nowhere" class="addCartLink"><button class="addCartButton"><span class="glyphicon glyphicon-shopping-cart"></span>???????????????</button></a>
        </div>
    </div>

    <div style="clear:both"></div>

</div>
