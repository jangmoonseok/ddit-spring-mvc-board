package kr.or.ddit.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jsp.command.SearchCriteria;
import com.jsp.controller.FileDownloadResolver;
import com.jsp.dto.AttachVO;
import com.jsp.dto.PdsVO;
import com.jsp.service.PdsService;

import kr.or.ddit.command.PdsModifyCommand;
import kr.or.ddit.command.PdsRegistCommand;

@Controller
@RequestMapping("/pds")
public class PdsController {
	
	@Autowired
	private PdsService pdsService;
	
	@RequestMapping("/main")
	public String main() throws Exception{
		String url = "pds/main";
		return url;
	}
	
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
	
	@Resource(name = "fileUploadPath")
	private String uploadPath;
	
	@RequestMapping(value = "/regist", method=RequestMethod.POST, produces = "text/plain; charset=utf-8")
	public String regist(PdsRegistCommand pdsCMD, RedirectAttributes rttr, HttpServletRequest request) throws Exception{
		String url = "redirect:/pds/list.do";
		
		List<AttachVO> attachList = GetAttachesByMultipartFileAdapter.save(pdsCMD.getUploadFile(), uploadPath);

		PdsVO pds = pdsCMD.toPdsVO();
		pds.setTitle((String)request.getAttribute("XSStitle"));
		pds.setAttachList(attachList);
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
		
		//파일명 재정의
		if(pds != null) {
			List<AttachVO> attachList = pds.getAttachList();
			if(attachList != null) {
				for(AttachVO attach : attachList) {
					String fileName = attach.getFileName().split("\\$\\$")[1];
					attach.setFileName(fileName);
				}
			}
		}
		model.addAttribute("pds", pds);
		
		return url;
	}
	
	@RequestMapping(value = "/modifyForm")
	public String modifyForm(int pno, Model model) throws Exception{
		String url = "pds/modify";
		
		PdsVO pds = pdsService.getPds(pno);
		
		List<AttachVO> attachList = pds.getAttachList();
		if(attachList != null) {
			for(AttachVO attach : attachList) {
				String fileName = attach.getFileName().split("\\$\\$")[1];
				attach.setFileName(fileName);
			}
		}
		
		model.addAttribute("pds", pds);
		
		return url;
	}
	
	
	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public String modify(PdsModifyCommand pdsCMD, RedirectAttributes rttr, HttpServletRequest request) throws Exception{
		String url = "redirect:/pds/detail.do";
		
		//삭제
		if(pdsCMD.getDeleteFile() != null && pdsCMD.getDeleteFile().length > 0) {
			for(String anoStr : pdsCMD.getDeleteFile()) {
				int ano = Integer.parseInt(anoStr);
				
				AttachVO attach = pdsService.getAttachByAno(ano);
				File target = new File(uploadPath, attach.getFileName());
				if(target.exists()) {
					target.delete();
				}
				
				pdsService.removeAttachByAno(ano);
			}
		}
		
		//등록
		List<MultipartFile> multiFiles = pdsCMD.getUploadFile();
		List<AttachVO> attachList = GetAttachesByMultipartFileAdapter.save(multiFiles, uploadPath);
		
		PdsVO pds = pdsCMD.toPdsVO();
		pds.setTitle((String)request.getAttribute("XSStitle"));
		pds.setAttachList(attachList);
		
		pdsService.modify(pds);
		
		rttr.addAttribute("pno", pds.getPno());
		rttr.addFlashAttribute("from", "modify");
		
		return url;
	}
	
	@RequestMapping("/remove")
	public String remove(int pno, RedirectAttributes rttr) throws Exception{
		String url = "redirect:/pds/detail.do";
			
		PdsVO pds = pdsService.getPds(pno);
		List<AttachVO> attachList = pds.getAttachList();
		if(attachList != null) {
			for(AttachVO attach : attachList) {
				File target = new File(uploadPath, attach.getFileName());
				
				if(target.exists()) {
					target.delete();
				}
			}
		}
		
		pdsService.remove(pno);
		
		rttr.addAttribute("pno", pno);
		rttr.addFlashAttribute("from", "remove");
		
		return url;
	}
	
	@RequestMapping("/getFile")
	public String getFile(int ano, Model model) throws Exception{
		String url = "downloadFile";
		
		AttachVO attach = pdsService.getAttachByAno(ano);
		
		model.addAttribute("savedPath", attach.getUploadPath());
		model.addAttribute("fileName", attach.getFileName());
		
		return url;
	}
}
