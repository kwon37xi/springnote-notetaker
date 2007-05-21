package com.springnote.notetaker;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class NoteTakerMain {
	public TrayIcon trayIcon;

	/** 글 입력창 */
	public NoteWindow noteWindow = null;

	/** 설정 창 */
	public JDialog configWindow = null;

	Config config = Config.getInstance();

	/**
	 * Notetaker를 실행한다.
	 * @param args 어떠한 외부 파라미터도 받아들이지 않는다.
	 */
	public static void main(String[] args) {
		// 시스템 트레이 등록
		if (!SystemTray.isSupported()) {
			JOptionPane
					.showMessageDialog(null, "시스템 트레이를 지원하지 않으면 사용할 수 없습니다.");
			System.exit(1);
		}

		if (System.getProperty("os.name").indexOf("Windows") >= 0) {
			try {
				// LookAndFeel을 시스템의 기본 값으로 설정한다.
				// Java 5.0에서 Windows XP의 경우 XP 테마로 나온다.
				UIManager.setLookAndFeel(UIManager
						.getSystemLookAndFeelClassName());
			} catch (Exception ex) {
				// LookAndFeel 설정 실패
				ex.printStackTrace();
			}
		}
		
		NoteTakerMain noteTakerMain = new NoteTakerMain();
		noteTakerMain.runNoteTaker();

	}

	/**
	 * 시스템 트레이 아이콘을 생성하고, 기본 메뉴를 지정한다.
	 */
	public void runNoteTaker() {
		SystemTray tray = SystemTray.getSystemTray();

		Image image = Toolkit
				.getDefaultToolkit()
				.getImage(
						getResource("com/springnote/notetaker/resources/springnote.png"));

		// 메뉴 구성
		// About 메뉴
		ActionListener aboutListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String msg = "StringNote-NoteTaker\n"
						+ "* 목적 : http://www.springnote.com 매시업을 이용해 스프링노트에 실시간 메모 남기기\n"
						+ " - ESC 키 : 현재 창의 내용을 저장하고 창 닫기\n"
						+ " - Ctrl-Enter 키 : 현재 창의 내용저장\n"
						+ "* 라이센스 : 무제한. 제작자는 이 소프트웨어를 사용함으로써 생기는 어떤 문제에도 책임을 지지 않습니다.\n";

				JOptionPane.showMessageDialog(null, msg);
			}
		};

		PopupMenu popup = new PopupMenu();
		MenuItem aboutMenuItem = new MenuItem("Springnote-NoteTaker");
		aboutMenuItem.addActionListener(aboutListener);
		popup.add(aboutMenuItem);

		popup.insertSeparator(1);

		// 설정 메뉴
		ActionListener configListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				configWindow = new ConfigWindow();
				System.out.println("Config..");
				configWindow.setVisible(true);
				noteWindow.setVisible(false);
				noteWindow.setSizeAndLocation(config.getRows(), config
						.getCols());
			}
		};
		MenuItem configMenuItem = new MenuItem("설정");
		configMenuItem.addActionListener(configListener);
		popup.add(configMenuItem);

		// 종료 메뉴
		ActionListener exitListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Exiting...");
				System.exit(0);
			}
		};

		MenuItem exitMenuItem = new MenuItem("종료");
		exitMenuItem.addActionListener(exitListener);
		popup.add(exitMenuItem);

		trayIcon = new TrayIcon(image, "Springnote - NoteTaker", popup);

		// 아이콘에 마우스 더블 클릭시 내용 입력창을 띄운다.
		ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (noteWindow.isVisible()) {
					noteWindow.setVisible(false);
				} else {
					if (config.getOpenId().equals("")
							|| config.getPageId() == 0
							|| config.getUserDomainName().equals("")
							|| config.getUserKey().equals("")) {
						JOptionPane.showMessageDialog(null, "설정정보를 모두 입력하세요.");
					} else {
						noteWindow.setVisible(true);
					}

				}
			}
		};

		trayIcon.setImageAutoSize(true);
		trayIcon.addActionListener(actionListener);

		try {
			tray.add(trayIcon);
			this.noteWindow = new NoteWindow(config.getRows(), config.getCols());
			this.noteWindow.requestFocus();
		} catch (AWTException e) {
			System.err.println("TrayIcon could not be added.");
		}

	}

	/**
	 * 클래스패스 Resource에서 파일을 가져온다.
	 * 
	 * @param name
	 *            파일명
	 * @return
	 */
	public URL getResource(String name) {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		return loader.getResource(name);
	}
}
