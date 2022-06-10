package kr.or.ddit.controller;

import java.sql.SQLException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jsp.command.MemberRegistCommand;
import com.jsp.command.SearchCriteria;
import com.jsp.controller.MakeFileName;
import com.jsp.dto.MemberVO;
import com.jsp.service.LoginSearchMemberService;

import kr.or.ddit.command.MemberModifyCommand;

@Controller
@RequestMapping(value="/member")
public class MemberContoller{
	
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
	
	@RequestMapping(value="/regist", method=RequestMethod.POST)
	public String regist(MemberRegistCommand memberReq) throws Exception{
		String url = "member/regist_success";
		
		MemberVO member = memberReq.toMemberVO();
		memberService.regist(member);
		
		return url;
	}
	
	@RequestMapping(value="/detail", method=RequestMethod.GET)
	public String detail(String id, Model model)throws Exception{
		String url = "member/detail";
		
		MemberVO member = memberService.getMember(id);
		
		model.addAttribute("member",member);
		
		return url;
	}
	
	
	@RequestMapping(value="/modifyForm", method=RequestMethod.GET)
	public String modifyForm(String id, Model model) throws Exception{
		String url = "member/modify";
		
		MemberVO member = memberService.getMember(id);
		
		String picture = MakeFileName.parseFileNameFromUUID(member.getPicture(), "\\$\\$");
		member.setPicture(picture);
	
		model.addAttribute("member", member);
		
		return url;
	}
	
	@Autowired
	MemberRestController restController;
	
	@RequestMapping(value="/modify", method=RequestMethod.POST, produces = "text/plain;charset=utf-8")
	public String modify(MemberModifyCommand modifyReq, HttpSession session, RedirectAttributes rttr) throws Exception{
		String url = "redirect:/member/detail.do";
		
		MemberVO member = modifyReq.toMember();
		
		// 신규 파일 변경 및 기존 파일 삭제
		String oldPicture = memberService.getMember(member.getId()).getPicture();
		if(modifyReq.getUploadPicture() != null && !modifyReq.getUploadPicture().isEmpty()) {
			String fileName = restController.savePicture(oldPicture, modifyReq.getPicture());
			member.setPicture(fileName);
		}else {
			member.setPicture(oldPicture);
		}
		
		memberService.modify(member);
		
		return url;
	}
}











