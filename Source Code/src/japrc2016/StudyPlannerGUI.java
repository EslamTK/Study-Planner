package japrc2016;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import japrc2016.StudyPlannerInterface.CalendarEventType;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import java.awt.FlowLayout;
import java.awt.event.WindowStateListener;
import java.awt.event.WindowEvent;

public final class StudyPlannerGUI extends JFrame implements StudyPlannerGUIInterface {
	
	private StudyPlanner planner;
	private JButton generateButton;
	private JButton exitButton;
	
	//To Organize Control Buttons Layout
	private JPanel panelControlBtns;
	
	//Topic List Section
	private JPanel panelTopics;
	private JList<String> topicList;
	private JLabel topicLabel;
	private JLabel planLabel;
	
	//Level3.1.a
	private JPanel panelAddTopic;
	private JLabel lblTopicName;
	private JTextField txtTopicName;
	private JLabel lblStudyLength;
	private JSpinner spinnerStudyLength;
	private JButton btnAddTopic;
	
	//Leve3.6
	private JLabel lblTarget;
	private JComboBox comboTargetEvent;
	
	//Level3.1.b
	private JButton btnDeleteTopic;
	
	//Level3.2
	private JPanel panelStudyPlan;
	private JList<String> studyPlan;
	
	//Level3.4
	private JPanel panelAddEvent;
	private JLabel lblEventName;
	private JTextField txtEventName;
	private JLabel lblEventLength;
	private JSpinner spinnerEventLength;
	private JButton btnAddEvent;
	private JLabel lblDate;
	private JSpinner spinnerDate;
	private JLabel lblType;
	private JComboBox comboEventType;
	
	//Level3.7
	private JButton btnSaveData;
	private JButton btnLoadData;
	private JFileChooser fileChooser;
	
	//For Organize Add Topic And Add Event Sections Layouts
	private JPanel panelTopicEventAdd;
	
	//Day Control Section
	private JPanel panelDay;
	private JPanel panelDayControls;
	private JLabel lblStartDay;
	private JSpinner spinnerStartTime;
	private JLabel lblEndTime;
	private JSpinner spinnerEndTime;
	private JLabel lblStudyLength_1;
	private JSpinner spinnerStudyLength_1;
	private JLabel lblBreakLength;
	private JSpinner spinnerBreakLength;
	
	

