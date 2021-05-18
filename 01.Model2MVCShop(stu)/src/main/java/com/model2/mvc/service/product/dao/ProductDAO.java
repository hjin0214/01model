package com.model2.mvc.service.product.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import com.model2.mvc.common.SearchVO;
import com.model2.mvc.common.util.DBUtil;
import com.model2.mvc.service.product.vo.ProductVO;
import com.model2.mvc.service.user.vo.UserVO;


public class ProductDAO {
	
	public ProductDAO(){
	}

	public void insertProduct(ProductVO productVO) throws Exception {
		
		Connection con = DBUtil.getConnection();

		String sql = "INSERT INTO product VALUES (seq_product_prod_no.nextval, ?, ?, ?, ?, ?, SYSDATE)";
		 System.out.println(productVO);
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(5, productVO.getFileName());
	
		stmt.setString(3, productVO.getManuDate());
		stmt.setInt(4, productVO.getPrice());
		stmt.setString(2, productVO.getProdDetail());
//		System.out.println("name 위");
		stmt.setString(1, productVO.getProdName());
		
//		System.out.println(productVO.getProdName());
//		stmt.setInt(6, productVO.getProdNo());
//		stmt.setString(8, productVO.getProTranCode());
		
		int a = stmt.executeUpdate();
		System.out.println("쿼리실행결과:"+a);
		
		con.close();
	}

	public ProductVO findProduct(int prodNo) throws Exception {
		
		Connection con = DBUtil.getConnection();

		String sql = "SELECT * FROM product WHERE prod_No=?";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setInt(1, prodNo);

		ResultSet rs = stmt.executeQuery();

		ProductVO productVO = null;
		while (rs.next()) {
			productVO = new ProductVO();
			productVO.setProdName(rs.getString("prod_name"));
			productVO.setFileName(rs.getString("IMAGE_FILE"));
			productVO.setManuDate(rs.getString("MANUFACTURE_DAY"));
			productVO.setPrice(rs.getInt("price"));
			productVO.setProdDetail(rs.getString("prod_Detail"));
			productVO.setProdNo(rs.getInt("prod_No"));
			productVO.setRegDate(rs.getDate("reg_Date"));
//			productVO.setProTranCode(rs.getString("proTranCode"));
		}
		
		con.close();

		return productVO;
	}

	public HashMap<String,Object> getProductList(SearchVO searchVO) throws Exception {
		
		Connection con = DBUtil.getConnection();
		
		String sql = "SELECT p.PROD_NO, p.prod_name, p.price, p.MANUFACTURE_DAY, t.TRAN_STATUS_CODE from product p, transaction t where p.prod_no=t.prod_no(+) ";
		if (searchVO.getSearchCondition() != null) {
			if (searchVO.getSearchCondition().equals("0")) {
				sql += "and p.PROD_NO LIKE '%" + searchVO.getSearchKeyword()+"%'";
			} else if (searchVO.getSearchCondition().equals("1")) {
				sql += "and p.PROD_NAME LIKE '%" + searchVO.getSearchKeyword()+"%'";
				System.out.println("불러오는 키워드 : "+searchVO.getSearchKeyword());
			}else if  (searchVO.getSearchCondition().equals("2")) {
				sql += "and p.PRICE LIKE '%" + searchVO.getSearchKeyword()+"%'";
			}
		}
		sql += " ORDER BY p.PROD_NO";

		PreparedStatement stmt = 
			con.prepareStatement(	sql,
														ResultSet.TYPE_SCROLL_INSENSITIVE,
														ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = stmt.executeQuery();

		rs.last();
		int total = rs.getRow();
		System.out.println("로우의 수:" + total);

		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("count", new Integer(total));

		rs.absolute(searchVO.getPage() * searchVO.getPageUnit() - searchVO.getPageUnit()+1);
		System.out.println("searchVO.getPage():" + searchVO.getPage());
		System.out.println("searchVO.getPageUnit():" + searchVO.getPageUnit());

		ArrayList<ProductVO> list = new ArrayList<ProductVO>();
		if (total > 0) {
			for (int i = 0; i < searchVO.getPageUnit(); i++) {
				ProductVO vo = new ProductVO();
				
				vo.setProdName(rs.getString("prod_Name"));
				//vo.setFileName(rs.getString("IMAGE_FILE"));
				vo.setManuDate(rs.getString("MANUFACTURE_DAY"));
				vo.setPrice(rs.getInt("price"));
				//vo.setProdDetail(rs.getString("prod_Detail"));
				vo.setProdNo(rs.getInt("prod_No"));
				//vo.setRegDate(rs.getDate("reg_Date"));
				vo.setProTranCode(rs.getString("TRAN_STATUS_CODE"));

				list.add(vo);
				if (!rs.next())
					break;
			}
		}
		System.out.println("list.size() : "+ list.size());
		map.put("list", list);
		System.out.println("map().size() : "+ map.size());

		con.close();
			
		return map;
	}

	public void updateProduct(ProductVO productVO) throws Exception {
		
		Connection con = DBUtil.getConnection();

		String sql = "UPDATE product SET prod_Name=?, PROD_DETAIL=?, MANUFACTURE_DAY=?, price=? WHERE prod_No=?";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		
		stmt.setString(1, productVO.getProdName());
		stmt.setString(2, productVO.getProdDetail());
		stmt.setString(3, productVO.getManuDate());
		stmt.setInt(4, productVO.getPrice());
		stmt.setInt(5, productVO.getProdNo());
		
		stmt.executeUpdate();
		System.out.println(stmt);
				
		con.close();
	}
}