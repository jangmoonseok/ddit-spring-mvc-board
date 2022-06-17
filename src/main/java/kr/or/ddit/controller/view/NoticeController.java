package kr.or.ddit.controller.view;

import java.sql.SQLException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
	
	@RequestMapping("/main")
	public String main() throws Exception{
		String url ="notice/main";
		return url;
	}
	
	@RequestMapping("/list")
	public ModelAndView list(SearchCriteria cri, ModelAndView mnv) throws Exception{
		String url = "notice/list";
		
		Map<String, Object> dataMap = noticeService.getNoticeList(cri);
		
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
		MemberVO loginUser = memberService.getMember("mimi");
		
		model.addAttribute("loginUser", loginUser);
		return url;
	}
	
	@RequestMapping(value="/regist", method=RequestMethod.POST)
	public String regist(NoticeVO notice, RedirectAttributes rttr, HttpServletRequest request) throws Exception{
		String url = "redirect:/notice/list";
		
		notice.setTitle((String)request.getAttribute("XSStitle"));
		noticeService.regist(notice);

		rttr.addFlashAttribute("from", "regist");
		
		return url;
	}
	
	@RequestMapping(value="/detail", method=RequestMethod.GET)
	public ModelAndView detail(int nno, ModelAndView mnv, @RequestParam(defaultValue = "") String from) throws Exception{
		String url = "notice/detail";
		
		NoticeVO notice = null;
		
		if(!from.equals("list")) {
			notice = noticeService.getNoticeForModify(nno);
			
		}else {
			notice = noticeService.getNotice(nno);
			url = "redirect:/notice/detail.do?nno="+nno;
		}
		
		mnv.addObject("notice", notice);
		mnv.setViewName(url);
		return mnv;
	}
	
	@RequestMapping(value="/modifyForm")
	public ModelAndView modifyForm(int nno, ModelAndView mnv) throws Exception{
		String url = "notice/modify";
		
		NoticeVO notice = noticeService.getNoticeForModify(nno);
		
		mnv.addObject("notice", notice);
		mnv.setViewName(url);
		return mnv;
	}
	
	@RequestMapping(value="/modify", method=RequestMethod.POST)
	public String modify(NoticeVO notice, RedirectAttributes rttr, HttpServletRequest request) throws Exception{
		String url = "redirect:/notice/detail.do";
		
		//notice.setTitle(HTMLInputFilter.htmlSpecialChars(notice.getTitle())
		notice.setTitle((String)request.getAttribute("XSStitle"));
		
		noticeService.modify(notice);
		
		rttr.addAttribute("nno", notice.getNno());
		rttr.addFlashAttribute("from", "modify");
		
		return url;
	}
	
	@RequestMapping(value="/remove")
	public String remove(int nno, RedirectAttributes rttr) throws Exception{
		String url = "redirect:/notice/list.do";
		
		noticeService.remove(nno);
		
		rttr.addFlashAttribute("from", "remove");
		return url;
	}
}
