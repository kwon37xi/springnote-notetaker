package com.springnote.notetaker;

import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class NoteWindow extends JDialog  {

	private static final long serialVersionUID = 1L;

	private JTextArea inputTextArea = null;
	
	public NoteWindow(int rows, int cols) {
		super();
		
		setUndecorated(true);
		
		// 텍스트 입력 상자 만들기
		inputTextArea = new JTextArea("", rows, cols);
		inputTextArea.setEditable(true);
		inputTextArea.setEnabled(true);
		inputTextArea.addKeyListener(keyEventListener);
		
		JScrollPane scrollPane = 
		    new JScrollPane(inputTextArea,
		                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		JPanel notePanel = new JPanel();
		notePanel.setLayout(new FlowLayout());		
		notePanel.add(scrollPane);
		this.setContentPane(notePanel);
		
		// 위치 선정 및 크기 지정
		
		this.setSizeAndLocation(rows,cols);
		this.setAlwaysOnTop(true);
	}
	
	KeyListener keyEventListener = new KeyListener() {

		public void keyPressed(KeyEvent arg0) {
		}

		public void keyReleased(KeyEvent arg0) {
		}

		public void keyTyped(KeyEvent keyEvent) {
			if(Integer.valueOf(keyEvent.getKeyChar()) == KeyEvent.VK_ENTER && keyEvent.getModifiers() == KeyEvent.CTRL_MASK)
			{
				System.out.println("Ctrl+Enter 누름");	
				saveMemo(inputTextArea.getText().trim());
				inputTextArea.setText("");
				
			} else if (Integer.valueOf(keyEvent.getKeyChar()) == KeyEvent.VK_ESCAPE) {
				System.out.println("ESC 누름");
				saveMemo(inputTextArea.getText().trim());
				inputTextArea.setText("");
				setVisible(false);
			}
		}
		
	};
	
	public void saveMemo(String memo){
		if (memo.trim().length() == 0) {
			return;
		}
		
		Config config = Config.getInstance();		
		
		NoteTaker noteTaker = new NoteTaker(config.getOpenId(),config.getUserDomainName(), config.getUserKey());
		noteTaker.setPage(config.getPageId());
		
		noteTaker.addMessageToNote(memo);
	}
	
	public void setSizeAndLocation(int rows,int cols){
		inputTextArea.setRows(rows);
		inputTextArea.setColumns(cols);
		this.pack();
		
		GraphicsEnvironment graphicsEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Rectangle screenSize = graphicsEnv.getMaximumWindowBounds();
		System.out.println("Rectangle : " + screenSize);
		int windowWidth = this.getWidth();
		int windowHeight = this.getHeight();
		this.setLocation((screenSize.width + screenSize.x) - windowWidth, (screenSize.height + screenSize.y) - windowHeight);
	}
	
	
	
	
}