	public StudyPlannerGUI(StudyPlanner simToUse) {
		super("Study Planner");
		this.planner = simToUse;
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        
		//File Chooser to choose a file to save data to it or loading data from it
		fileChooser = new JFileChooser();
		fileChooser.setFileFilter(HelperUtility.getFileFilter());
		fileChooser.setAcceptAllFileFilterUsed(false);
        
		//panel to organize the layout of the control buttons the generateButton,exitButton,btnSaveData,btnLoadData
		panelControlBtns = new JPanel();
		getContentPane().add(panelControlBtns);
		GridBagLayout gbl_panelControlBtns = new GridBagLayout();
		gbl_panelControlBtns.columnWeights = new double[] { 0.0, 0.0 };
		gbl_panelControlBtns.rowWeights = new double[] { 0.0, 0.0 };
		panelControlBtns.setLayout(gbl_panelControlBtns);
        
		//button to call StudyPlanner.GenerateStudyPlan to Generate a Study Plan
		generateButton = new JButton("Generate Study Plan");
		GridBagConstraints gbc_generateButton = new GridBagConstraints();
		gbc_generateButton.insets = new Insets(0, 0, 0, 5);
		gbc_generateButton.fill = GridBagConstraints.BOTH;
		gbc_generateButton.gridx = 0;
		gbc_generateButton.gridy = 1;
		panelControlBtns.add(generateButton, gbc_generateButton);
		generateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					Calendar startTime = Calendar.getInstance();
					HelperUtility.roundCalendarToMinutes(startTime);
					startTime.set(Calendar.MINUTE, 0);
					startTime.set(Calendar.HOUR_OF_DAY, Integer.valueOf(spinnerStartTime.getValue().toString()));
					Calendar endTime = (Calendar) startTime.clone();
					endTime.set(Calendar.HOUR_OF_DAY, Integer.valueOf(spinnerEndTime.getValue().toString()));

					planner.setDailyStartAndEndStudyTime(startTime, endTime);
					planner.setBlockSize(Integer.valueOf(spinnerStudyLength_1.getValue().toString()));
					planner.setBreakLength(Integer.valueOf(spinnerBreakLength.getValue().toString()));
					planner.generateStudyPlan();
				} catch (Exception e) {
					//Lavel3.8
					JOptionPane.showMessageDialog(StudyPlannerGUI.this, e.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
        //button to exit the app
		exitButton = new JButton("Exit Program");
		GridBagConstraints gbc_exitButton = new GridBagConstraints();
		gbc_exitButton.insets = new Insets(0, 5, 0, 0);
		gbc_exitButton.fill = GridBagConstraints.BOTH;
		gbc_exitButton.gridx = 1;
		gbc_exitButton.gridy = 1;
		panelControlBtns.add(exitButton, gbc_exitButton);
        
		//button to open the file chooser window to select a file and create a OutputStream object of it 
		//and call StudyPlanner.saveData with it to save data to it
		btnSaveData = new JButton("Save Data");
		btnSaveData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fileChooser.setSelectedFile(new File("New Text.txt"));
				if(fileChooser.showSaveDialog(StudyPlannerGUI.this)==JFileChooser.APPROVE_OPTION)
				{
					File file = fileChooser.getSelectedFile();
					try {
                        file.createNewFile();
						OutputStream out = new FileOutputStream(file);
						planner.saveData(out);
					} catch (Exception e) {
						//Lavel3.8
						JOptionPane.showMessageDialog(StudyPlannerGUI.this, e.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				}	
				}
			});
		GridBagConstraints gbc_btnSaveData = new GridBagConstraints();
		gbc_btnSaveData.insets = new Insets(0, 0, 5, 5);
		gbc_btnSaveData.fill = GridBagConstraints.BOTH;
		gbc_btnSaveData.gridx = 0;
		gbc_btnSaveData.gridy = 0;
		panelControlBtns.add(btnSaveData, gbc_btnSaveData);
        
		//button to open the file chooser window to select a file and create a InputStream object of it 
		//and call StudyPlanner.loadData with it to get data from it
		btnLoadData = new JButton("Load Data");
		btnLoadData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(fileChooser.showOpenDialog(StudyPlannerGUI.this)==JFileChooser.APPROVE_OPTION)
				{
				File file = fileChooser.getSelectedFile();
					InputStream input;
					try {
						input = new FileInputStream(file);
						planner.loadData(input);
						updateDisplay();
					} catch (Exception e) {
						//Lavel3.8
						JOptionPane.showMessageDialog(StudyPlannerGUI.this, e.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		GridBagConstraints gbc_btnLoadData = new GridBagConstraints();
		gbc_btnLoadData.insets = new Insets(0, 5, 5, 0);
		gbc_btnLoadData.fill = GridBagConstraints.BOTH;
		gbc_btnLoadData.gridx = 1;
		gbc_btnLoadData.gridy = 0;
		panelControlBtns.add(btnLoadData, gbc_btnLoadData);
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
        
		//panel to organize the layout of the add event and add topic panels
		panelTopicEventAdd = new JPanel();
		getContentPane().add(panelTopicEventAdd);
        
		//panel to organize the layout of the add topic controls
		panelAddTopic = new JPanel();
		panelTopicEventAdd.add(panelAddTopic);
		panelAddTopic
				.setBorder(new TitledBorder(null, "Add Topic", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagLayout gbl_panelAddTopic = new GridBagLayout();
		gbl_panelAddTopic.columnWidths = new int[] { 80, 150, 0 };
		gbl_panelAddTopic.rowHeights = new int[] { 35, 35, 35, 35 };
		gbl_panelAddTopic.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_panelAddTopic.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0 };
		panelAddTopic.setLayout(gbl_panelAddTopic);

		lblTopicName = new JLabel("Topic Name : ");
		lblTopicName.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblTopicName = new GridBagConstraints();
		gbc_lblTopicName.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblTopicName.insets = new Insets(0, 0, 5, 5);
		gbc_lblTopicName.gridx = 0;
		gbc_lblTopicName.gridy = 0;
		panelAddTopic.add(lblTopicName, gbc_lblTopicName);

		txtTopicName = new JTextField();
		txtTopicName.setHorizontalAlignment(SwingConstants.LEFT);
		txtTopicName.setToolTipText("");
		txtTopicName.setColumns(15);
		GridBagConstraints gbc_txtTopicName = new GridBagConstraints();
		gbc_txtTopicName.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtTopicName.insets = new Insets(0, 0, 5, 0);
		gbc_txtTopicName.gridx = 1;
		gbc_txtTopicName.gridy = 0;
		panelAddTopic.add(txtTopicName, gbc_txtTopicName);

		lblStudyLength = new JLabel("Study Length : ");
		lblStudyLength.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblStudyLength = new GridBagConstraints();
		gbc_lblStudyLength.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblStudyLength.insets = new Insets(0, 0, 5, 5);
		gbc_lblStudyLength.gridx = 0;
		gbc_lblStudyLength.gridy = 1;
		panelAddTopic.add(lblStudyLength, gbc_lblStudyLength);

		spinnerStudyLength = new JSpinner();
		spinnerStudyLength.setModel(new SpinnerNumberModel(60, 10, 60000, 1));
		spinnerStudyLength.setSize(50, 50);

		GridBagConstraints gbc_spinnerStudyLength = new GridBagConstraints();
		gbc_spinnerStudyLength.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerStudyLength.insets = new Insets(0, 0, 5, 0);
		gbc_spinnerStudyLength.gridx = 1;
		gbc_spinnerStudyLength.gridy = 1;
		panelAddTopic.add(spinnerStudyLength, gbc_spinnerStudyLength);
        
		//button to get the data from the controls of add topic and call StudyPlanner.addTopic with this data to add the new Topic
		btnAddTopic = new JButton("Add");
		btnAddTopic.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					planner.addTopic(txtTopicName.getText(),
							Integer.parseInt(spinnerStudyLength.getValue().toString()));
					txtTopicName.setText("");
					spinnerStudyLength.setValue(10);
					if (comboTargetEvent.getSelectedIndex() > -1) {
						CalendarEventInterface event = planner.getCalendarEvents()
								.get(comboTargetEvent.getSelectedIndex());
						planner.getTopics().get(planner.getTopics().size() - 1).setTargetEvent(event);
					}
				} catch (Exception e) {
					//Lavel3.8
					JOptionPane.showMessageDialog(StudyPlannerGUI.this, e.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnAddTopic.setAlignmentY(Component.TOP_ALIGNMENT);
		GridBagConstraints gbc_btnAddTopic = new GridBagConstraints();
		gbc_btnAddTopic.insets = new Insets(5, 0, 5, 0);
		gbc_btnAddTopic.gridwidth = 2;
		gbc_btnAddTopic.gridx = 0;
		gbc_btnAddTopic.gridy = 3;
		panelAddTopic.add(btnAddTopic, gbc_btnAddTopic);

		lblTarget = new JLabel("Target :");
		GridBagConstraints gbc_lblTarget = new GridBagConstraints();
		gbc_lblTarget.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblTarget.insets = new Insets(0, 0, 5, 5);
		gbc_lblTarget.gridx = 0;
		gbc_lblTarget.gridy = 2;
		panelAddTopic.add(lblTarget, gbc_lblTarget);

		comboTargetEvent = new JComboBox();
		GridBagConstraints gbc_comboTargetEvent = new GridBagConstraints();
		gbc_comboTargetEvent.insets = new Insets(0, 0, 5, 0);
		gbc_comboTargetEvent.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboTargetEvent.gridx = 1;
		gbc_comboTargetEvent.gridy = 2;
		panelAddTopic.add(comboTargetEvent, gbc_comboTargetEvent);
        
		//panel to organize the layout of the add event controls
		panelAddEvent = new JPanel();
		panelTopicEventAdd.add(panelAddEvent);
		panelAddEvent.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Add Event",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		GridBagLayout gbl_panelAddEvent = new GridBagLayout();
		gbl_panelAddEvent.columnWidths = new int[] { 80, 150, 0 };
		gbl_panelAddEvent.rowHeights = new int[] { 23, 23, 23, 0, 0, 0, 0 };
		gbl_panelAddEvent.columnWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE };
		gbl_panelAddEvent.rowWeights = new double[] { 0.0, 0.0, 1.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		panelAddEvent.setLayout(gbl_panelAddEvent);

		lblEventName = new JLabel("Event Name : ");
		lblEventName.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblEventName = new GridBagConstraints();
		gbc_lblEventName.fill = GridBagConstraints.BOTH;
		gbc_lblEventName.insets = new Insets(0, 0, 5, 5);
		gbc_lblEventName.gridx = 0;
		gbc_lblEventName.gridy = 0;
		panelAddEvent.add(lblEventName, gbc_lblEventName);

		txtEventName = new JTextField();
		txtEventName.setToolTipText("");
		txtEventName.setHorizontalAlignment(SwingConstants.LEFT);
		txtEventName.setColumns(15);
		GridBagConstraints gbc_txtEventName = new GridBagConstraints();
		gbc_txtEventName.fill = GridBagConstraints.BOTH;
		gbc_txtEventName.insets = new Insets(0, 0, 5, 0);
		gbc_txtEventName.gridx = 1;
		gbc_txtEventName.gridy = 0;
		panelAddEvent.add(txtEventName, gbc_txtEventName);

		spinnerDate = new JSpinner();
		spinnerDate.setModel(new SpinnerDateModel(new Date(1483221600000L), new Date(1451599200000L),
				new Date(1577829600000L), Calendar.HOUR_OF_DAY));
		GridBagConstraints gbc_spinnerDate = new GridBagConstraints();
		gbc_spinnerDate.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerDate.insets = new Insets(0, 0, 5, 0);
		gbc_spinnerDate.gridx = 1;
		gbc_spinnerDate.gridy = 1;
		panelAddEvent.add(spinnerDate, gbc_spinnerDate);

		lblEventLength = new JLabel("Event Length : ");
		lblEventLength.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblEventLength = new GridBagConstraints();
		gbc_lblEventLength.fill = GridBagConstraints.BOTH;
		gbc_lblEventLength.insets = new Insets(0, 0, 5, 5);
		gbc_lblEventLength.gridx = 0;
		gbc_lblEventLength.gridy = 2;
		panelAddEvent.add(lblEventLength, gbc_lblEventLength);

		spinnerEventLength = new JSpinner();
		spinnerEventLength.setModel(new SpinnerNumberModel(1, 1, 60000, 1));
		GridBagConstraints gbc_spinnerEventLength = new GridBagConstraints();
		gbc_spinnerEventLength.fill = GridBagConstraints.BOTH;
		gbc_spinnerEventLength.insets = new Insets(0, 0, 5, 0);
		gbc_spinnerEventLength.gridx = 1;
		gbc_spinnerEventLength.gridy = 2;
		panelAddEvent.add(spinnerEventLength, gbc_spinnerEventLength);

		lblType = new JLabel("Type :");
		GridBagConstraints gbc_lblType = new GridBagConstraints();
		gbc_lblType.fill = GridBagConstraints.BOTH;
		gbc_lblType.insets = new Insets(0, 0, 5, 5);
		gbc_lblType.gridx = 0;
		gbc_lblType.gridy = 3;
		panelAddEvent.add(lblType, gbc_lblType);

		comboEventType = new JComboBox();
		comboEventType.setModel(new DefaultComboBoxModel(CalendarEventType.values()));
		GridBagConstraints gbc_comboEventType = new GridBagConstraints();
		gbc_comboEventType.fill = GridBagConstraints.BOTH;
		gbc_comboEventType.insets = new Insets(0, 0, 5, 0);
		gbc_comboEventType.gridx = 1;
		gbc_comboEventType.gridy = 3;
		panelAddEvent.add(comboEventType, gbc_comboEventType);
        
		//button to get the data from the controls of add event and call StudyPlanner.addCalendarEvent with this data to add the new event
		btnAddEvent = new JButton("Add");
		btnAddEvent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Calendar startTime = Calendar.getInstance();
					startTime.setTime((Date) spinnerDate.getValue());
					planner.addCalendarEvent(txtEventName.getText(), startTime,
							Integer.parseInt(spinnerEventLength.getValue().toString()),
							CalendarEventType.valueOf(comboEventType.getSelectedItem().toString()));
					txtEventName.setText("");
					spinnerEventLength.setValue(1);
					comboEventType.setSelectedIndex(0);
				} catch (Exception e) {
					//Lavel3.8
					JOptionPane.showMessageDialog(StudyPlannerGUI.this, e.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnAddEvent.setAlignmentY(0.0f);
		GridBagConstraints gbc_btnAddEvent = new GridBagConstraints();
		gbc_btnAddEvent.insets = new Insets(5, 0, 5, 0);
		gbc_btnAddEvent.gridwidth = 2;
		gbc_btnAddEvent.gridx = 0;
		gbc_btnAddEvent.gridy = 4;
		panelAddEvent.add(btnAddEvent, gbc_btnAddEvent);

		lblDate = new JLabel("Date :");
		GridBagConstraints gbc_lblDate = new GridBagConstraints();
		gbc_lblDate.anchor = GridBagConstraints.WEST;
		gbc_lblDate.insets = new Insets(0, 0, 5, 5);
		gbc_lblDate.gridx = 0;
		gbc_lblDate.gridy = 1;
		panelAddEvent.add(lblDate, gbc_lblDate);
		
		panelDay = new JPanel();
		getContentPane().add(panelDay);
		panelDay.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		panelDayControls = new JPanel();
		panelDayControls.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Day Study", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelDay.add(panelDayControls);
		GridBagLayout gbl_panelDayControls = new GridBagLayout();
		gbl_panelDayControls.columnWidths = new int[] {100, 100, 30, 100, 100, 5};
		gbl_panelDayControls.rowHeights = new int[] {30, 30};
		gbl_panelDayControls.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
		gbl_panelDayControls.rowWeights = new double[]{0.0, 0.0};
		panelDayControls.setLayout(gbl_panelDayControls);
		
		lblStartDay = new JLabel("Start Time :");
		GridBagConstraints gbc_lblStartDay = new GridBagConstraints();
		gbc_lblStartDay.gridx = 0;
		gbc_lblStartDay.gridy = 0;
		panelDayControls.add(lblStartDay, gbc_lblStartDay);
		
		spinnerStartTime = new JSpinner();
		spinnerStartTime.setModel(new SpinnerNumberModel(9, 0, 23, 1));
		GridBagConstraints gbc_spinnerStartTime = new GridBagConstraints();
		gbc_spinnerStartTime.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerStartTime.gridx = 1;
		gbc_spinnerStartTime.gridy = 0;
		panelDayControls.add(spinnerStartTime, gbc_spinnerStartTime);
		
		lblEndTime = new JLabel("End Time :");
		GridBagConstraints gbc_lblEndTime = new GridBagConstraints();
		gbc_lblEndTime.gridx = 3;
		gbc_lblEndTime.gridy = 0;
		panelDayControls.add(lblEndTime, gbc_lblEndTime);
		
		spinnerEndTime = new JSpinner();
		spinnerEndTime.setModel(new SpinnerNumberModel(17, 0, 23, 1));
		GridBagConstraints gbc_spinnerEndTime = new GridBagConstraints();
		gbc_spinnerEndTime.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerEndTime.gridx = 4;
		gbc_spinnerEndTime.gridy = 0;
		panelDayControls.add(spinnerEndTime, gbc_spinnerEndTime);
		
		lblStudyLength_1 = new JLabel("Study Length :");
		GridBagConstraints gbc_lblStudyLength_1 = new GridBagConstraints();
		gbc_lblStudyLength_1.gridx = 0;
		gbc_lblStudyLength_1.gridy = 1;
		panelDayControls.add(lblStudyLength_1, gbc_lblStudyLength_1);
		
		spinnerStudyLength_1 = new JSpinner();
		spinnerStudyLength_1.setModel(new SpinnerNumberModel(60, 0, 120, 1));
		GridBagConstraints gbc_spinnerStudyLength_1 = new GridBagConstraints();
		gbc_spinnerStudyLength_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerStudyLength_1.gridx = 1;
		gbc_spinnerStudyLength_1.gridy = 1;
		panelDayControls.add(spinnerStudyLength_1, gbc_spinnerStudyLength_1);
		
		lblBreakLength = new JLabel("Break Length :");
		GridBagConstraints gbc_lblBreakLength = new GridBagConstraints();
		gbc_lblBreakLength.gridx = 3;
		gbc_lblBreakLength.gridy = 1;
		panelDayControls.add(lblBreakLength, gbc_lblBreakLength);
		
		spinnerBreakLength = new JSpinner();
		spinnerBreakLength.setModel(new SpinnerNumberModel(0, 0, 60, 1));
		GridBagConstraints gbc_spinnerBreakLength = new GridBagConstraints();
		gbc_spinnerBreakLength.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerBreakLength.gridx = 4;
		gbc_spinnerBreakLength.gridy = 1;
		panelDayControls.add(spinnerBreakLength, gbc_spinnerBreakLength);
        
		//panel to organize the layout of the Topics list and the delete button
		panelTopics = new JPanel();
		getContentPane().add(panelTopics);
		GridBagLayout gbl_panelTopics = new GridBagLayout();
		gbl_panelTopics.columnWeights = new double[] { 0.0 };
		gbl_panelTopics.rowWeights = new double[] { 0.0, 0.0, 0.0 };
		panelTopics.setLayout(gbl_panelTopics);

		topicLabel = new JLabel("Topics:");
		GridBagConstraints gbc_topicLabel = new GridBagConstraints();
		gbc_topicLabel.anchor = GridBagConstraints.WEST;
		gbc_topicLabel.insets = new Insets(0, 0, 0, 5);
		gbc_topicLabel.gridx = 0;
		gbc_topicLabel.gridy = 0;
		panelTopics.add(topicLabel, gbc_topicLabel);
        
		//List to display the topics of the topics list
		topicList = new JList<String>();
		topicList.setVisibleRowCount(6);
		JScrollPane topicListScrollPane = new JScrollPane(topicList);
		GridBagConstraints gbc_topicListScrollPane = new GridBagConstraints();
		gbc_topicListScrollPane.anchor = GridBagConstraints.WEST;
		gbc_topicListScrollPane.insets = new Insets(0, 0, 0, 5);
		gbc_topicListScrollPane.gridx = 0;
		gbc_topicListScrollPane.gridy = 1;
		panelTopics.add(topicListScrollPane, gbc_topicListScrollPane);
        
		//button to delete the selected topic from the topics list and delete it by calling the StudyPlanner.deleteTopic
		//with the topic name of it
		btnDeleteTopic = new JButton("Delete Topic");
		GridBagConstraints gbc_btnDeleteTopic = new GridBagConstraints();
		gbc_btnDeleteTopic.insets = new Insets(5, 0, 0, 0);
		gbc_btnDeleteTopic.gridx = 0;
		gbc_btnDeleteTopic.gridy = 2;
		panelTopics.add(btnDeleteTopic, gbc_btnDeleteTopic);
		btnDeleteTopic.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (topicList.getSelectedIndex() >= 0) {
					planner.deleteTopic(planner.getTopics().get(topicList.getSelectedIndex()).getSubject());
				}
			}
		});
        
		//panel to organize the studyplan list layout
		panelStudyPlan = new JPanel();
		getContentPane().add(panelStudyPlan);
		GridBagLayout gbl_panelStudyPlan = new GridBagLayout();
		gbl_panelStudyPlan.columnWeights = new double[] { 0.0 };
		gbl_panelStudyPlan.rowWeights = new double[] { 0.0, 0.0 };
		panelStudyPlan.setLayout(gbl_panelStudyPlan);

		planLabel = new JLabel("Study Plan:");
		GridBagConstraints gbc_planLabel = new GridBagConstraints();
		gbc_planLabel.anchor = GridBagConstraints.WEST;
		gbc_planLabel.insets = new Insets(0, 0, 0, 5);
		gbc_planLabel.gridx = 0;
		gbc_planLabel.gridy = 0;
		panelStudyPlan.add(planLabel, gbc_planLabel);
		studyPlan = new JList<String>();
		studyPlan.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane studyPlanScrollPane = new JScrollPane(studyPlan);
		GridBagConstraints gbc_studyPlanScrollPane = new GridBagConstraints();
		gbc_studyPlanScrollPane.fill = GridBagConstraints.BOTH;
		gbc_studyPlanScrollPane.gridx = 0;
		gbc_studyPlanScrollPane.gridy = 1;
		panelStudyPlan.add(studyPlanScrollPane, gbc_studyPlanScrollPane);
	}
    
	/*
	 * Method to notify the gui to update
	 */
	@Override
	public void notifyModelHasChanged() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				updateDisplay();
			}
		});
	}
    
	/*
	 * Method to Update the Gui
	 */
	private void updateDisplay() {
		if (planner == null) {
			// nothing to update from, so do nothing
		} else 
		{
			//Delete the old target events of the combox and add the new events in the planner event list
			comboTargetEvent.removeAllItems();
			for (CalendarEventInterface calendarEvent : planner.getCalendarEvents()) {
				comboTargetEvent.addItem(calendarEvent.getName());
			}
			
			//get the topics from the planner topic list and set them to the Gui topic list
			topicList.removeAll();
			List<String> topicData = new ArrayList<String>();
			DefaultListModel<String> model = new DefaultListModel<>();
			for (TopicInterface t : planner.getTopics()) {
				model.addElement(" " + t.getSubject() + " (" + t.getDuration() + ") ");
				//topicData.add(" " + t.getSubject() + " (" + t.getDuration() + ") ");
			}
			//topicList.setListData(topicData.toArray(new String[1]));
			topicList.setModel(model);
			
			List<String> eventData = new ArrayList<String>();
			//if the planner study plan list is empty no work is needed
			if(planner.getStudyPlan().isEmpty())
			{
				studyPlan.setListData(new String[]{});
				if(planner.getTopics().isEmpty()) topicList.setListData(new String[]{});
				resizeJList();
				return;
			}
			//loop through the event list and the study plan list and add them to the Gui Study plan list in sorted order by their start time
			//Level3.2 && Level3.5
			int topicOrEvent = 0;
			for (int i = 0, j = 0; i < planner.getStudyPlan().size() || j < planner.getCalendarEvents().size();) {
				if (i < planner.getStudyPlan().size() && j < planner.getCalendarEvents().size()) {
					if (planner.getStudyPlan().get(i).getStartTime()
							.before(planner.getCalendarEvents().get(j).getStartTime()))
						topicOrEvent = 0;
					else
						topicOrEvent = 1;
				} else {
					if (i < planner.getStudyPlan().size())
						topicOrEvent = 0;
					else if (j < planner.getCalendarEvents().size())
						topicOrEvent = 1;
				}
				if (topicOrEvent == 0) {
					StudyBlockInterface study = planner.getStudyPlan().get(i);
					String dateAndTime = HelperUtility.getFormatedTimeAndDate(study.getStartTime().get(Calendar.MONTH),
							study.getStartTime().get(Calendar.DAY_OF_MONTH),
							study.getStartTime().get(Calendar.HOUR_OF_DAY), study.getStartTime().get(Calendar.MINUTE));

					eventData.add(dateAndTime + " " + study.getTopic() + "  (" + study.getDuration() + ") ");
					++i;
				} else {
					CalendarEventInterface event = planner.getCalendarEvents().get(j);
					String dateAndTime = HelperUtility.getFormatedTimeAndDate(event.getStartTime().get(Calendar.MONTH),
							event.getStartTime().get(Calendar.DAY_OF_MONTH),
							event.getStartTime().get(Calendar.HOUR_OF_DAY), event.getStartTime().get(Calendar.MINUTE));

					eventData.add(dateAndTime + " " + event.getName() + "  (" + event.getDuration() + ") ");
					++j;
				}
			}
			studyPlan.setListData(eventData.toArray(new String[1]));
			resizeJList();
		}

	}
	
	private void resizeJList()
	{
		int state = this.getExtendedState();
		Rectangle rect = this.getBounds();
		StudyPlannerGUI.this.setSize(rect.width+1, rect.height+1);
		if(state==JFrame.MAXIMIZED_BOTH)
		{
			this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		}
		else StudyPlannerGUI.this.setSize(rect.width, rect.height);
	}

	public static void main(String[] args) 
	{
		StudyPlanner planner = new StudyPlanner();
		planner.addTopic("Java file handling", 480);
		planner.addTopic("European agricultural policy 1950-1974", 720);

		StudyPlannerGUI gui = new StudyPlannerGUI(planner);
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gui.setMinimumSize(new Dimension(600, 700));
		gui.addWindowStateListener(new WindowStateListener() {
			public void windowStateChanged(WindowEvent e) {
				if (gui.getExtendedState() == JFrame.NORMAL){
					gui.setSize(600, 700);
				   }
			}
		});
		planner.setGUI(gui);

		gui.setVisible(true);
		gui.updateDisplay();
	}
}
