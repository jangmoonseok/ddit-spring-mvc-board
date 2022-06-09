package kr.or.ddit.controller;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jsp.service.LoginSearchMemberService;

@RestController
@RequestMapping(value="/member")
public class MemberRestController {

	@Autowired
	private LoginSearchMemberService memberService;
	
	@Resource(name="picturePath")
	private String picturePath;
	
	@RequestMapping(value="/picture", method=RequestMethod.POST, produces="text/plain;charset=utf-8")
	public ResponseEntity<String> picture(@RequestParam("pictureFile") MultipartFile multi, String oldPicture)
											throws Exception{
		ResponseEntity<String> entity = null;
		String result = "";
		HttpStatus status = null;
		
		
		entity = new ResponseEntity<String>(result, status);
		return entity;
	}
}
