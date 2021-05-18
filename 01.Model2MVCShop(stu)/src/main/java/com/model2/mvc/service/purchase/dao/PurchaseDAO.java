package com.model2.mvc.service.purchase.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import com.model2.mvc.common.SearchVO;
import com.model2.mvc.common.util.DBUtil;
import com.model2.mvc.service.product.vo.ProductVO;
import com.model2.mvc.service.purchase.vo.PurchaseVO;
import com.model2.mvc.service.user.vo.UserVO;

public class PurchaseDAO {
	
	public PurchaseDAO(){
	}

	public void insertPurchase(PurchaseVO purchaseVO) throws Exception {
		
		Connection con = DBUtil.getConnection();
		String sql = "INSERT INTO transaction VALUES (seq_transaction_tran_no.nextval, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE,? )";
		 System.out.println(purchaseVO);
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setInt(1, purchaseVO.getPurchaseProd().getProdNo());
		stmt.setString(2, purchaseVO.getBuyer().getUserId());		
		stmt.setString(3, purchaseVO.getPaymentOption());
		stmt.setString(4, purchaseVO.getReceiverName());
		stmt.setString(5, purchaseVO.getReceiverPhone());
		stmt.setString(6, purchaseVO.getDivyAddr());
		System.out.println(" dao 에서의 주소 나와라 :"+ purchaseVO.getDivyAddr());
//		System.out.println("name 위");
		stmt.setString(7, purchaseVO.getDivyRequest());
		stmt.setString(8, "1"); //트랜코드
		stmt.setString(9, purchaseVO.getDivyDate());
//		System.out.println(productVO.getProdName());
//		stmt.setInt(6, productVO.getProdNo());
//		stmt.setString(8, productVO.getProTranCode());
		
		int a = stmt.executeUpdate();
		System.out.println("쿼리실행결과:"+a);
		
		con.close();
	}

	public PurchaseVO findPurchase(int tranNo) throws Exception {
		
		Connection con = DBUtil.getConnection();

		String sql = "SELECT * FROM transaction WHERE tran_No=?";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setInt(1, tranNo);
		System.out.println("dao에서의 트랜넘버는 잘 받니? : "+ tranNo);

		ResultSet rs = stmt.executeQuery();

		PurchaseVO purchaseVO = null;
		ProductVO productVO=null;
		UserVO userVO=null;
		
		while (rs.next()) {
			purchaseVO = new PurchaseVO();
			productVO = new ProductVO();
			userVO = new UserVO();
			
			purchaseVO.setTranNo(rs.getInt("tran_no"));
			System.out.println("dao에서의 prod_no (넣기전) : "+rs.getInt("prod_no"));
			//int prodNo = rs.getInt("prod_no");
			productVO.setProdNo(rs.getInt("prod_no"));
			System.out.println("dao에서의 prod_no : "+rs.getInt("prod_no"));
			purchaseVO.setPurchaseProd(productVO);
			userVO.setUserId(rs.getString("buyer_id"));
			purchaseVO.setBuyer(userVO);
			purchaseVO.setPaymentOption(rs.getString("payment_option"));
			purchaseVO.setReceiverName(rs.getString("receiver_name"));
			purchaseVO.setReceiverPhone(rs.getString("receiver_phone"));
			purchaseVO.setDivyAddr(rs.getString(7));
			purchaseVO.setDivyRequest(rs.getString("dlvy_request"));
			purchaseVO.setDivyDate(rs.getString("dlvy_date"));
			purchaseVO.setOrderDate(rs.getDate("order_data"));
//			productVO.setProTranCode(rs.getString("proTranCode"));
		}
		
		System.out.println("dao에서의 vo :"+rs);
		con.close();

		return purchaseVO;
	}

	public HashMap<String,Object> getPurchaseList(SearchVO searchVO, String buyerId) throws Exception {
		
		ProductVO productVO=new ProductVO();
		UserVO userVO=new UserVO();
		userVO.setUserId(buyerId);
		System.out.println("입력받은 아이디 : "+buyerId);
		System.out.println("입력받은 아이디2 : "+userVO.getUserId());
		
		Connection con = DBUtil.getConnection();
		
		String sql = "SELECT * FROM transaction WHERE buyer_id IN (?)";

		PreparedStatement stmt = 
			con.prepareStatement(	sql,
														ResultSet.TYPE_SCROLL_INSENSITIVE,
														ResultSet.CONCUR_UPDATABLE);
		stmt.setString(1, buyerId);
		ResultSet rs = stmt.executeQuery();

		rs.last();
		int total = rs.getRow();
		System.out.println("로우의 수:" + total);

		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("count", new Integer(total));

		rs.absolute(searchVO.getPage() * searchVO.getPageUnit() - searchVO.getPageUnit()+1);
		System.out.println("searchVO.getPage():" + searchVO.getPage());
		System.out.println("searchVO.getPageUnit():" + searchVO.getPageUnit());

		ArrayList<PurchaseVO> list = new ArrayList<PurchaseVO>();
		if (total > 0) {
			for (int i = 0; i < searchVO.getPageUnit(); i++) {
				PurchaseVO vo = new PurchaseVO();
								
				vo.setTranNo(rs.getInt("TRAN_NO"));
//				productVO.setProdNo(rs.getInt("prod_no"));
//				vo.setPurchaseProd(productVO);
				userVO.setUserId(rs.getString("buyer_id"));
				vo.setBuyer(userVO);
//				vo.setPaymentOption(rs.getString("payment_option"));
				vo.setReceiverName(rs.getString("receiver_name"));
				vo.setReceiverPhone(rs.getString("receiver_phone"));
//				vo.setDivyAddr(rs.getString("demailaddr"));
//				vo.setDivyRequest(rs.getString("dlvy_request"));
//				vo.setDivyDate(rs.getString("dlvy_date"));
//				vo.setOrderDate(rs.getDate("order_date"));
				//vo.setProTranCode(rs.getString("proTranCode"));

				list.add(vo);
				System.out.println(list);
				
				if (!rs.next())
					break;
			}
		}
		System.out.println("list.size() : "+ list.size());
		map.put("list", list);
		System.out.println("map().size() : "+ map.size());

		con.close();
		stmt.close();
		rs.close();
		return map;
	}

	public void updatePurchase(PurchaseVO purchaseVO) throws Exception {
		
		Connection con = DBUtil.getConnection();

		String sql = "UPDATE transaction SET payment_option=?, receiver_name=?, receiver_phone=?, DEMAILADDR=?, dlvy_request=? , dlvy_date=? WHERE tran_No=?";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		
		System.out.println("쿼리 날리기 시작");
		stmt.setString(1, purchaseVO.getPaymentOption());
		stmt.setString(2, purchaseVO.getReceiverName());
		System.out.println("dao의 업데이트의 네임 : "+purchaseVO.getReceiverName());
		stmt.setString(3, purchaseVO.getReceiverPhone());
		stmt.setString(4, purchaseVO.getDivyAddr());
		stmt.setString(5, purchaseVO.getDivyRequest());
		stmt.setString(6, purchaseVO.getDivyDate());
		System.out.println("dao의 배송날짜 : "+purchaseVO.getDivyDate());
		stmt.setInt(7, purchaseVO.getTranNo());
		System.out.println("dao의 업데이트의 트랜넘버 : "+purchaseVO.getTranNo());
		
		stmt.executeUpdate();
		System.out.println(stmt);
				
		con.close();
	}
}