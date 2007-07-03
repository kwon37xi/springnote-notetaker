package com.springnote.notetaker;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class Config {

	public static final int DEFAULT_TEXTAREA_ROWS = 3;
	public static final int DEFAULT_TEXTAREA_COLS = 50;
	
	private String userKey = "";

	private String userDomainName = "";

	private String openId = "";

	private int pageId = 0;

	private String pageName = "";

	private int rows = DEFAULT_TEXTAREA_ROWS;

	private int cols = DEFAULT_TEXTAREA_COLS;

	private static Config config = new Config();

	protected Config() {
		load();
	}

	public static Config getInstance() {
		return Config.config;
	}

	public int getCols() {
		return cols;
	}

	public void setCols(int cols) {
		this.cols = cols;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public int getPageId() {
		return pageId;
	}

	public void setPageId(int pageId) {
		this.pageId = pageId;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public String getUserDomainName() {
		return userDomainName;
	}

	public void setUserDomainName(String userDomainName) {
		this.userDomainName = userDomainName;
	}

	public String getUserKey() {
		return userKey;
	}

	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}

	public String toString() {
		return "Springnote-NoteTaker Configuration";
	}
	
	public String getPageName() {
		return pageName;
	}
	
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	/**
	 * 설정 저장
	 */
	public void save() {
		System.out.println("저장");
		Preferences preferenceUser = Preferences
				.userNodeForPackage(NoteTakerMain.class);
		preferenceUser.put("openId", this.getOpenId());
		preferenceUser.put("userDomainName", this.getUserDomainName());
		preferenceUser.put("userKey", this.getUserKey());
		preferenceUser.putInt("pageId", this.getPageId());
		preferenceUser.putInt("rows", this.getRows());
		preferenceUser.putInt("cols", this.getCols());
		preferenceUser.put("pageName", this.pageName);
	}

	/**
	 * 설정 가져오기
	 */
	public void load() {
		Preferences preferenceUser = Preferences
				.userNodeForPackage(NoteTakerMain.class);
		this.setOpenId(preferenceUser.get("openId", ""));
		this.setUserDomainName(preferenceUser.get("userDomainName", ""));
		this.setUserKey(preferenceUser.get("userKey", ""));
		this.setPageId(preferenceUser.getInt("pageId", 0));
		this.setPageName(preferenceUser.get("pageName", ""));
		this.setRows(preferenceUser.getInt("rows", DEFAULT_TEXTAREA_ROWS));
		this.setCols(preferenceUser.getInt("cols", DEFAULT_TEXTAREA_COLS));
	}
	
	public void reset() {
		Preferences preferenceUser = Preferences
				.userNodeForPackage(NoteTakerMain.class);
		try {
			preferenceUser.clear();
			load();
		} catch (BackingStoreException e) {
			throw new NoteTakerException("설정 정보 초기화 실패", e);
		}
	}
}
