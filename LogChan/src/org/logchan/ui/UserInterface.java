package org.logchan.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import javax.swing.JToolBar.Separator;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileFilter;
import org.jdesktop.swingx.MultiSplitLayout.Divider;
import org.jdesktop.swingx.MultiSplitLayout.Leaf;
import org.jdesktop.swingx.MultiSplitLayout.Node;
import org.jdesktop.swingx.MultiSplitLayout.Split;
import org.jdesktop.swingx.MultiSplitPane;

public class UserInterface extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1054889042259220064L;

	private JEditorPane logFileContentArea;
	private JEditorPane templateDetailsArea;	
	private JEditorPane templateSelectArea;
	private JPanel graphArea;
	private JTextField sourceFilePathField;
	private JButton saveButton;
	private JFileChooser fileChooser;
	private String oldFileName = "";
	private String fileName = "";
	private String newFileName = "";

	public UserInterface() throws Exception {
		initialize();
	}

	// TODO i18n
	private void initialize() throws Exception {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new GridBagLayout());
		this.setSize(1000, 600);
		GridBagConstraints constraints = new GridBagConstraints();
		
		constraints.weightx = 0.9;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(0, 0, 0, 20);
		this.add(getFileBrowsePanel(), constraints);
		constraints.weightx = 0.1;
		constraints.gridx = 1;
		constraints.anchor = GridBagConstraints.EAST;
		this.add(getRunButtonPanel(), constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.insets = new Insets(5, 10, 5, 10);
		constraints.weighty = 1;
		constraints.gridwidth = 2;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = new Insets(0, 2, 0, 10);
		
		this.add(getSplitPane(), constraints);
	}

	private MultiSplitPane getSplitPane() throws IOException {
		List<Node> nodes = Arrays.asList(new Leaf("left"), new Divider(), new Leaf("middle"), new Divider(),
				new Leaf("right"));
		Split root = new Split();
		root.setChildren(nodes);
		MultiSplitPane splitPane = new MultiSplitPane();
		splitPane.getMultiSplitLayout().setModel(root);
		//
		JPanel leftPanel = new JPanel(new GridBagLayout());
		JLabel oldProperties = new JLabel("Logs");
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(5, 5, 5, 0);
		constraints.weightx = 0.33;
		leftPanel.add(oldProperties, constraints);
		ScrollPane leftScrollPane = new ScrollPane();
		leftScrollPane.add(getLogFileContentArea());
		constraints.gridy = 1;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weighty = 1;
		constraints.insets = new Insets(0, 0, 0, 0);
		leftPanel.add(leftScrollPane, constraints);
		splitPane.add(leftPanel, "left");
		//
		JPanel midPanel = new JPanel(new GridBagLayout());
		JLabel midProperties= new JLabel("Template Details");
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridy = 0;
		constraints.weighty = 0;
		midPanel.add(midProperties, constraints);
		ScrollPane midScrollPane = new ScrollPane();
		midScrollPane.add(getTemplateDetailsArea());
		constraints.gridy = 1;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weighty = 1;
		constraints.insets = new Insets(0, 0, 0, 0);
		midPanel.add(midScrollPane, constraints);
		splitPane.add(midPanel, "middle");
		//
		JPanel rightPanel = new JPanel(new GridBagLayout());
		JLabel newProperties = new JLabel("Template");
		constraints.gridy = 0;
		constraints.weighty = 0;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = new Insets(5, 5, 5, 0);
		rightPanel.add(newProperties, constraints);
		ScrollPane rightScrollPane = new ScrollPane();
		rightScrollPane.add(getTemplateDisplayArea());
		constraints.gridy = 1;
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = new Insets(0, 0, 0, 0);
		rightPanel.add(rightScrollPane, constraints);
		splitPane.add(rightPanel, "right");

		return splitPane;
	}

	private JEditorPane getLogFileContentArea() throws IOException {
		if (logFileContentArea == null) {
			logFileContentArea = new JEditorPane();
			logFileContentArea.setEditable(false);
		}
		return logFileContentArea;
	}

	private JEditorPane getTemplateDetailsArea() throws IOException {
		if (templateDetailsArea == null) {
			templateDetailsArea = new JEditorPane();
			templateDetailsArea.setEditable(false);
		}
		return templateDetailsArea;
	}

	private JEditorPane getTemplateDisplayArea() throws IOException {
		if (templateSelectArea == null) {
			templateSelectArea = new JEditorPane();
			templateSelectArea.setEditable(false);
		}
		return templateSelectArea;
	}

	private JPanel getFileBrowsePanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(15, 5, 5, 5);
		panel.add(new JLabel("source file"), constraints);
		constraints.gridx = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 1;
		panel.add(getSourceFilePathField(), constraints);
		constraints.weightx = 0;
		constraints.gridx = 2;
		constraints.fill = GridBagConstraints.NONE;
		panel.add(getSourceFileBrowseButton(), constraints);
		Separator leftSeparator = new Separator(new Dimension(new Dimension(2,
				25)));
		leftSeparator.setBorder(new CompoundBorder(new EtchedBorder(
				EtchedBorder.LOWERED, new Color(149, 165, 159), null),
				new EtchedBorder(EtchedBorder.LOWERED,
						new Color(149, 165, 159), null)));
		constraints.gridx = 3;
		panel.add(leftSeparator, constraints);
		Separator rightSeparator = new Separator(new Dimension(new Dimension(2,
				25)));
		rightSeparator.setBorder(new CompoundBorder(new EtchedBorder(
				EtchedBorder.LOWERED, new Color(149, 165, 159), null),
				new EtchedBorder(EtchedBorder.LOWERED,
						new Color(149, 165, 159), null)));
		constraints.gridx = 4;
		panel.add(rightSeparator, constraints);
		return panel;
	}

	private JPanel getRunButtonPanel() throws IOException {
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.EAST;
		constraints.insets = new Insets(10, 2, 0, 2);
		panel.add(getRunButton(getSourceFilePathField().getText().trim()),
				constraints);
		return panel;
	}

	private JButton getRunButton(String fileName) throws IOException {
		if (saveButton == null) {
			saveButton = new JButton("Run");
			saveButton.setToolTipText("run");
			saveButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						// printTokens();

					} catch (Exception ex) {
					}
				}
			});

		}
		return saveButton;
	}

	private JButton getSourceFileBrowseButton() {
		JButton browser = new JButton("Browse");
		browser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser dialog = getFileOpenDialog(sourceFilePathField
						.getText());
				int option = dialog.showOpenDialog(UserInterface.this);
				if (option == JFileChooser.APPROVE_OPTION) {
					try {
						String content = readFile(dialog.getSelectedFile()
								.getAbsolutePath());
						// System.out.println(content);
						sourceFilePathField.setText(dialog.getSelectedFile()
								.getAbsolutePath());
						getLogFileContentArea().setText(content);
						getTemplateDisplayArea().setText(getTemplate());

					} catch (IOException e) {
					}
				}
			}
		});
		return browser;
	}

	private JTextField getSourceFilePathField() {
		if (sourceFilePathField == null) {
			sourceFilePathField = new JTextField(oldFileName);
		}
		return sourceFilePathField;
	}

	private JFileChooser getFileOpenDialog(String selectedFile) {
		if (fileChooser == null) {
			fileChooser = new JFileChooser();
			fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fileChooser.setMultiSelectionEnabled(false);
			// fileChooser.setApproveButtonText(approveButtonText) TODO
			// fileChooser.setApproveButtonToolTipText(toolTipText)
			fileChooser.setAcceptAllFileFilterUsed(false);
			fileChooser
					.setSelectedFile(new File(System.getProperty("user.dir")));
			fileChooser.setFileFilter(new FileFilter() {
				@Override
				public String getDescription() {
					return "*.log";
				}

				@Override
				public boolean accept(File file) {
					return true;
				}
			});
		}
		if (selectedFile != null && new File(selectedFile).exists()) {
			fileChooser.setSelectedFile(new File(selectedFile));
		}
		return fileChooser;
	}

	private String readFile(String fileName) throws IOException {
		StringBuilder content = new StringBuilder("\n");
		if (fileName != null && new File(fileName).exists()
				&& new File(fileName).isFile()) {
			this.fileName = fileName;
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(new File(fileName))));
			String line;
			while ((line = reader.readLine()) != null) {
				content.append(line).append("\n");
			}
		}
		return content.toString();

	}

	private String getTemplate() {
		return "Please specify template";
	}
}
