package kr.or.ddit.controller.view;

import java.sql.SQLException;
import java.util.List;
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
import com.jsp.dto.BoardVO;
import com.jsp.dto.MemberVO;
import com.jsp.service.BoardService;

@Controller
@RequestMapping("/board")
public class BoardController {

	@Autowired
	private BoardService boardService;
	
	@RequestMapping("/main")
	public String main() {
		String url = "board/main";
		return url;
	}
	
	@RequestMapping("/list")
	public String list(SearchCriteria cri, Model model) throws Exception{
		String url = "board/list";
		
		Map<String, Object> dataMap = null;
	
		dataMap = boardService.getBoardList(cri);
		model.addAttribute("dataMap", dataMap);

		
		return url;
	}
	
	@RequestMapping("/registForm")
	public String registForm(HttpSession session, Model model) throws Exception{
		String url = "board/regist";
		
		MemberVO loginUser = (MemberVO)session.getAttribute("loginUser");
		
		model.addAttribute("loginUser", loginUser);
		return url;
	}
	
	@RequestMapping(value = "/regist", method=RequestMethod.POST)
	public String regist(BoardVO board, RedirectAttributes rttr, HttpServletRequest request) throws Exception {
		String url = "redirect:/board/list";
		
		board.setTitle((String)request.getAttribute("XSStitle"));
		boardService.regist(board);
		if(true) throw new SQLException();
		rttr.addFlashAttribute("from", "regist");
		return url;
	}
	
	@RequestMapping("/detail")
	public String detail(int bno, Model model, @RequestParam(defaultValue = "") String from) throws Exception{
		String url = "board/detail";
		
		BoardVO board = null;
		
		if(from.equals("list")) {
			board = boardService.getBoard(bno);
			url = "redirect:/board/detail.do?bno=" + bno;
		}else {
			board = boardService.getBoardForModify(bno);
		}
		
		model.addAttribute("board", board);
		
		return url;
	}
	
	@RequestMapping("/modifyForm")
	public String modifyForm(int bno, Model model) throws Exception{
		String url = "board/modify";
		
		BoardVO board = boardService.getBoardForModify(bno);
		
		model.addAttribute("board", board);
		return url;
	}
	
	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public String modify(BoardVO board, RedirectAttributes rttr, HttpServletRequest request) throws Exception{
		String url = "redirect:/board/detail.do";
		
		board.setTitle((String)request.getAttribute("XSStitle"));
		boardService.modify(board);
		
		rttr.addFlashAttribute("from", "modify");
		rttr.addAttribute("bno", board.getBno());
		
		return url;
	}
	
	@RequestMapping(value = "/remove")
	public String remove(int bno, RedirectAttributes rttr) throws Exception{
		String url = "redirect:/board/detail.do";
		
		boardService.remove(bno);
	
		rttr.addAttribute("bno", bno);
		rttr.addFlashAttribute("from", "remove");
		return url;
	}
}
