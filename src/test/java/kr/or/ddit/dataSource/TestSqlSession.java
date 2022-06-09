package kr.or.ddit.dataSource;

import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.jsp.dto.MemberVO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:kr/or/ddit/context/dataSource-context.xml")
@Transactional
public class TestSqlSession {

	@Autowired
	private SqlSession session;
	
	@Test
	public void testGetMember() throws Exception{
		String id = "mimi";
		
		MemberVO member = session.selectOne("Member-Mapper.selectMemberById", id);
		
		Assert.assertNotNull(member);
	}
	
	@Test
	@Rollback
	public void testInsertMember() throws Exception{
		MemberVO member = new MemberVO();
		member.setId("aaaa");
		member.setPwd("aaaa");
		member.setEmail("aaaa");
		member.setPhone("0000-0000-0000");
		member.setPicture("noImage.jpg");
		member.setName("aaaa");
		member.setAuthority("ROLE_USER");
		
		session.update("Member-Mapper.insertMember", member);
		
		MemberVO result = session.selectOne("Member-Mapper.selectMemberById", member.getId());
		
		Assert.assertNotNull(result);
	}
}
