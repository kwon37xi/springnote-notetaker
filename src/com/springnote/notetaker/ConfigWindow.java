package com.springnote.notetaker;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import rath.toys.springnote.PageMeta;

public class ConfigWindow extends JDialog {
	private static final long serialVersionUID = 1L;

	Config config = Config.getInstance();

	Label userOpenIdLabel = new Label("사용자 오픈 아이디");

	Label userDomainNameLabel = new Label("스프링노트 도메인명");

	Label userKeyLabel = new Label("사용자 키");

	Label rowLabel = new Label("행");

	Label columnLabel = new Label("열");

	JTextField userOpenIdTextField = new JTextField(config.getOpenId(), 32);

	JTextField userDomainNameTextField = new JTextField(config
			.getUserDomainName(), 32);

	JPasswordField userKeyPasswordField = new JPasswordField(config.getUserKey(), 32);

	JTextField rowTextField = new JTextField(String.valueOf(config.getRows()),
			3);

	JTextField columnTextField = new JTextField(String
			.valueOf(config.getCols()), 3);

	JButton okButton = new JButton("확인");

	JButton cancelButton = new JButton("취소");

	JButton resetButton = new JButton("설정 정보 지우기");

	JButton getPagesButton = new JButton("페이지 가져오기");

	JComboBox pagesComboBox = new JComboBox();

	public ConfigWindow() {
		super();
		this.setModal(true);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		JPanel configPanel = new JPanel();
		configPanel.setLayout(new GridLayout(6, 1));

		populateValues();

		// 페이지 목록 가져오기 액션
		ActionListener getPagesActionListener = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				NoteTaker noteTaker = new NoteTaker(userOpenIdTextField
						.getText(), userDomainNameTextField.getText(),
						String.valueOf(userKeyPasswordField.getPassword()));
				List<PageMeta> pages = noteTaker.fetchPageList();

				System.out.println("페이지 목록 : " + pages.size());
				pagesComboBox.removeAllItems();
				for (PageMeta pageMeta : pages) {
					TempPageMeta tempPageMeta = new TempPageMeta(pageMeta);
					pagesComboBox.addItem(tempPageMeta);
					if (pageMeta.getId() == config.getPageId()) {
						pagesComboBox.setSelectedItem(tempPageMeta);
					}
				}

			}
		};
		getPagesButton.addActionListener(getPagesActionListener);

		// 확인 액션
		ActionListener okActionListener = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("확인");
				config.setOpenId(userOpenIdTextField.getText());
				config.setUserDomainName(userDomainNameTextField.getText());
				config.setUserKey(String.valueOf(userKeyPasswordField.getPassword()));
				TempPageMeta selectedTempPageMeta = (TempPageMeta) pagesComboBox
						.getSelectedItem();
				if (selectedTempPageMeta == null) {
					config.setPageId(0);
					config.setPageName("");
				} else {
					config.setPageId(selectedTempPageMeta.getId());
					config.setPageName(selectedTempPageMeta.getName());
				}
				config.setRows(Integer.parseInt(rowTextField.getText()));
				config.setCols(Integer.parseInt(columnTextField.getText()));
				config.save();

				dispose();
			}
		};

		okButton.addActionListener(okActionListener);

		// 취소 액션
		ActionListener cancelActionListener = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (JOptionPane.showConfirmDialog(null,
						"정말로 취소하시겠습니까? 변경내용이 저장되지 않습니다.",
						"취소",
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					dispose();
				}
			}
		};

		cancelButton.addActionListener(cancelActionListener);

		// 설정정보지우기 액션
		ActionListener resetActionListener = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (JOptionPane.showConfirmDialog(null,
						"정말로 설정 정보를 삭제하시겠습니까? 모든 설정 정보가 사라집니다.",
						"취소",
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					config.reset();
					populateValues();
				}
			}
		};

		resetButton.addActionListener(resetActionListener);

		JPanel userOpenIdPanel = new JPanel(new FlowLayout());
		userOpenIdPanel.add(userOpenIdLabel);
		userOpenIdPanel.add(userOpenIdTextField);
		configPanel.add(userOpenIdPanel);

		JPanel userDomainNamePanel = new JPanel(new FlowLayout());
		userDomainNamePanel.add(userDomainNameLabel);
		userDomainNamePanel.add(userDomainNameTextField);
		configPanel.add(userDomainNamePanel);

		JPanel userKeyPanel = new JPanel(new FlowLayout());
		userKeyPanel.add(userKeyLabel);
		userKeyPanel.add(userKeyPasswordField);
		configPanel.add(userKeyPanel);

		JPanel rowColPanel = new JPanel(new FlowLayout());
		rowColPanel.add(rowLabel);
		rowColPanel.add(rowTextField);
		rowColPanel.add(columnLabel);
		rowColPanel.add(columnTextField);
		configPanel.add(rowColPanel);

		JPanel pageSelectPanel = new JPanel(new FlowLayout());

		pageSelectPanel.add(pagesComboBox);
		pageSelectPanel.add(getPagesButton);
		configPanel.add(pageSelectPanel);

		JPanel buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		buttonPanel.add(resetButton);
		configPanel.add(buttonPanel);

		this.add(configPanel);
		pack();
	}

	protected void populateValues() {
		userOpenIdTextField.setText(config.getOpenId());
		userDomainNameTextField.setText(config.getUserDomainName());
		userKeyPasswordField.setText(config.getUserKey());
		rowTextField.setText(String.valueOf(config.getRows()));
		columnTextField.setText(String.valueOf(config.getCols()));
		pagesComboBox.removeAllItems();

		if (config.getPageId() != 0) {
			PageMeta currentPageMeta = new PageMeta();
			currentPageMeta.setId(config.getPageId());
			currentPageMeta.setName(config.getPageName());
			pagesComboBox.addItem(new TempPageMeta(currentPageMeta));
		}
	}

	protected void processWindowEvent(WindowEvent event) {
		System.out.println("event id : " + event.getID());
		if (event.getID() == WindowEvent.WINDOW_CLOSING) {
			if (JOptionPane.showConfirmDialog(this,
					"정말로 창을 닫으시겠습니까? 변경내용이 저장되지 않습니다.",
					"설정창을 닫습니다.",
					JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				dispose();
			}
		}
	}
}
