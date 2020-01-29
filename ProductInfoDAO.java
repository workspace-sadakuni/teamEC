package com.internousdev.mocha.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.internousdev.mocha.dto.ProductInfoDTO;
import com.internousdev.mocha.util.DBConnector;

public class ProductInfoDAO {

		private DBConnector db = new DBConnector();
		private Connection con = db.getConnection();
		private ProductInfoDTO productInfoDTO = new ProductInfoDTO();

//		商品画像一覧から送られてきた商品IDを素にDBからその
//		商品の必要なデータを抽出しDTOに格納する。

		public ProductInfoDTO getProductInfo(int product_id)throws SQLException {
//			Actionから引数としてもらう。public データ型　変数名(引数)
		String sql = "SELECT * FROM product_info where product_id=?";
//			商品IDを参照しそのレコードをとってくるSQL文

		try{
			PreparedStatement preparedStatement = con.prepareStatement(sql);
//			preparedStatement変数にSQLを実行できるようにする文を入れる。
			preparedStatement.setInt(1, product_id);
//			preparedStatement変数にint型のproduct_idをsetする。
			ResultSet resultSet = preparedStatement.executeQuery();
//			resultSet変数に処理結果を入れる。データベースのレコードが入ってる。

			//			resultSet.next()でそのレコードのカラム名を探してくるため矢印を動かすためnextが必要。
			//			その後、DTO変数の箱に各々入れていくこと。

			while(resultSet.next()) {
				productInfoDTO.setProductId(resultSet.getInt("product_id"));
				productInfoDTO.setProductName(resultSet.getString("product_name"));
				productInfoDTO.setProductNameKana(resultSet.getString("product_name_kana"));
				productInfoDTO.setImageFilePath(resultSet.getString("image_file_path"));
				productInfoDTO.setImageFileName(resultSet.getString("image_file_name"));
				productInfoDTO.setProductDescription(resultSet.getString("product_description"));
				productInfoDTO.setPrice(resultSet.getInt("price"));
				productInfoDTO.setReleaseCompany(resultSet.getString("release_company"));
				productInfoDTO.setReleaseDate(resultSet.getDate("release_date"));
				productInfoDTO.setCategoryId(resultSet.getInt("category_id"));

//				productInfoDTOのpが大文字だとインスタンス化されたDTOの変数に合わないため
//				static参照できなくなる。

//			DBから商品IDに紐ずいた商品を写真と商品名を取り出すSQL文の記述。

			}
		}catch(Exception e){
			e.printStackTrace();
		} finally {
			con.close();
		}
		return productInfoDTO;
}

