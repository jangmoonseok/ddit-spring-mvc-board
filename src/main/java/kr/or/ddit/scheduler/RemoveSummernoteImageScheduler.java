package kr.or.ddit.scheduler;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.or.ddit.dao.BoardDAOBean;
import kr.or.ddit.dao.NoticeDAOBean;
import kr.or.ddit.dao.PdsDAOBean;

public class RemoveSummernoteImageScheduler {

	private NoticeDAOBean noticeDAO;
	private BoardDAOBean boardDAO;
	private PdsDAOBean pdsDAO;
	private String filePath;
	
	public void setNoticeDAO(NoticeDAOBean noticeDAO) {
		this.noticeDAO = noticeDAO;
	}
	public void setBoardDAO(BoardDAOBean boardDAO) {
		this.boardDAO = boardDAO;
	}
	public void setPdsDAO(PdsDAOBean pdsDAO) {
		this.pdsDAO = pdsDAO;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	private static final Logger logger = LoggerFactory.getLogger(RemoveSummernoteImageScheduler.class);
	
	public void fileRemove() throws Exception{
		
		File dir = new File(filePath);
		File[] files = dir.listFiles();
		
		if(files != null) {
			for(File file : files) {
				boolean existFile = false;
				existFile = (noticeDAO.selectNoticeByImage(file.getName()) != null)
						|| (boardDAO.selectBoardByImage(file.getName()) != null)
						|| (pdsDAO.selectPdsByImage(file.getName()) != null);
				if(existFile) {
					logger.info(file.getName() + "은 사용하는 파일입니다.");
				}else {
					logger.info(file.getName() + "은 사용하지 않는 파일입니다.");
					if(file.exists()) file.delete();
				}
			}
		}
		
	}
}
