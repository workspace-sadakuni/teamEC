<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width,initial-scale=1.0">
<link rel="stylesheet" type="text/css" href="./css/productList.css">
<link href="./css/productList.css" rel="stylesheet" type="text/css" media="(min-width: 320px) and (max-width: 599px)" >
<link rel="stylesheet" type="text/css" href="./css/style.css">
<title>商品検索結果画面</title>
</head>
<body>
<jsp:include page="header.jsp" flush="true" />

<div id="main">
	<div class="top">
		<h1>商品一覧画面</h1>
	</div>

<!-- もし,エラーメッセージがあれば表示 -->
	<s:if test="keywordsErrorMessageList!=null && keywordsErrorMessageList.size()>0">
		<div class="error">
		<div class="error-message">
			<s:iterator value="keywordsErrorMessageList"><s:property /><br></s:iterator>
		</div>
		</div>
	</s:if>

<!-- 商品情報リスト情報があれば、一つずつ取得 -->
		<s:elseif test="ProductInfoList!=null && ProductInfoList.size()>0">
	<div class="product-list">
	<s:iterator value="ProductInfoList" status="st">

			<div class="product">
				<a href='<s:url action="ProductDetailsAction"><s:param name="product_id" value="%{productId}"/></s:url>'>
					<img src='<s:property value="imageFilePath"/>' class="item-image-box-200"/><br>
					<s:property value="productName"/><br>
					<s:property value="productNameKana"/><br>
					<s:property value="price"/>円<br>
				</a>
			</div>

	</s:iterator>
	</div>
	</s:elseif>

<!-- 検索結果がなければ、messageに値があるはずなので、表示。 -->
	<s:if test="message!=null">
		<div class="info">
			<s:property value="message"/>
		</div>
	</s:if>

</div>

</body>
</html>