package com.springnote.notetaker;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rath.toys.springnote.Page;
import rath.toys.springnote.PageMeta;
import rath.toys.springnote.SpringNote;

public class NoteTaker {
	
	/** Application ID */
	public static final String APPLICATION_ID = "62";
	
	/** Application Key */
	public static final String APPLICATION_KEY = "82cf7434fae3fff7034887bd719f5b248c8677c8";
	
	/**
	 * 스프링노트 정보 객체
	 */
	private SpringNote springnote = null;
	
	/**
	 * 노트 적기에 사용할 페이지
	 */
	private Page page = null;
	
	public NoteTaker(String openId, String domainName, String userKey) {
		try {
			this.springnote = new SpringNote();
			this.springnote.setOpenID(new URL(openId));
			this.springnote.setUserKey(userKey);
			this.springnote.setUsername(domainName);
			this.springnote.setApplicationId(APPLICATION_ID);
			this.springnote.setApplicationKey(APPLICATION_KEY);
		} catch (Exception ex) {
			this.springnote = null;
			throw new NoteTakerException("Springnote 정보 설정중 오류 발생.", ex);
		}
	}
	
	public void setSpringNote(final SpringNote springnote) {
		this.springnote = springnote;
	}
	
	/**
	 * 페이지 목록을 반환한다.
	 * @return
	 */
	public List<PageMeta> fetchPageList() {
		List<PageMeta> list = null;
		try {
			list = springnote.getPages();
		} catch (Exception e) {
			throw new NoteTakerException("페이지 목록을 가져오는 도중 오류가 발생하였습니다.", e);
		}
		
		return list;
	}
	
	/**
	 * 노트 적기에 사용할 페이지를 지정한다.
	 * @param pageId 페이지 ID
	 */
	public void setPage(final int pageId) {
		Page page = null;
		try {
			page = springnote.getPage(pageId);
		} catch (Exception ex) {
			throw new NoteTakerException(pageId + " 페이지를 가져오는데 실패하였습니다.");
		}
		
		this.page = page;
	}
	
	/**
	 * 노트 적기에 사용할 페이지를 가져온다.
	 * @return
	 */
	public Page getPage() {
		return this.page;
	}

	/**
	 * 노트에 내용을 적는다.
	 * @param note 내용
	 */
	public void addMessageToNote(String note) {
		if (this.page == null) {
			throw new NoteTakerException("메시지를 추가할 페이지를 지정하지 않았습니다.");
		}
		
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초");
		String dateStr = sdFormat.format(new Date());
		
		note = note.replaceAll("<", "&amp;lt;"); 
		note = note.replaceAll(">", "&amp;gt;"); 
	

		// "스프링노트":http://www.springnote.com 형태의 주소를 링크로 변경한다.
		Pattern pattern = Pattern.compile("\"([\\S ]+)\":(http?://\\S+)(\\s*)");
		Matcher matcher = pattern.matcher(note);
		note = matcher.replaceAll("<a href=\"$2\">$1</a>$3");
		
		System.out.println("Replaced Note : " + note);
		
		note = note.replaceAll("  ", " &nbsp;");
		note = note.replaceAll("\r\n","<br>");
		note = note.replaceAll("\n","<br>");
		note = note.replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
		
		note = "<p><h3>" + dateStr + "에 남긴 기록</h3>\n<blockquote>" + note + "</blockquote><br /></p>";
		
		System.out.println(note);
		try {
			springnote.updatePage(page.getId(), note + page.getContent());
			
			// 메시지를 추가한 뒤에, page를 다시 불러와야 한다.
			// 그렇지 않으면 변경내용이 적용되지 않는다.
			setPage(page.getId());
		} catch (Exception e) {
			throw new NoteTakerException("노트 추가에 실패하였습니다.");
		}
	}
}