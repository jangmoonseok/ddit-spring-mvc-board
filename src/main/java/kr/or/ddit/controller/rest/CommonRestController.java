package kr.or.ddit.controller.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.jsp.dto.MenuVO;
import com.jsp.service.MenuService;

@RestController
public class CommonRestController {

	@Autowired
	private MenuService menuService;
	
	@RequestMapping(value="/subMenu" ,method=RequestMethod.GET)
	public ResponseEntity<List<MenuVO>> subMenu(@RequestParam(defaultValue = "M000000") String mCode) throws Exception {
		ResponseEntity<List<MenuVO>> entity = null;
		
		List<MenuVO> subMenuList = null;
		
		try {
			subMenuList = menuService.getSubMenuList(mCode);
			entity = new ResponseEntity<List<MenuVO>>(subMenuList, HttpStatus.OK);
		}catch(Exception e) {
			entity = new ResponseEntity<List<MenuVO>>(HttpStatus.INTERNAL_SERVER_ERROR);
			e.printStackTrace();
		}
		
		return entity;
	}
}
