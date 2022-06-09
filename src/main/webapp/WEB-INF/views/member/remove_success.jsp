<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<script>
	alert("삭제에 성공했습니다.\n 회원목록 페이지로 이동합니다.");
	
	if(window.opener) window.opener.location.reload(true);
	window.close();
</script>