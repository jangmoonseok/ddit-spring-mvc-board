package kr.or.ddit.controller;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jsp.dto.MenuVO;
import com.jsp.exception.InvalidPasswordException;
import com.jsp.exception.NotFoundIdException;
import com.jsp.service.LoginSearchMemberService;
import com.jsp.service.MenuService;

@Controller
public class CommonController {

	@Autowired
	private MenuService menuService;
	@Autowired
	private LoginSearchMemberService memberService;
	
	@RequestMapping(value="/index")
	public ModelAndView indexPage(@RequestParam(defaultValue = "M000000") String mCode, ModelAndView mnv) 
					throws Exception{
		String url = "common/indexPage";
		
		List<MenuVO> mainMenuList = menuService.getMainMenuList();
		MenuVO menu = menuService.getMenuByMcode(mCode);
		
		mnv.addObject("mainMenuList", mainMenuList);
		mnv.addObject("menu", menu);
		mnv.setViewName(url);
		
		return mnv;
	}
	
	@RequestMapping(value="/main")
	public String main() throws Exception{
		String url = "home";
		return url;
	}
	
	@RequestMapping(value="/subMenu" ,method=RequestMethod.GET)
	@ResponseBody
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
	
	@RequestMapping(value="/common/loginForm", method=RequestMethod.GET)
	public String loginForm(@RequestParam(defaultValue = "")String error, HttpServletResponse response) 
			throws Exception{
		String url = "common/loginForm";
		
		if(error.equals("-1")) {
			response.setStatus(302);
		}
		
		return url;
	}
	
	@RequestMapping(value="/common/login", method=RequestMethod.POST)
	public String login(String id, String pwd, HttpSession session) throws Exception{
		String url = "redirect:/index.do";
		
		try {
			memberService.login(id, pwd);
			session.setAttribute("loginUser", memberService.getMember(id));
		}catch(NotFoundIdException | InvalidPasswordException e) {
			url = "common/login_fail";
		}catch(SQLException e) {
			e.printStackTrace();
			throw e;
		}
		
		return url;
	}
	
	@RequestMapping(value="/common/logout", method=RequestMethod.GET)
	public String logout(HttpSession session) {
		String url = "redirect:/";
		session.invalidate();
		return url;
	}
}
