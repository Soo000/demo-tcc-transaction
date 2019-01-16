<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.kkwrite.demo.order.entity.ProductDO" %>
<%@ page import="java.util.List" %>
<%@ page import="java.math.BigDecimal" %>
<!DOCTYPE html>
<html lang="zh-CN">
    <head>
        <meta charset="UTF-8">
        <title>商品列表</title>
    </head>
    <body>
        <div class="page">
            <div class="bg-f">
                <ul class="list" >
                    数量：${products.size()}
                    <%
                        long userId = (long) request.getAttribute("userId");
                        long shopId = (long) request.getAttribute("shopId");
                        List<ProductDO> products = (List) request.getAttribute("products");
                        if (products.size() > 0) {
                            for (ProductDO product: products) {
                                String productName = product.getProductName();
                                BigDecimal price = product.getPrice();
                    %>
                                <li class="list-item">
                                    <p>
                                        <%=product.getProductName()%>(<%=price%>)
                                            &nbsp;&nbsp;&nbsp;&nbsp;
                                            <span>
                                                <a href="<%=request.getContextPath()%>/user/${userId}/shop/${shopId}/product/<%=product.getProductId()%>/confirm">
                                                    购买
                                                </a>
                                            </span>
                                    </p>
                                </li>
                    <%
                            }
                        }
                    %>
                </ul>
            </div>
        </div>
    </body>
</html>
