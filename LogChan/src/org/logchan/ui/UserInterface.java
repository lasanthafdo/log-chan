package org.logchan.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.io.ObjectInputStream.GetField;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import javax.swing.JToolBar.Separator;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.jdesktop.swingx.MultiSplitLayout.Divider;
import org.jdesktop.swingx.MultiSplitLayout.Leaf;
import org.jdesktop.swingx.MultiSplitLayout.Node;
import org.jdesktop.swingx.MultiSplitLayout.Split;
import org.jdesktop.swingx.MultiSplitPane;

public class UserInterface extends JFrame implements ActionListener{
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

	private JButton templateButton;

	private JButton recomendationsButton;

	private JMenuBar menuBar;

	private JMenu fileMenu;

	private JMenu editMenu;

	private JMenu helpMenu;

	private JMenuItem saveMenuItem;

	private JMenuItem aboutMenuItem;

	private JMenuItem helpMenuItem;

	private JMenuItem removeMenuItem;

	private JMenuItem refreshMenuItem;

	private JMenuItem exitMenuItem;

	private JMenuItem addServerMenuItem;

	private JMenuItem addGroupMenuItem;

	public UserInterface() throws Exception {
		initialize();
	}

	// TODO i18n
	private void initialize() throws Exception {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new GridBagLayout());
		this.setSize(1000, 600);
		this.setJMenuBar(getJJMenuBar());
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
		constraints.weighty = 1;
		constraints.gridwidth = 2;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = new Insets(0, 2, 0, 10);
		
		this.add(getSplitPane(), constraints);
		
		constraints.gridy = 2;
		constraints.weightx = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		this.add(new JPanel(),constraints);
		constraints.weighty = 0;
		constraints.gridx = 1;
		constraints.fill = GridBagConstraints.NONE;
		this.add(getBottomButtons(),constraints);
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
						getTemplateButton().setEnabled(true);
						getRecomendationsButton().setEnabled(true);

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
			fileChooser.setSelectedFile(new File(System.getProperty("user.dir")));
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
	
	private JPanel getBottomButtons(){
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.EAST;
		panel.add(getTemplateButton(),constraints);
		constraints.gridx = 1;
		panel.add(getRecomendationsButton(),constraints);
		return panel;
	}
   
	private JButton getTemplateButton(){
		if(templateButton == null){
			templateButton = new JButton("View Template");
			templateButton.setEnabled(false);
			templateButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					new TemplateViewer().setVisible(true);
					
				}
			});
		}
		return templateButton;
	}
	private JButton getRecomendationsButton(){
		if(recomendationsButton == null){
			recomendationsButton = new JButton("View Recomendations");
			recomendationsButton.setEnabled(false);
			recomendationsButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					
					
				}
			});
		}
		return recomendationsButton;
	}
	

	  private JMenuBar getJJMenuBar()
	  {
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

	  private JMenu getFileMenu()
	  {
	    fileMenu = new JMenu();
	    fileMenu.setText("File");
	    fileMenu.add(getAddGroupMenuItem());
	    fileMenu.add(getAddServerMenuItem());
	    fileMenu.add(getSaveMenuItem(true));
	    fileMenu.add(getExitMenuItem());

	    return fileMenu;
	  }

	  private JMenu getEditMenu()
	  {
	    editMenu = new JMenu();
	    editMenu.setText("Edit");
	    editMenu.add(getRefreshMenuItem());
	    editMenu.add(getRemoveMenuItem());
	    return editMenu;
	  }

	  private JMenu getHelpMenu()
	  {
	    helpMenu = new JMenu();
	    helpMenu.setText("Help");
	    helpMenu.add(getHelpMenuItem());
	    helpMenu.add(getAboutMenuItem());

	    return helpMenu;
	  }
	  
	  private JMenuItem getAddGroupMenuItem()
	  {
	    addGroupMenuItem = new JMenuItem("Add A Group");
	    addGroupMenuItem.setActionCommand("ADD_GROUP");
	    addGroupMenuItem.addActionListener(this);
	    return addGroupMenuItem;
	  }

	  private JMenuItem getAddServerMenuItem()
	  {
	    addServerMenuItem = new JMenuItem("Add a server");
	    addServerMenuItem.setActionCommand("ADD_SERVER");
	    addServerMenuItem.addActionListener(this);
	    return addServerMenuItem;
	  }

	  private JMenuItem getExitMenuItem()
	  {
	    exitMenuItem = new JMenuItem("Exit");
	    exitMenuItem.setActionCommand("EXIT");
	    exitMenuItem.addActionListener(this);
	    return exitMenuItem;
	  }

	  private JMenuItem getRefreshMenuItem()
	  {

	    refreshMenuItem = new JMenuItem("Refresh");
	    refreshMenuItem.setActionCommand("REFRESH");
	    refreshMenuItem.addActionListener(this);
	    return refreshMenuItem;
	  }

	  private JMenuItem getRemoveMenuItem()
	  {

	    removeMenuItem = new JMenuItem("Remove");
	    removeMenuItem.setActionCommand("REMOVE");
	    removeMenuItem.addActionListener(this);
	    return removeMenuItem;
	  }

	  private JMenuItem getHelpMenuItem()
	  {

	    helpMenuItem = new JMenuItem("Help");
	    helpMenuItem.setActionCommand("HELP");
	    helpMenuItem.addActionListener(this);
	    return helpMenuItem;
	  }

	  private JMenuItem getAboutMenuItem()
	  {

	    aboutMenuItem = new JMenuItem("About");
	    aboutMenuItem.setActionCommand("VIEW_ABOUT");
	    aboutMenuItem.addActionListener(this);
	    return aboutMenuItem;
	  }

	  public JMenuItem getSaveMenuItem(boolean initialize)
	  {
	    if (initialize)
	    {
	      saveMenuItem = new JMenuItem("Save");
	      saveMenuItem.setActionCommand("SAVE");
	      saveMenuItem.addActionListener(this);
	      return saveMenuItem;
	    }
	    else
	    {
	      return saveMenuItem;
	    }

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

	@Override
	public void actionPerformed(ActionEvent e) {


	    String actionCommand = e.getActionCommand();
	    if (actionCommand.equals("RESTART"))
	    {
	      
	    }
	    else if (actionCommand.equals("REFRESH"))
	    {
	      
	    }
	    else if (actionCommand.equals("RENAME_GROUP"))
	    {

	    }
	    else if (actionCommand.equals("RENAME_SERVER"))
	    {
	    }
	    else if (actionCommand.equals("SAVE"))
	    {

	      try
	      {
	        
	      }
	      catch (Exception e1)
	      {
	        
	      }
	    }
	    else if (actionCommand.equals("SERVER_PROPERTIES"))
	    {

	    }
	    else if (actionCommand.equals("ADD_GROUP"))
	    {
	    }
	    else if (actionCommand.equals("START"))
	    {
	      

	    }
	    else if (actionCommand.equals("STOP"))
	    {
	      
	    }
	    else if (actionCommand.equals("PROPERTIES"))
	    {}
	    else if (actionCommand.equals("ADD_SERVER"))
	    {}
	    else if (actionCommand.equals("REMOVE"))
	    {
	    }
	    else if (actionCommand.equals("VIEW_ABOUT"))
	    {
	    }
	    else if (actionCommand.equals("EXIT"))
	    {}
	    else if (actionCommand.equals("HELP"))
	    {
	      try
	      {
	        File f = new File(".");
	        String path = f.getCanonicalPath() + "\\help.html";
	        Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + path);
	      }
	      catch (Exception e1)
	      {
	      }
	    }

	  
	}
}
