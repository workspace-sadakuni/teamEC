package com.internousdev.mocha.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.mocha.dto.MCategoryDTO;
import com.internousdev.mocha.util.DBConnector;

public class MCategoryDAO implements SessionAware{

	private DBConnector dbConnector = new DBConnector();
	private Connection con = dbConnector.getConnection();
	private String message;
	public Map<String, Object> session;

//	カテゴリー検索メソッド
	public List<MCategoryDTO> getProductInfo (String categoryId) throws SQLException {
		List<MCategoryDTO> mProductDTO = new ArrayList<MCategoryDTO>();

		String sql = "SELECT product_id,product_name,product_name_kana,image_file_path,price,category_id FROM product_info WHERE category_id = ?";

		try {
			PreparedStatement preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1,categoryId);
			ResultSet resultSet = preparedStatement.executeQuery();

			while(resultSet.next()) {
				MCategoryDTO dto = new MCategoryDTO();
				dto.setProductId(resultSet.getString("product_id"));
				dto.setProductName(resultSet.getString("product_name"));
				dto.setProductNameKana(resultSet.getString("product_name_kana"));
				dto.setImageFilePath(resultSet.getString("image_file_path"));
				dto.setPrice(resultSet.getString("price"));
				dto.setCategoryId(resultSet.getString("category_id"));
				mProductDTO.add(dto);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			con.close();
		}
		return mProductDTO;
	}

//上記getProductInfoで商品情報テーブルの商品名、かな読み,カテゴリーIDから値取得。

//下記getCategoryInfoメソッドでcategoryテーブルからID,名前全件取得

	public List<MCategoryDTO> getCategoryInfo () throws SQLException {
		List<MCategoryDTO> mCategoryDTO = new ArrayList<MCategoryDTO>();

		String sql = "SELECT * FROM m_category";

		try {
			PreparedStatement preparedStatement = con.prepareStatement(sql);

			ResultSet resultSet = preparedStatement.executeQuery();

			while(resultSet.next()) {
				MCategoryDTO dto = new MCategoryDTO();
				dto.setCategoryId(resultSet.getString("category_id"));
				dto.setCategoryName(resultSet.getString("category_name"));
				mCategoryDTO.add(dto);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			con.close();
		}
		return mCategoryDTO;

	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
