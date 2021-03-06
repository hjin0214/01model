package com.model2.mvc.view.product;

import java.net.URLDecoder;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.common.SearchVO;
import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.user.UserService;
import com.model2.mvc.service.user.impl.UserServiceImpl;


public class ListProductAction extends Action {

	@Override
	public String execute(	HttpServletRequest request,
												HttpServletResponse response) throws Exception {
		SearchVO searchVO=new SearchVO();
		
		int page=1;
		if(request.getParameter("page") != null)
			page=Integer.parseInt(request.getParameter("page"));
		
		searchVO.setPage(page);
		searchVO.setSearchCondition(request.getParameter("searchCondition"));
		searchVO.setSearchKeyword(request.getParameter("searchKeyword"));
		System.out.println(request.getParameter("searchCondition"));
		System.out.println("searchCondition 위 값");
		
		//searchVO.setSearchKeyword(URLDecoder.decode((String)request.getParameter("searchKeyword", "UTF-8")));
		
		//String searchKeyword = request.getParameter("searchKeyword");
		//searchKeyword = URLDecoder.decode(searchKeyword, "UTF-8");
		//System.out.println("써치키워드 : "+searchKeyword);
		
		System.out.println(request.getParameter("searchKeyword"));
		System.out.println("searchKeyword 위 값");
		
		String pageUnit=getServletContext().getInitParameter("pageSize");
		searchVO.setPageUnit(Integer.parseInt(pageUnit));
		
		ProductService service=new ProductServiceImpl();
		HashMap<String,Object> map=service.getProductList(searchVO);

		request.setAttribute("map", map);
		request.setAttribute("searchVO", searchVO);
		request.setAttribute("menu", request.getParameter("menu"));
		
		System.out.println("ListproductAction : "+searchVO);
		
		return "forward:/product/listProduct.jsp";
	}
}