		public List<ProductInfoDTO> getProductRelation(int categoryId, int product_id, int limitOffset, int limitRowCount)throws SQLException {
			List<ProductInfoDTO> relatedProductList = new ArrayList<ProductInfoDTO>();
			Connection con = db.getConnection();
//			Actionから引数としてもらう。public データ型　変数名(引数)
//			上記のメソッドでとってきたレコードの中のカテゴリIDを参照にカテゴリの情報をとってくる。

		String sql1 = "select product_name, image_file_path, image_file_name,product_id from product_info where category_id=? and product_id not in(?) order by rand() limit ?,?";
//			商品IDで取得したレコードのIDカラムを引数としその表をとってくる。　ORDER BY RAND() LIMIT 3でランダムにレコードを３行とってくる。

		try{
			PreparedStatement preparedStatement = con.prepareStatement(sql1);
//			preparedStatement変数にSQLを実行できるようにする文を入れる。
			preparedStatement.setInt(1,categoryId);
			preparedStatement.setInt(2,product_id);
			preparedStatement.setInt(3, limitOffset);
			preparedStatement.setInt(4, limitRowCount);
//			preparedStatement変数にint型のidをsetする。
			ResultSet resultSet = preparedStatement.executeQuery();
//			resultSet変数に処理結果を入れる。データベースのレコードが入ってる。

//			resultSet.next()でそのレコードのカラム名を探してくるため矢印を動かすためnextが必要。
//			その後、DTO変数の箱に各々入れていく。

			while(resultSet.next()) {
				ProductInfoDTO piDTO = new ProductInfoDTO();
				piDTO.setProductNameRelation(resultSet.getString("product_name"));
				piDTO.setImageFilePathRelation(resultSet.getString("image_file_path"));
				piDTO.setImageFileNameRelation(resultSet.getString("image_file_name"));
				piDTO.setProductId(resultSet.getInt("product_id"));
				relatedProductList.add(piDTO);
			}
		}catch(Exception e){
			e.printStackTrace();
		} finally {
			con.close();
		}
		return relatedProductList;
}

//		下記メソッドで検索商品名から商品情報を取得
		public List<ProductInfoDTO> getProductInfo (String[] searchProductNameList) throws SQLException {
			List<ProductInfoDTO> productInfoDTOList = new ArrayList<ProductInfoDTO>();
			Connection con = db.getConnection();
//			String sql = "SELECT product_id,product_name,product_name_kana,image_file_path,price,category_id FROM product_info WHERE product_name LIKE CONCAT('%',?,'%') OR product_name_kana LIKE CONCAT('%',?,'%') ORDER BY product_id ASC";

			String sql = "SELECT product_id,product_name,product_name_kana,image_file_path,price,category_id FROM product_info";

			// Actionで処理しているため、要素として""(空文字)が存在する場合は、
			// 検索ワードを指定していないときのみなので、0番目が空文字の場合のみWhere句を追加しないようにする。
			if (!"".equals(searchProductNameList[0])) {

				// ここで拡張for文を使わない理由：indexを使用した方がわかりやすいため
				for (int i=0; i<searchProductNameList.length; i++) {
					if (i == 0) {
						sql += " where (product_name like '%" + searchProductNameList[i] + "%' or product_name_kana like '%" + searchProductNameList[i] + "%')";
					} else {
						sql += " or (product_name like '%" + searchProductNameList[i] + "%' or product_name_kana like '%" + searchProductNameList[i] + "%')";
					}
				}
			}
			sql += " order by product_id asc";

			try {
				PreparedStatement preparedStatement = con.prepareStatement(sql);
//				preparedStatement.setString(1,searchProductName);
//				preparedStatement.setString(2,searchProductName);

				ResultSet resultSet = preparedStatement.executeQuery();

				while(resultSet.next()) {
					ProductInfoDTO productInfoDTO = new ProductInfoDTO();
					productInfoDTO.setProductId(resultSet.getInt("product_id"));
					productInfoDTO.setProductName(resultSet.getString("product_name"));
					productInfoDTO.setProductNameKana(resultSet.getString("product_name_kana"));
					productInfoDTO.setImageFilePath(resultSet.getString("image_file_path"));
					productInfoDTO.setPrice(resultSet.getInt("price"));
					productInfoDTO.setCategoryId(resultSet.getInt("category_id"));
					productInfoDTOList.add(productInfoDTO);
				}
			} catch(Exception e) {
				e.printStackTrace();
			} finally {
				con.close();
			}
			return productInfoDTOList;
		}

		/**
		 * カテゴリーIDとキーワードを条件に商品情報を取得する
		 * @param categoryId カテゴリーID
		 * @param keywordsList キーワードの配列
		 * @return 商品情報のList
		 */

		public List<ProductInfoDTO> getProductInfoListByCategoryIdAndKeyword(String categoryId, String[] searchProductName) {
			DBConnector dbConnector = new DBConnector();
			Connection con = dbConnector.getConnection();
			List<ProductInfoDTO> productInfoDTOList = new ArrayList<ProductInfoDTO>();
			String sql = "select * from product_info where category_id=" + categoryId ;

			// Actionで処理しているため、要素として""(空文字)が存在する場合は、
			// 検索ワードを指定していないときのみなので、0番目が空文字の場合のみWhere句を追加しないようにする。
			if (!"".equals(searchProductName[0])) {

				// ここで拡張for文を使わない理由：indexを使用した方がわかりやすいため
				for (int i=0; i<searchProductName.length; i++) {
					if (i == 0) {
						sql += " and ((product_name like '%" + searchProductName[i] + "%' or product_name_kana like '%" + searchProductName[i] + "%')";
					} else {
						sql += " or (product_name like '%" + searchProductName[i] + "%' or product_name_kana like '%" + searchProductName[i] + "%')";
					}
				}
				sql += ")";
			}
			sql += " order by product_id asc";

			try {
				PreparedStatement preparedStatement = con.prepareStatement(sql);
				ResultSet resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					ProductInfoDTO productInfoDTO = new ProductInfoDTO();
					productInfoDTO.setId(resultSet.getInt("id"));
					productInfoDTO.setProductId(resultSet.getInt("product_id"));
					productInfoDTO.setProductName(resultSet.getString("product_name"));
					productInfoDTO.setProductNameKana(resultSet.getString("product_name_kana"));
					productInfoDTO.setProductDescription(resultSet.getString("product_description"));
					productInfoDTO.setCategoryId(resultSet.getInt("category_id"));
					productInfoDTO.setPrice(resultSet.getInt("price"));
					productInfoDTO.setImageFilePath(resultSet.getString("image_file_path"));
					productInfoDTO.setImageFileName(resultSet.getString("image_file_name"));
					productInfoDTO.setReleaseDate(resultSet.getDate("release_date"));
					productInfoDTO.setReleaseCompany(resultSet.getString("release_company"));
					productInfoDTOList.add(productInfoDTO);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}finally {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return productInfoDTOList;
		}

	}