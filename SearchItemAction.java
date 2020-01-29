package com.internousdev.mocha.action;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.mocha.dao.MCategoryDAO;
import com.internousdev.mocha.dao.ProductInfoDAO;
import com.internousdev.mocha.dto.MCategoryDTO;
import com.internousdev.mocha.dto.ProductInfoDTO;
import com.internousdev.mocha.util.InputChecker;
import com.opensymphony.xwork2.ActionSupport;

public class SearchItemAction extends ActionSupport implements SessionAware{

	public Map<String, Object> session;
	private MCategoryDAO mCategoryDAO = new MCategoryDAO();
	private List<MCategoryDTO> mCategoryList = new ArrayList<MCategoryDTO>();
	private List<ProductInfoDTO> ProductInfoList = new ArrayList<ProductInfoDTO>();

	private String message;
	private String searchProductName;
	private String categoryId;
	private List<String> keywordsErrorMessageList;

	private String cateId;

	public String execute() throws SQLException {
		String ret = SUCCESS;

//	何も入力されてなければ、メソッドの引数として使用できないため、空文字を代入。
//	Emptyメソッドは はfalse(Empty=null),Blankメソッドは はtrueとみなす
		if(StringUtils.isEmpty(searchProductName)) {
			searchProductName = "";
		}else {

//			入力された値が50文字以上でないかなどをチェックし、正規表現でなければエラーメッセージを代入。

			InputChecker inputChecker = new InputChecker();
			keywordsErrorMessageList = inputChecker.doCheck("検索ワード", searchProductName,0,50, true, true, true, true, true, true);

			if (keywordsErrorMessageList.size() > 0) {

				return ret;
			}

			// キーワードの"　"を" "に変換して" "2個以上を" "に変換し、前後のスペースを削除
			searchProductName = searchProductName.replaceAll("　", " ").replaceAll("\\s{2,}", " ").trim();
		}

		// カテゴリーの選択肢が存在しない場合は、すべてのカテゴリーを設定する
		if (categoryId == null) {
			categoryId = "1";
		}

//			入力された値を半角スペースで区切り、検索を商品情報テーブルにかける。
		ProductInfoDAO productInfoDAO = new ProductInfoDAO();
		switch (categoryId) {
		case "1":
			ProductInfoList = productInfoDAO.getProductInfo(searchProductName.split(" "));
			break;

//			カテゴリIDが1(全カテゴリー以外を選択された場合、選択カテゴリ、入力された値から情報を取得)
		default:
			ProductInfoList = productInfoDAO.getProductInfoListByCategoryIdAndKeyword(categoryId, searchProductName.split(" "));
			break;
		}

		if(ProductInfoList.size() <= 0) {
			setMessage("検索結果がありません。");
		}

//		下記の処理でsessionにカテゴリー情報がない場合、すべて代入
		if(session.get("Category") == null) {
			mCategoryList = mCategoryDAO.getCategoryInfo();
			session.put("Category",mCategoryList);
		}
		cateId=categoryId;
		return ret;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public List<MCategoryDTO> getMcategoryList() {
		return this.mCategoryList;
	}

	public List<ProductInfoDTO> getProductInfoList() {
		return ProductInfoList;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setKeywordsErrorMessageList(List<String> keywordsErrorMessageList) {
		this.keywordsErrorMessageList = keywordsErrorMessageList;
	}

	public List<String> getKeywordsErrorMessageList() {
		return keywordsErrorMessageList;
	}

	public String getSearchProductName() {
		return searchProductName;
	}

	public void setSearchProductName(String searchProductName) {
		this.searchProductName = searchProductName;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getCateId() {
		return cateId;
	}

}
