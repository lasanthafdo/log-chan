package org.logchan.ui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import org.jdesktop.swingx.MultiSplitLayout.Divider;
import org.jdesktop.swingx.MultiSplitLayout.Leaf;
import org.jdesktop.swingx.MultiSplitLayout.Node;
import org.jdesktop.swingx.MultiSplitLayout.Split;
import org.jdesktop.swingx.MultiSplitPane;
import org.jfree.chart.ChartPanel;
import org.logchan.core.DefaultFlowController;
import org.logchan.core.FlowControllable;
import org.logchan.core.LogReader;
import org.logchan.core.SystemConstants;
import org.logchan.core.SystemMappings;
import org.logchan.core.UIConstants;
import org.logchan.formats.LGCFilter;
import org.logchan.model.TableData;
import org.logchan.reports.LogChart;

public class UserInterface extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1054889042259220064L;

	private RecommendationViewer recommendationViewer;
	private JEditorPane logFileContentArea;
	private JTextField sourceFilePathField;
	private JTextField logPatternField;
	private JComboBox comboBox;
	private JFileChooser fileChooser;
	private FileFilter fileFilter;
	private JButton runButton;
	private JButton clearButton;
	private JButton addTemplateButton;
	private JButton deriveTemplateButton;
	private JButton recomendationsButton;
	private JCheckBox showFileCheckBox;
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenu editMenu;
	private JMenu helpMenu;
	private JMenuItem saveMenuItem;
	private JMenuItem aboutMenuItem;
	private JMenuItem clearMenuItem;
	private JMenuItem exitMenuItem;

	private MultiSplitPane splitPane;

	private FlowControllable flowController = null;
	private String filename = null;

	private List<String[]> messages = null;

	private static final String ACTION_COMMAND_VIEW_OUTPUT = "View Parsed Output";
	private static final String ACTION_COMMAND_VIEW_RECOMENDATIONS = "View Recomendations";

	public UserInterface() throws Exception {
		flowController = DefaultFlowController.getInstance();
		initializeUi();
	}

	// TODO i18n
	private void initializeUi() throws Exception {
		this.setTitle("Log-Chan - Log Analysis Recommendations");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new GridBagLayout());
		this.setSize(1000, 600);
		this.setJMenuBar(getLogJMenuBar());
		GridBagConstraints constraints = new GridBagConstraints();

		constraints.anchor = GridBagConstraints.WEST;
		constraints.gridwidth = 2;
		constraints.weightx = 0.9;
		constraints.insets = new Insets(5, 15, 2, 2);
		this.add(getShowFileCheckPanel(), constraints);

		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridy = 1;
		constraints.insets = new Insets(2, 5, 2, 2);
		this.add(getFileBrowsePanel(), constraints);

		constraints.gridy = 2;
		constraints.weighty = 0.1;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = new Insets(0, 10, 0, 10);
		this.add(getSplitPane(), constraints);

		constraints.weightx = 0.1;
		constraints.gridwidth = 1;
		constraints.gridy = 3;
		constraints.gridx = 1;
		constraints.fill = GridBagConstraints.NONE;
		this.add(getBottomButtons(), constraints);

	}

	private MultiSplitPane getSplitPane() throws IOException {
		splitPane = new MultiSplitPane();
		splitPane.getMultiSplitLayout().setModel(getMultiSplitLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.weightx = 0.1;
		constraints.weighty = 0.1;

		// Display top panel
		displayTopPanel();

		// Add label for mid pane
		JPanel midPanel = new JPanel(new GridBagLayout());
		JLabel midProperties = new JLabel(UIConstants.MID_PANEL_LABEL);
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.insets = new Insets(1, 0, 1, 0);
		constraints.gridy = 0;
		midPanel.add(midProperties, constraints);
		// Add mid scroll pane
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridy = 1;
		constraints.gridwidth = 3;
		constraints.insets = new Insets(0, 0, 0, 0);
		midPanel.add(getLogPatternField(), constraints);
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridy = 2;
		constraints.insets = new Insets(5, 10, 5, 10);
		midPanel.add(getMidPanelWidgets(), constraints);

		splitPane.add(midPanel, "middle");

		return splitPane;
	}

	private JComboBox getKnownStandardsCombo() {
		if (comboBox == null) {
			comboBox = new JComboBox(SystemMappings.EXPRESSION_MAP.keySet()
					.toArray());
		}
		return comboBox;
	}

	private Split getMultiSplitLayout() {
		Leaf topLeaf = new Leaf("top");
		Leaf midLeaf = new Leaf("middle");
		Leaf bottomLeaf = new Leaf("bottom");
		List<Node> nodes = Arrays.asList(topLeaf, new Divider(), midLeaf,
				new Divider(), bottomLeaf);
		Split root = new Split();
		root.setRowLayout(false);
		root.setChildren(nodes);

		return root;
	}

	private void displayTopPanel() {
		JPanel topPanel = new JPanel(new GridBagLayout());
		JLabel logInputLabel = new JLabel(UIConstants.TOP_PANEL_LABEL);
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.insets = new Insets(1, 0, 1, 0);
		constraints.weightx = 0.1;
		constraints.weighty = 0.1;
		constraints.gridy = 0;
		topPanel.add(logInputLabel, constraints);
		//
		ScrollPane topScrollPane = new ScrollPane();
		topScrollPane.add(getLogFileContentArea());
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridy = 1;
		constraints.insets = new Insets(0, 0, 0, 0);
		topPanel.add(topScrollPane, constraints);
		splitPane.add(topPanel, "top");
		splitPane.validate();
	}

	private void displayOutput(Vector<Class<?>> columnTypes) {
		JPanel bottomPanel = new JPanel(new GridBagLayout());
		JLabel bottomLabel = new JLabel(UIConstants.BOTTOM_PANEL_LABEL);
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridy = 0;
		constraints.weighty = 0.1;
		constraints.weightx = 0.1;
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = new Insets(1, 0, 1, 0);
		bottomPanel.add(bottomLabel, constraints);

		Vector<String> columnNames = new Vector<String>();
		for (Class<?> type : columnTypes)
			columnNames.add(type.getSimpleName());

		Vector<Vector<String>> data = new Vector<Vector<String>>();
		for (String[] message : messages) {
			Vector<String> element = new Vector<String>();
			for (int i = 0; i < message.length; i++) {
				element.add(i, message[i]);
			}
			data.add(element);
		}
		ParseTable table = new ParseTable(data, columnNames);
		ScrollPane bottomScrollPane = new ScrollPane();
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.BOTH;
		bottomScrollPane.add(table, constraints);

		constraints.gridy = 1;
		constraints.weighty = 0.1;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = new Insets(0, 0, 0, 0);
		bottomPanel.add(table.getTableHeader(), constraints);

		constraints.gridy = 2;
		constraints.weighty = 0.1;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = new Insets(0, 0, 0, 0);
		bottomPanel.add(bottomScrollPane, constraints);
		splitPane.add(bottomPanel, "bottom");
		splitPane.validate();
	}

	private class ParseTable extends JTable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private ParseTable(Vector<Vector<String>> data,
				Vector<String> columnNames) {
			TableData model = new TableData(data, columnNames);
			this.setModel(model.getModel());
		}
	}

	private JEditorPane getLogFileContentArea() {
		if (logFileContentArea == null) {
			logFileContentArea = new JEditorPane();
			logFileContentArea.setEditable(false);
		}
		return logFileContentArea;
	}

	private JPanel getFileBrowsePanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(15, 5, 5, 5);
		panel.add(new JLabel("Log file"), constraints);
		constraints.gridx = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 1;
		panel.add(getSourceFilePathField(), constraints);
		constraints.weightx = 0;
		constraints.gridx = 2;
		constraints.fill = GridBagConstraints.NONE;
		panel.add(getSourceFileBrowseButton(), constraints);

		return panel;
	}

	private JPanel getShowFileCheckPanel() {
		JPanel panel = new JPanel();
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 0.1;
		constraints.gridx = 0;
		constraints.insets = new Insets(2, 20, 2, 0);
		panel.add(getShowFileCheckBox(), constraints);
		constraints.gridx = 1;
		constraints.insets = new Insets(2, 50, 2, 10);
		panel.add(new JLabel(
				"Do not load file to text pane (Recommended for large files)"),
				constraints);

		return panel;
	}

	private JButton getRunButton() {
		if (runButton == null) {
			runButton = new JButton("Process");
			runButton.setToolTipText("Run and process selected log");
			runButton.addActionListener(new ActionListener() {

				@SuppressWarnings("unchecked")
				@Override
				public void actionPerformed(ActionEvent e) {
					Map<String, Object> metaMap;
					try {
						if (!logPatternField.getText().isEmpty()) {
							messages = flowController.parseFile(filename,
									logPatternField.getText());
							if (!messages.isEmpty()) {
								flowController.processRules();
								metaMap = flowController.getOutputData();
								displayOutput((Vector<Class<?>>) metaMap
										.get(SystemConstants.COL_DATA_TYPES));
								recommendationViewer = new RecommendationViewer(
										metaMap);
								recommendationViewer.populateRecommendations();
								recommendationViewer.addInfoPanel();
								Map<Integer, Integer> dataMap = flowController
										.getTimeMarshalledData(messages);
								if (dataMap != null) {
									ChartPanel panel = new LogChart()
											.createChart(dataMap);
									recommendationViewer.addChart(panel);
								}
								getRecomendationsButton().setEnabled(true);
								getClearButton().setEnabled(true);
							} else {
								JOptionPane.showMessageDialog(
										UserInterface.this,
										"Error in parsing log file",
										"Log-Chan", JOptionPane.ERROR_MESSAGE);
							}
						} else {
							JOptionPane.showMessageDialog(UserInterface.this,
									"Please select log format", "Log-Chan",
									JOptionPane.INFORMATION_MESSAGE);
						}
					} catch (Exception ex) {
						ex.printStackTrace();
						getAddRegexButton().setEnabled(false);
						getRecomendationsButton().setEnabled(false);
					}
				}
			});

		}
		return runButton;
	}

	private JButton getClearButton() {
		if (clearButton == null) {
			clearButton = new JButton("Clear");
			clearButton.setToolTipText("Clear the current data");
			clearButton.setEnabled(false);
			clearButton.setActionCommand("CLEAR");
			clearButton.addActionListener(this);

		}

		return clearButton;
	}

	private JPanel getPanelWithLabel(String checkLabel) {
		for (Component comp : splitPane.getComponents()) {
			if (comp instanceof JPanel
					&& (((JPanel) comp).getComponent(0) instanceof JLabel)) {
				JLabel panelLabel = (JLabel) ((JPanel) comp).getComponent(0);
				if (panelLabel.getText().equals(checkLabel)) {
					return (JPanel) comp;
				}
			}
		}

		return null;
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
					// BufferedReader reader = null;
					if (filename != null && !filename.isEmpty())
						getClearButton().doClick();
					try {
						filename = dialog.getSelectedFile().getAbsolutePath();
						sourceFilePathField.setText(dialog.getSelectedFile()
								.getAbsolutePath());
						if (!showFileCheckBox.isSelected()) {
							String content = new LogReader().readFile(filename);
							getLogFileContentArea().setText(content);
						}

						// reader = new BufferedReader(new InputStreamReader(
						// new LogReader().getInputStream(filename)));
						// Document doc = getLogFileContentArea().getDocument();
						// String str;
						// while ((str = reader.readLine()) != null) {
						// doc.insertString(doc.getLength(), str + "\n", null);
						// }
						// reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		return browser;
	}

	private JTextField getSourceFilePathField() {
		if (sourceFilePathField == null) {
			sourceFilePathField = new JTextField(filename);
		}
		return sourceFilePathField;
	}

	private JTextField getLogPatternField() {
		if (logPatternField == null) {
			logPatternField = new JTextField();
		}
		return logPatternField;
	}

	private JFileChooser getFileOpenDialog(String selectedFile) {
		if (fileChooser == null) {
			fileChooser = new JFileChooser();
		}
		fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setSelectedFile(new File(System.getProperty("user.dir")));
		fileChooser.resetChoosableFileFilters();
		fileChooser.setAcceptAllFileFilterUsed(true);

		if (selectedFile != null && new File(selectedFile).exists()) {
			fileChooser.setSelectedFile(new File(selectedFile));
		}
		return fileChooser;
	}

	private JFileChooser getFileSaveDialog(String selectedFile) {
		if (fileChooser == null) {
			fileChooser = new JFileChooser();
		}
		fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.addChoosableFileFilter(getLgcFileFilter());
		if (selectedFile != null && new File(selectedFile).exists()) {
			File prevFile = new File(selectedFile);
			fileChooser.setSelectedFile(new File(prevFile.getParentFile(),
					prevFile.getName() + ".lgc"));
		}

		return fileChooser;
	}

	private JCheckBox getShowFileCheckBox() {
		if (showFileCheckBox == null) {
			showFileCheckBox = new JCheckBox();
			showFileCheckBox.setActionCommand("FILE_SHOW");
			showFileCheckBox.addActionListener(this);
			showFileCheckBox.setVisible(true);
		}

		return showFileCheckBox;
	}

	private FileFilter getLgcFileFilter() {
		if (fileFilter == null) {
			fileFilter = new LGCFilter();
		}

		return fileFilter;
	}

	private JPanel getBottomButtons() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.EAST;
		constraints.insets = new Insets(2, 2, 2, 2);
		panel.add(getRecomendationsButton(), constraints);
		constraints.gridx = 1;
		panel.add(getClearButton(), constraints);
		return panel;
	}

	private JPanel getMidPanelWidgets() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 0.2;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(2, 10, 2, 10);
		panel.add(getKnownStandardsCombo(), constraints);
		constraints.fill = GridBagConstraints.NONE;
		constraints.insets = new Insets(2, 2, 2, 2);
		constraints.gridx = 1;
		panel.add(getAddRegexButton(), constraints);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 2;
		constraints.insets = new Insets(2, 10, 2, 10);
		panel.add(getDeriveTemplateButton(), constraints);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(2, 10, 2, 10);
		constraints.gridx = 3;
		panel.add(getRunButton(), constraints);

		return panel;
	}

	private JButton getAddRegexButton() {
		if (addTemplateButton == null) {
			addTemplateButton = new JButton("Add Format Regex");
			addTemplateButton.setEnabled(true);
			addTemplateButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					String regexPattern = SystemMappings.EXPRESSION_MAP
							.get((String) comboBox.getSelectedItem());
					logPatternField.setText(regexPattern);
					splitPane.validate();
				}
			});
		}
		return addTemplateButton;
	}

	private JButton getDeriveTemplateButton() {
		if (deriveTemplateButton == null) {
			deriveTemplateButton = new JButton("Derive Regex");
			deriveTemplateButton.setEnabled(true);
			deriveTemplateButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					try {
						logPatternField.setText(flowController
								.getDerivedRegex(filename));
						splitPane.validate();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
		}

		return deriveTemplateButton;
	}

	private JButton getRecomendationsButton() {
		if (recomendationsButton == null) {
			recomendationsButton = new JButton("View Recomendations");
			recomendationsButton.setEnabled(false);
			recomendationsButton
					.setActionCommand(ACTION_COMMAND_VIEW_RECOMENDATIONS);
			recomendationsButton.addActionListener(this);
		}

		return recomendationsButton;
	}

	private JMenuBar getLogJMenuBar() {
		menuBar = new JMenuBar();
		GridBagConstraints fileMenuConstraints = new GridBagConstraints();
		fileMenuConstraints.gridx = 0;
		fileMenuConstraints.gridy = 1;
		fileMenuConstraints.gridheight = 1;
		fileMenuConstraints.gridwidth = 1;
		fileMenuConstraints.ipadx = 0;
		fileMenuConstraints.ipady = 0;
		fileMenuConstraints.fill = GridBagConstraints.NONE;
		fileMenuConstraints.anchor = GridBagConstraints.NORTH;
		fileMenuConstraints.insets = new Insets(0, 0, 0, 0);
		fileMenuConstraints.weightx = 0;
		fileMenuConstraints.weighty = 0;
		menuBar.add(getFileMenu(), fileMenuConstraints);

		GridBagConstraints editMenuConstraints = new GridBagConstraints();
		editMenuConstraints.gridx = 1;
		editMenuConstraints.gridy = 0;
		editMenuConstraints.gridheight = 1;
		editMenuConstraints.gridwidth = 1;
		editMenuConstraints.ipadx = 0;
		editMenuConstraints.ipady = 0;
		editMenuConstraints.fill = GridBagConstraints.NONE;
		editMenuConstraints.anchor = GridBagConstraints.NORTH;
		editMenuConstraints.insets = new Insets(0, 0, 0, 0);
		editMenuConstraints.weightx = 0;
		editMenuConstraints.weighty = 0;
		menuBar.add(getEditMenu(), editMenuConstraints);

		GridBagConstraints helpMenuConstraints = new GridBagConstraints();
		helpMenuConstraints.gridx = 2;
		helpMenuConstraints.gridy = 0;
		helpMenuConstraints.gridheight = 1;
		helpMenuConstraints.gridwidth = 1;
		helpMenuConstraints.ipadx = 0;
		helpMenuConstraints.ipady = 0;
		helpMenuConstraints.fill = GridBagConstraints.NONE;
		helpMenuConstraints.anchor = GridBagConstraints.NORTH;
		helpMenuConstraints.insets = new Insets(0, 0, 0, 0);
		helpMenuConstraints.weightx = 0;
		helpMenuConstraints.weighty = 0;
		menuBar.add(getHelpMenu(), helpMenuConstraints);

		return menuBar;
	}

	private JMenu getFileMenu() {
		fileMenu = new JMenu();
		fileMenu.setText("File");
		fileMenu.add(getSaveMenuItem(true));
		fileMenu.add(getExitMenuItem());

		return fileMenu;
	}

	private JMenu getEditMenu() {
		editMenu = new JMenu();
		editMenu.setText("Edit");
		editMenu.add(getClearMenuItem());
		return editMenu;
	}

	private JMenu getHelpMenu() {
		helpMenu = new JMenu();
		helpMenu.setText("Help");
		// helpMenu.add(getHelpMenuItem());
		helpMenu.add(getAboutMenuItem());

		return helpMenu;
	}

	private JMenuItem getExitMenuItem() {
		exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.setActionCommand("EXIT");
		exitMenuItem.addActionListener(this);
		return exitMenuItem;
	}

	private JMenuItem getClearMenuItem() {

		clearMenuItem = new JMenuItem("Clear");
		clearMenuItem.setActionCommand("CLEAR");
		clearMenuItem.addActionListener(this);
		return clearMenuItem;
	}

	// private JMenuItem getHelpMenuItem() {
	// helpMenuItem = new JMenuItem("Help");
	// helpMenuItem.setActionCommand("HELP");
	// helpMenuItem.addActionListener(this);
	// return helpMenuItem;
	// }

	private JMenuItem getAboutMenuItem() {

		aboutMenuItem = new JMenuItem("About");
		aboutMenuItem.setActionCommand("VIEW_ABOUT");
		aboutMenuItem.addActionListener(this);
		return aboutMenuItem;
	}

	public JMenuItem getSaveMenuItem(boolean initialize) {
		if (initialize) {
			saveMenuItem = new JMenuItem("Save");
			saveMenuItem.setActionCommand("SAVE");
			saveMenuItem.addActionListener(this);
			return saveMenuItem;
		} else {
			return saveMenuItem;
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		String actionCommand = e.getActionCommand();
		if (actionCommand.equals(ACTION_COMMAND_VIEW_OUTPUT)) {
			new TemplateViewer(messages).setVisible(true);
		} else if (actionCommand.equals(ACTION_COMMAND_VIEW_RECOMENDATIONS)) {
			if (recommendationViewer != null)
				recommendationViewer.setVisible(true);
		} else if (actionCommand.equals("CLEAR")) {
			JPanel bottomPanel = getPanelWithLabel(UIConstants.BOTTOM_PANEL_LABEL);
			if (bottomPanel != null) {
				splitPane.remove(bottomPanel);
				splitPane.getMultiSplitLayout().removeLayoutComponent(bottomPanel);
				splitPane.validate();
			}
			getLogFileContentArea().setText("");
			getSourceFilePathField().setText("");
			getLogPatternField().setText("");
			flowController.reset();
			getRecomendationsButton().setEnabled(false);
			getClearButton().setEnabled(false);
		} else if (actionCommand.equals("SAVE")) {
			JFileChooser dialog = getFileSaveDialog(filename);
			int option = dialog.showSaveDialog(UserInterface.this);
			if (option == JFileChooser.APPROVE_OPTION) {
				try {
					filename = dialog.getSelectedFile().getAbsolutePath();
					flowController.saveFile(filename);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		} else if (actionCommand.equals("VIEW_ABOUT")) {
			new AboutDialog().setVisible(true);
		} else if (actionCommand.equals("EXIT")) {
			this.dispose();
		} else if (actionCommand.equals("HELP")) {

		} else if (actionCommand.equals("FILE_SHOW")) {
			JPanel topPanel = getPanelWithLabel(UIConstants.TOP_PANEL_LABEL);
			if (topPanel != null) {
				if (showFileCheckBox.isSelected()) {
					topPanel.setVisible(false);
					splitPane.getMultiSplitLayout().removeLayoutComponent(topPanel);					
				} else {
					topPanel.setVisible(true);
					splitPane.getMultiSplitLayout().addLayoutComponent("top", topPanel);
				}
				splitPane.validate();
			}
		}
	}
}
