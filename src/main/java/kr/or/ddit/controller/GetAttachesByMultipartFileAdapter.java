package kr.or.ddit.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.jsp.controller.MakeFileName;
import com.jsp.dto.AttachVO;

public class GetAttachesByMultipartFileAdapter {

	public static List<AttachVO> save(List<MultipartFile> multiFiles, String savePath) throws Exception{
		List<AttachVO> attachList = new ArrayList<AttachVO>();

		if(multiFiles != null) {

			for(MultipartFile multi : multiFiles) {
				String fileName = MakeFileName.toUUIDFileName(multi.getOriginalFilename(), "$$");
				
				File file = new File(savePath, fileName);
				
				multi.transferTo(file);

				AttachVO attach = new AttachVO();
				attach.setFileName(fileName);
				attach.setUploadPath(savePath);
				attach.setFileType(fileName.substring(fileName.lastIndexOf(".") + 1).toUpperCase());
				
				attachList.add(attach);
			}
			
		}
		
		return attachList;
	}
}
