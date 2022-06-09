package kr.or.ddit.controller;

import java.sql.SQLException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.jsp.command.SearchCriteria;
import com.jsp.service.LoginSearchMemberService;

@Controller
@RequestMapping(value="/member")
public class MemberContoller {
	
	@Autowired
	private LoginSearchMemberService memberService;
	
	@RequestMapping("/main")
	public void main() {}
	
	@RequestMapping("/list")
	public ModelAndView list(SearchCriteria cri, ModelAndView mnv, HttpServletRequest request) throws SQLException{
		String url = "member/list";
		
		Map<String, Object> dataMap = null;
		
		try {
			dataMap = memberService.getMemberListForPage(cri);
		}catch(SQLException e) {
			throw e;
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		mnv.addObject("dataMap", dataMap);
		mnv.setViewName(url);
		
		return mnv;
	}
	
	@RequestMapping(value="/registForm", method=RequestMethod.GET)
	public String registForm() {
		String url = "member/regist";
		return url;
	}
}
