package kr.or.ddit.controller;

import java.sql.SQLException;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jsp.command.SearchCriteria;
import com.jsp.dto.MemberVO;
import com.jsp.dto.NoticeVO;
import com.jsp.service.LoginSearchMemberService;
import com.jsp.service.NoticeService;

@Controller
@RequestMapping("/notice")
public class NoticeController {

	@Autowired
	private NoticeService noticeService;
	
	@RequestMapping("/list")
	public ModelAndView list(SearchCriteria cri, ModelAndView mnv) throws Exception{
		String url = "notice/list";
		
		Map<String, Object> dataMap = null;
		try {			
			dataMap = noticeService.getNoticeList(cri);
		}catch(SQLException e) {
			e.printStackTrace();
			throw e;
		}
		
		mnv.addObject("dataMap", dataMap);
		mnv.setViewName(url);
		return mnv;
	}
	
	@Autowired
	private LoginSearchMemberService memberService;
	
	@RequestMapping("/registForm")
	public String registForm(Model model, HttpSession session) throws Exception{
		String url = "notice/regist";
		
		//MemberVO loginUser = (MemberVO)session.getAttribute("loginUser");
		MemberVO loginUser = null;
		try {
			loginUser = memberService.getMember("mimi");
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		session.setAttribute("loginUser", loginUser);
		model.addAttribute("loginUser", loginUser);
		return url;
	}
	
	@RequestMapping(value="/regist", method=RequestMethod.POST)
	public String regist(NoticeVO notice, RedirectAttributes rttr) throws Exception{
		String url = "redirect:/notice/registForm.do";
		
		try {
			noticeService.regist(notice);
			rttr.addFlashAttribute("status", "success");
		}catch(SQLException e) {
			e.printStackTrace();
			rttr.addFlashAttribute("status", "fail");
		}
		rttr.addFlashAttribute("from", "regist");
		
		return url;
	}
	
	@RequestMapping(value="/detail", method=RequestMethod.GET)
	public ModelAndView detail(int nno, ModelAndView mnv) throws Exception{
		String url = "notice/detail";
		
		NoticeVO notice = null;
		
		try {
			notice = noticeService.getNotice(nno);
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		
		mnv.addObject("notice", notice);
		mnv.setViewName(url);
		return mnv;
	}
	
	@RequestMapping(value="/modifyForm")
	public ModelAndView modifyForm(int nno, ModelAndView mnv) throws Exception{
		String url = "notice/modify";
		
		NoticeVO notice = null;
		
		try {
			notice = noticeService.getNoticeForModify(nno);
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		
		mnv.addObject("notice", notice);
		mnv.setViewName(url);
		return mnv;
	}
	
	@RequestMapping(value="/modify", method=RequestMethod.POST)
	public String modify(NoticeVO notice, RedirectAttributes rttr) throws Exception{
		String url = "redirect:/notice/detail.do";
		
		try {
			noticeService.modify(notice);
			rttr.addAttribute("nno", notice.getNno());
			rttr.addFlashAttribute("status", "success");
		}catch(Exception e) {
			e.printStackTrace();
			rttr.addFlashAttribute("status", "fail");
			throw e;
		}
		
		rttr.addFlashAttribute("from", "modify");
		
		return url;
	}
	
	@RequestMapping(value="/remove")
	public String remove(int nno, RedirectAttributes rttr) throws Exception{
		String url = "redirect:/notice/detail.do";
		
		try {
			
		}catch(Exception e) {
			e.printStackTrace();
			
		}
		return url;
	}
}
