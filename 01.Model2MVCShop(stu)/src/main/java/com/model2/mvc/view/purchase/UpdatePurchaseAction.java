package com.model2.mvc.view.purchase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;
import com.model2.mvc.service.purchase.vo.PurchaseVO;


public class UpdatePurchaseAction extends Action {

	@Override
	public String execute(	HttpServletRequest request,
												HttpServletResponse response) throws Exception {
		
		int tranNo=Integer.parseInt(request.getParameter("tranNo"));
		System.out.println("updateProductAction의 tranNo : "+tranNo);
		
		PurchaseVO purchaseVO =new PurchaseVO();
		purchaseVO.setTranNo(tranNo);
		purchaseVO.setPaymentOption(request.getParameter("paymentOption"));
		purchaseVO.setReceiverName(request.getParameter("receiverName"));
		purchaseVO.setReceiverPhone(request.getParameter("receiverPhone"));
		purchaseVO.setDivyAddr(request.getParameter("receiverAddr"));
		purchaseVO.setDivyRequest(request.getParameter("receiverRequest"));
		purchaseVO.setDivyDate(request.getParameter("divyDate"));
		System.out.println("업데이트펄체이스액션 배송날짜 : "+purchaseVO.getDivyDate());
				
		PurchaseService service=new PurchaseServiceImpl();
		service.updatePurchase(purchaseVO);
		request.setAttribute("purchaseVO", purchaseVO);
		
		//HttpSession session=request.getSession();
		//int sessionId=((ProductVO)session.getAttribute("prodNo")).getProdNo();
	
		//if(sessionId==(prodNo)){
		//	session.setAttribute("prodNo", productVO);
		//	System.out.println("update product action : "+productVO);
		//}
		
		return "forward:/getPurchase.do?prodNo="+tranNo;
	}
}