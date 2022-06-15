package kr.or.ddit.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jsp.command.SearchCriteria;
import com.jsp.dto.PdsVO;
import com.jsp.service.PdsService;

@Controller
@RequestMapping("/pds")
public class PdsController {
	
	@Autowired
	private PdsService pdsService;
	
	@RequestMapping("/list")
	public String list(SearchCriteria cri, Model model) throws Exception{
		String url = "pds/list";
		
		Map<String, Object> dataMap = pdsService.getList(cri);
		
		model.addAttribute("dataMap", dataMap);
		return url;
	}
	
	@RequestMapping("/registForm")
	public String registForm() throws Exception{
		String url = "pds/regist";
		
		return url;
	}
	
	@RequestMapping(value = "/regist", method=RequestMethod.POST)
	public String regist(PdsVO pds, RedirectAttributes rttr, HttpServletRequest request) throws Exception{
		String url = "redirect:/pds/list";
		
		pds.setTitle((String)request.getAttribute("XSStitle"));
		
		pdsService.regist(pds);
		
		rttr.addFlashAttribute("from", "regist");
		
		return url;
	}
	
	@RequestMapping(value = "/detail")
	public String detail(int pno, Model model, @RequestParam(defaultValue = "") String from)throws Exception{
		String url = "pds/detail";
		
		PdsVO pds = null;
		if(!from.equals("list")) {
			pds = pdsService.getPds(pno);
		}else {
			pds = pdsService.read(pno);
			url = "redirect:/pds/detail.do?pno="+pno;
		}
		
		model.addAttribute("pds", pds);
		
		return url;
	}
	
	@RequestMapping(value = "/modifyForm")
	public String modifyForm(int pno, Model model) throws Exception{
		String url = "pds/modify";
		
		PdsVO pds = pdsService.getPds(pno);
		
		model.addAttribute("pds", pds);
		
		return url;
	}
	
	@RequestMapping(value = "modify", method = RequestMethod.POST)
	public String modify(int pno ) throws Exception{
		return null;
	}
}