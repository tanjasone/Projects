import javax.swing.*;
import javax.swing.border.*;
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

class Encounter
{
	String date;
	String desc;
	
	Encounter(String date, String desc)
	{
		this.date = date;
		this.desc = desc;
	}
}

class Patient implements Comparable<Patient>
{
//	0 lastName
//	1 firstName
//	2 accountNum
//	3 sex
//	4 DOB
//	5 race
//	6 address
//	7 city
//	8 state
//	9 zip
//	10 homePhone
//	11 workPhone
//	12 cellPhone
//	13 emgcPhone
//	14 insName
//	15 insPolicyNum
//	16 insGroupID
	
	String[] data;
	int encNum;
	Encounter[] encsList;
	
	Patient(String str, String encStr)
	{
		String[] encs;
		String[] data = str.split(";");
		
		this.data = data;
		
		if(!encStr.equals("0")) {
			encs = encStr.split(";");
			encNum = (encs.length - 1) / 2;
			encsList = new Encounter[encNum];
			int j=0;
			for (int i=1; j<encNum;i+=2)
				encsList[j++] = new Encounter(encs[i], encs[i+1]);
		}
		else
		{
			encNum = 0;
		}
	}

	@Override
	public int compareTo(Patient p) {
		if(!data[0].equals(p.data[0]))
			return data[0].compareTo(p.data[0]);
		else
			return data[1].compareTo(p.data[1]);
	}
	
	public String toString()
	{
		return data[0]+", "+data[1];
	}
	
	public String[] print()
	{
		String[] str = new String[2];
		
		str[0] = data[0];
		for(int i=1; i<data.length; i++)
		{
			str[0]+= ";"+data[i];
		}
		
		str[1] = "" + encNum;
		if(encNum > 0)
		{
			for(int i=0; i<encNum; i++)
			{
				str[1] += ";" + encsList[i].date + ";" + encsList[i].desc;
			}
		}
		
		return str;
	}
}

public class MedicalRecords
{
	public static void main(String[] args) {

		JFrame rootFrame = new JFrame("Medical Records - Eugene P. Tan M.D.");
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		JSplitPane searchPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		JPanel searchBox = new JPanel();
		JPanel searchResults = new JPanel();
		JScrollPane scrollPane = new JScrollPane();
		TypedButton searchButton = new TypedButton("Search", TypedButton.SEARCH_BUTTON);
		JTextField firstNameField = new JTextField();
		JTextField lastNameField = new JTextField();
		JPanel outPanel = new JPanel();
		JPanel dataPanel = new JPanel();
		JPanel optionsPanel = new JPanel();
		TypedButton addButton = new TypedButton("Add Patient", TypedButton.ADD_PATIENT);
		ProgramControl progControl = new ProgramControl();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		rootFrame.setSize(screenSize);
		rootFrame.setVisible(true);
		rootFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		searchButton.addActionListener(progControl);

		firstNameField.setColumns(20);
		firstNameField.addKeyListener(progControl);
		lastNameField.setColumns(20);
		lastNameField.addKeyListener(progControl);
		progControl.setTextSource(firstNameField, lastNameField);


		searchBox.add(new JLabel("First Name"));
		searchBox.add(firstNameField);
		searchBox.add(new JLabel("Last Name"));
		searchBox.add(lastNameField);
		searchBox.add(searchButton);
		searchBox.setMinimumSize(new Dimension(300, 100));
		searchBox.setPreferredSize(new Dimension(300, 100));

		searchResults.setBackground(new Color(210, 210, 210, 0));
		searchResults.addMouseListener(progControl);
		searchResults.setMaximumSize(new Dimension(300, screenSize.height));
		searchResults.setPreferredSize(new Dimension(300, screenSize.height-140));
		scrollPane.setViewportView(searchResults);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		progControl.setSearchPanel(searchResults, scrollPane);
		searchPanel.setTopComponent(searchBox);
		searchPanel.setBottomComponent(scrollPane);

		dataPanel.setBackground(new Color(240,240,240));
		dataPanel.setPreferredSize(new Dimension(1100, 900));
		dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.X_AXIS));
		progControl.setDataPanel(dataPanel);

		addButton.addActionListener(progControl);

		optionsPanel.setBackground(new Color(190,190,190));
		optionsPanel.setPreferredSize(new Dimension(1100, 30));
		optionsPanel.add(addButton);

		outPanel.setLayout(new BoxLayout(outPanel, BoxLayout.Y_AXIS));
		outPanel.setBackground(new Color(200,200,200));
		outPanel.add(dataPanel);
		outPanel.add(optionsPanel);

		splitPane.setLeftComponent(searchPanel);
		splitPane.setRightComponent(outPanel);
		splitPane.setDividerLocation(300);

		rootFrame.add(splitPane);
		rootFrame.pack();
		rootFrame.setExtendedState(rootFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
	}
}

class SearchItem extends JPanel
{
	private static final long serialVersionUID = 1L;
	Patient patient;
	
	SearchItem(Patient p)
	{
		patient = p;
		Color c = new Color(220,220,220);
		Label name = new Label(p.toString());
		name.setBackground(c);
		Label DOB = new Label("DOB: "+p.data[4]);
		DOB.setBackground(c);
		Label lastEnc;
		if(p.encNum != 0)
			lastEnc = new Label("Last Encounter: "+p.encsList[p.encNum-1].date);
		else
			lastEnc = new Label("Last Encounter: None");
		lastEnc.setBackground(c);
		this.add(name);
		this.add(DOB);
		this.add(lastEnc);
	}
}

class TypedButton extends JButton
{
	private static final long serialVersionUID = 1L;
	int type;
	static final int SEARCH_BUTTON = 0;
	static final int SAVE_BUTTON = 1;
	static final int ADD_PATIENT = 2;
	static final int ADD_ENC = 3;
	static final int DEL_PATIENT = 4;
	static final int SAVE_NEW = 5;
	
	TypedButton(String name, int type)
	{
		super(name);
		this.type = type;
	}
}

class DataField extends JPanel
{
	private static final long serialVersionUID = 1L;
	Label name;
	JTextField text;
	
	DataField(String name, JTextField tf)
	{
		this.name = new Label(name+":");
		this.name.setAlignment(Label.LEFT);
		text = tf;
		text.setAlignmentX(LEFT_ALIGNMENT);
		this.add(this.name);
		this.add(text);
		this.setAlignmentX(LEFT_ALIGNMENT);
	}
}

class ProgramControl implements ActionListener, KeyListener, MouseListener
{
	JTextField firstNameField;
	JTextField lastNameField;
	ArrayList<Patient> patientList;
	ArrayList<JTextField> dataFieldsList;
	JPanel resPanel;
	JScrollPane scrollPane;
	JPanel dataPanel;
	SearchItem selectedResult;
	
	private final Border DEFAULT_BORDER = new LineBorder(new Color(150,150,150),4);
	private final Color DEFAULT_COLOR = new Color(200,200,200);
	
	public void setTextSource(JTextField first, JTextField last){
		firstNameField = first;
		lastNameField = last;
	}
	
	public void setSearchPanel(JPanel p, JScrollPane sp){
		resPanel = p;
		scrollPane = sp;
	}
	
	public void setDataPanel(JPanel p){
		dataPanel = p;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		TypedButton tb = (TypedButton) e.getSource();
		if(tb.type == TypedButton.SEARCH_BUTTON)
			performSearch();
		if(tb.type == TypedButton.SAVE_BUTTON)
			savePatientInfo(selectedResult.patient);
		if(tb.type == TypedButton.SAVE_NEW)
			savePatientInfo(null);
		if(tb.type == TypedButton.ADD_PATIENT)
			addPatientInfo();
		if(tb.type == TypedButton.DEL_PATIENT)
			deletePatient(selectedResult.patient);
	}

	public void keyTyped(KeyEvent e) {
		if(e.getKeyChar() == KeyEvent.VK_ENTER)
			performSearch();
	}

	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {}
	
	@Override
	public void mouseClicked(MouseEvent e) 
	{
		Point p = resPanel.getMousePosition();
		Component item = resPanel.getComponentAt(p);
		if(!(item instanceof SearchItem || item.getParent() instanceof SearchItem)) return;
		
		if(selectedResult != null)
		{
			selectedResult.setBorder(DEFAULT_BORDER);
			selectedResult.setBackground(DEFAULT_COLOR);
		}
		if(item.getParent() instanceof SearchItem)
			selectedResult = (SearchItem)item.getParent();
		else
			selectedResult = (SearchItem)item;
		selectedResult.setBorder(new LineBorder(new Color(170, 190, 230), 5));
		selectedResult.setBackground(new Color(180,180,180));
		System.out.println("Selected "+selectedResult.patient.toString());
		displayPatientInfo(selectedResult.patient);
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
	
	private void performSearch()
	{
		String[] query = new String[2];
		String firstName = firstNameField.getText().toUpperCase();
		String lastName = lastNameField.getText().toUpperCase();
		if(lastName.equals("")) return;

		System.out.println("Searching for \""+firstName + " " + lastName +"\"...");
		query[0] = lastName;
		query[1] = firstName;
		
		String data;
		String encs;
		patientList = new ArrayList<>();
		
		try 
		{
			Scanner sc = new Scanner(new File(".P"+(int)Character.toUpperCase(query[0].charAt(0))));
			while(sc.hasNext())
			{
				data = sc.nextLine();
				encs = sc.nextLine();
				if(query[0].regionMatches(true, 0, data, 0, query[0].length()))
				{
					Patient p = new Patient(data, encs);
					if(!p.data[1].regionMatches(true, 0, query[1], 0, query[1].length()))
						continue;
					patientList.add(p);
				}
			}
			sc.close();
			Collections.sort(patientList);
			displaySearchItems(null);
		} 
		catch(IOException e)
		{
			e.printStackTrace();
			return;
		}
		System.gc();
	}
	
	private void displaySearchItems(ArrayList<Patient> list)
	{
		Label resLabel = new Label("", Label.CENTER);
		resLabel.setPreferredSize(new Dimension(300, 40));
		resLabel.setMaximumSize(new Dimension(300, 40));
		
		if(list == null)	//display class variable list
		{
			resPanel.removeAll();
			System.out.println("Printing search results...");
			
			resPanel.setPreferredSize(new Dimension(300, patientList.size()*65));
			resPanel.setLayout(new BoxLayout(resPanel, BoxLayout.Y_AXIS));
			resLabel.setText(patientList.size()+" results found");
			
			resPanel.add(resLabel);
			for (int i=0; i<patientList.size(); i++)
			{
				Patient p = patientList.get(i);
				SearchItem si = new SearchItem(p);
				si.setMaximumSize(new Dimension(300, 65));
				si.setPreferredSize(new Dimension(300, 65));
				si.setBackground(DEFAULT_COLOR);
				si.setBorder(DEFAULT_BORDER);
				resPanel.add(si);
			}
		}
		else	//display given list
		{
			resPanel.removeAll();
			System.out.println("Printing search results...");
			
			resPanel.setPreferredSize(new Dimension(300, list.size()*65));
			resPanel.setLayout(new BoxLayout(resPanel, BoxLayout.Y_AXIS));
			resPanel.add(resLabel);
			for (int i=0; i<list.size(); i++)
			{
				Patient p = list.get(i);
				SearchItem si = new SearchItem(p);
				si.setMaximumSize(new Dimension(300, 65));
				si.setPreferredSize(new Dimension(300, 65));
				si.setBackground(DEFAULT_COLOR);
				si.setBorder(DEFAULT_BORDER);
				resPanel.add(si);
			}
			
		}
		resPanel.repaint();
		resPanel.revalidate();
		scrollPane.repaint();
		scrollPane.revalidate();
	}
	
	private void savePatientInfo(Patient patient)
	{
		System.out.println("Saving info...");
		
		ArrayList<Patient> saveList = new ArrayList<>();
		String data;
		String encs = "";
		Scanner sc;
		int i;
		
		
		data = dataFieldsList.get(0).getText();
		for(i=1; i<17; i++)
		{
			if(i == 16 && dataFieldsList.get(i).getText().equals(""))
			{
				data += ";0";
				break;
			}
			data += ";"+dataFieldsList.get(i).getText();
		}
		for(i=17; i<dataFieldsList.size(); i++)
		{
			if(dataFieldsList.get(i).getText().equals(""))
				break;
			encs += ";"+dataFieldsList.get(i).getText();
		}
		encs = ((i-17)/2) + encs;
		Patient savePatient = new Patient(data, encs);
		
		try {
			if(patient != null)		// save change to existing patient info
			{
				File fp = new File(".P"+(int)Character.toUpperCase(savePatient.data[0].charAt(0)));
				Patient p1;
				sc = new Scanner(fp);
				
				while(sc.hasNext())
				{
					data = sc.nextLine();
					encs = sc.nextLine();
					p1 = new Patient(data, encs);
					saveList.add(p1);
				}
				sc.close();
				
				for(i=0; i < saveList.size(); i++)
					if(saveList.get(i).compareTo(savePatient)==0)
						break;
				saveList.set(i, savePatient);
				
				for(i=0; i < patientList.size(); i++)
					if(patientList.get(i).compareTo(savePatient)==0)
						break;
				patientList.set(i, savePatient);
				
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(fp, false)));
				for(Patient p3 : saveList)
				{
					String[] str = p3.print();
					pw.println(str[0]);
					pw.println(str[1]);
				}
				pw.close();
				
				displaySearchItems(null);
			}
			else	// create new patient(no existing patient info being modified)
			{
				File fp = new File(".P"+(int)Character.toUpperCase(savePatient.data[0].charAt(0)));
				boolean added = false;
				sc = new Scanner(fp);
				while(sc.hasNext())
				{
					data = sc.nextLine();
					encs = sc.nextLine();
					Patient p1 = new Patient(data, encs);
					saveList.add(p1);
					if(!added && savePatient.compareTo(p1) > 0)
					{
						saveList.add(savePatient);
						added = true;
					}
				}
				if(saveList.size() == 0) saveList.add(savePatient);
				sc.close();
				
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(fp, false)));
				for(Patient p3 : saveList)
				{
					String[] str = p3.print();
					pw.println(str[0]);
					pw.println(str[1]);
				}
				pw.close();
				ArrayList<Patient> list = new ArrayList<>();
				list.add(savePatient);
				displaySearchItems(list);
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void deletePatient(Patient p) {
		int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this patient information?","Warning",
				JOptionPane.YES_NO_OPTION);
		if(choice == JOptionPane.NO_OPTION) 
		{
			System.out.println("Deletion canceled...");
			return;
		}
		
		System.out.println("Deleting patient "+p+"...");
		
		ArrayList<Patient> deleteList = new ArrayList<>();
		String data;
		String encs;
		Scanner sc;
		
		try {
			File fp = new File(".P"+(int)Character.toUpperCase(p.data[0].charAt(0)));
			
			sc = new Scanner(fp);
			while(sc.hasNext())
			{
				data = sc.nextLine();
				encs = sc.nextLine();
				Patient p1 = new Patient(data, encs);
				deleteList.add(p1);
			}
			sc.close();
			
			for(Iterator<Patient> it = deleteList.iterator(); it.hasNext();)
			{
				if(p.data[0].equals(it.next().data[0]))
				{
					it.remove();
					break;
				}
			}
			int i;
			for(i=0; i < patientList.size(); i++)
				if(patientList.get(i).compareTo(p)==0)
					break;
			patientList.remove(i);
			
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(fp, false)));
			for(Patient p3 : deleteList)
			{
				String[] str = p3.print();
				pw.println(str[0]);
				pw.println(str[1]);
			}
			pw.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		displaySearchItems(null);
		dataPanel.removeAll();
		dataPanel.repaint();
		dataPanel.revalidate();
	}
	
	private void displayPatientInfo(Patient p)
	{
//		0 lastName
//		1 firstName
//		2 accountNum
//		3 sex
//		4 DOB
//		5 race
//		6 address
//		7 city
//		8 state
//		9 zip
//		10 homePhone
//		11 workPhone
//		12 cellPhone
//		13 emgcPhone
//		14 insName
//		15 insPolicyNum
//		16 insGroupID
		
		System.out.println("Displaying Patient data for "+p);
		dataPanel.removeAll();
		
		dataFieldsList = new ArrayList<>();
		TypedButton saveButton = new TypedButton("Save", TypedButton.SAVE_BUTTON);
		TypedButton delButton = new TypedButton("Delete",TypedButton.DEL_PATIENT);
		DataField dataField;
		JPanel dataTitle = new JPanel();
		JPanel mainDataPanel = new JPanel();
		JPanel encsPanel = new JPanel();
		JScrollPane dataScroll = new JScrollPane(mainDataPanel);
		JScrollPane encsScroll = new JScrollPane(encsPanel);
		Label titleLabel = new Label("Patient Demographics", Label.CENTER);
		titleLabel.setFont(new Font(null, Font.BOLD, 16));
		
		dataTitle.add(titleLabel);
		dataTitle.setBackground(new Color(190,190,190));
		mainDataPanel.add(dataTitle);
		
		mainDataPanel.setPreferredSize(new Dimension(dataPanel.getWidth()/2, dataPanel.getHeight()));
		mainDataPanel.setMinimumSize(new Dimension(dataPanel.getWidth()/2, dataPanel.getHeight()));
		mainDataPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		encsPanel.setPreferredSize(new Dimension(dataPanel.getWidth()/2, p.encsList.length * 47));
		encsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		dataField = new DataField("Last Name", new JTextField(p.data[0], 15));
		mainDataPanel.add(dataField);
		dataFieldsList.add(dataField.text);
		
		dataField = new DataField("First Name", new JTextField(p.data[1], 15));
		mainDataPanel.add(dataField);
		dataFieldsList.add(dataField.text);
		
		dataField = new DataField("Account Number", new JTextField(p.data[2], 7));
		mainDataPanel.add(dataField);
		dataFieldsList.add(dataField.text);
		
		dataField = new DataField("Sex", new JTextField(p.data[3], 2));
		mainDataPanel.add(dataField);
		dataFieldsList.add(dataField.text);
		
		dataField = new DataField("DOB", new JTextField(p.data[4], 8));
		mainDataPanel.add(dataField);
		dataFieldsList.add(dataField.text);
		
		dataField = new DataField("Race", new JTextField(p.data[5], 8));
		mainDataPanel.add(dataField);
		dataFieldsList.add(dataField.text);
		
		dataField = new DataField("Address", new JTextField(p.data[6], 20));
		mainDataPanel.add(dataField);
		dataFieldsList.add(dataField.text);
		
		dataField = new DataField("City", new JTextField(p.data[7], 20));
		mainDataPanel.add(dataField);
		dataFieldsList.add(dataField.text);

		dataField = new DataField("State",new JTextField(p.data[8], 3));
		mainDataPanel.add(dataField);
		dataFieldsList.add(dataField.text);
		
		dataField = new DataField("ZIP", new JTextField(p.data[9], 8));
		mainDataPanel.add(dataField);
		dataFieldsList.add(dataField.text);
		
		dataField = new DataField("Home Phone", new JTextField(p.data[10], 9));
		mainDataPanel.add(dataField);
		dataFieldsList.add(dataField.text);
		
		dataField = new DataField("Cell Phone", new JTextField(p.data[11], 9));
		mainDataPanel.add(dataField);
		dataFieldsList.add(dataField.text);
		
		dataField = new DataField("Work Phone", new JTextField(p.data[12], 9));
		mainDataPanel.add(dataField);
		dataFieldsList.add(dataField.text);
		
		dataField = new DataField("Emergency Phone", new JTextField(p.data[13], 9));
		mainDataPanel.add(dataField);
		dataFieldsList.add(dataField.text);
		
		dataField = new DataField("Insurance", new JTextField(p.data[14], 20));
		mainDataPanel.add(dataField);
		dataFieldsList.add(dataField.text);
		
		dataField = new DataField("Insurance Policy Number", new JTextField(p.data[15], 20));
		mainDataPanel.add(dataField);
		dataFieldsList.add(dataField.text);
		
		dataField = new DataField("Insurance Group ID", new JTextField(p.data[16], 10));
		if(dataField.text.getText().equals("0"))
			dataField.text.setText("");
		mainDataPanel.add(dataField);
		dataFieldsList.add(dataField.text);
		
		saveButton.addActionListener(this);
		delButton.addActionListener(this);
		mainDataPanel.add(saveButton);
		mainDataPanel.add(delButton);
		
		encsScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		encsScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		encsScroll.getVerticalScrollBar().setUnitIncrement(16);
		encsScroll.setBorder(new LineBorder(new Color(0,0,0), 3));
		
		dataScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		dataScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		Label encsLabel = new Label("Encounters", Label.CENTER);
		encsLabel.setFont(new Font(null, Font.BOLD, 14));
		encsPanel.add(encsLabel);
		
		JButton encButton = new JButton("Add Encounter");
		encButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addEncounter(encsPanel);
			}
		});
		encsPanel.add(encButton);
		
		for (int i=0; i<p.encNum; i++)
		{
			dataField = new DataField("Date", new JTextField(p.encsList[i].date, 13));
			dataField.add(new Label("Description"));
			dataField.add(new JTextField(p.encsList[i].desc, 18));
			dataField.setBorder(new LineBorder(new Color(180,180,180), 4));
			encsPanel.add(dataField);
			for(Component c : dataField.getComponents())
				if(c instanceof JTextField)
					dataFieldsList.add((JTextField)c);
		}
		
		dataPanel.add(dataScroll);
		dataPanel.add(encsScroll);
		
		dataPanel.repaint();
		dataPanel.revalidate();
	}
	
	private void addEncounter(JPanel panel)
	{
		System.out.println("Adding Encounter slot...");
		JPanel newEnc = new JPanel();
		
		newEnc.add(new Label("Date"));
		newEnc.add(new JTextField(13));
		newEnc.add(new Label("Description"));
		newEnc.add(new JTextField(18));
		newEnc.setBorder(new LineBorder(new Color(180,180,180), 4));
		panel.add(newEnc);
		for(Component c : newEnc.getComponents())
			if(c instanceof JTextField)
				dataFieldsList.add((JTextField)c);

		panel.setPreferredSize(new Dimension(panel.getWidth(), panel.getHeight()+47));
		
		panel.repaint();
		panel.revalidate();
	}
	
	private void addPatientInfo()
	{
		dataPanel.removeAll();
		
		dataFieldsList = new ArrayList<>();
		TypedButton saveButton = new TypedButton("Save", TypedButton.SAVE_NEW);
		DataField dataField;
		JPanel dataTitle = new JPanel();
		JPanel mainDataPanel = new JPanel();
		JPanel encsPanel = new JPanel();
		JScrollPane dataScroll = new JScrollPane(mainDataPanel);
		JScrollPane encsScroll = new JScrollPane(encsPanel);
		Label titleLabel = new Label("New Patient Demographics", Label.CENTER);
		titleLabel.setFont(new Font(null, Font.BOLD, 16));
		
		dataTitle.add(titleLabel);
		dataTitle.setBackground(new Color(190,190,190));
		mainDataPanel.add(dataTitle);
		
		mainDataPanel.setPreferredSize(new Dimension(dataPanel.getWidth()/2, dataPanel.getHeight()));
		mainDataPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		encsPanel.setPreferredSize(new Dimension(dataPanel.getWidth()/2, dataPanel.getHeight()-10));
		encsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		dataField = new DataField("Last Name", new JTextField(15));
		mainDataPanel.add(dataField);
		dataFieldsList.add(dataField.text);
		
		dataField = new DataField("First Name", new JTextField(15));
		mainDataPanel.add(dataField);
		dataFieldsList.add(dataField.text);
		
		dataField = new DataField("Account Number", new JTextField(7));
		mainDataPanel.add(dataField);
		dataFieldsList.add(dataField.text);
		
		dataField = new DataField("Sex", new JTextField(2));
		mainDataPanel.add(dataField);
		dataFieldsList.add(dataField.text);
		
		dataField = new DataField("DOB", new JTextField( 8));
		mainDataPanel.add(dataField);
		dataFieldsList.add(dataField.text);
		
		dataField = new DataField("Race", new JTextField(8));
		mainDataPanel.add(dataField);
		dataFieldsList.add(dataField.text);
		
		dataField = new DataField("Address", new JTextField(20));
		mainDataPanel.add(dataField);
		dataFieldsList.add(dataField.text);
		
		dataField = new DataField("City", new JTextField(20));
		mainDataPanel.add(dataField);
		dataFieldsList.add(dataField.text);

		dataField = new DataField("State",new JTextField(3));
		mainDataPanel.add(dataField);
		dataFieldsList.add(dataField.text);
		
		dataField = new DataField("ZIP", new JTextField(8));
		mainDataPanel.add(dataField);
		dataFieldsList.add(dataField.text);
		
		dataField = new DataField("Home Phone", new JTextField(9));
		mainDataPanel.add(dataField);
		dataFieldsList.add(dataField.text);
		
		dataField = new DataField("Cell Phone", new JTextField(9));
		mainDataPanel.add(dataField);
		dataFieldsList.add(dataField.text);
		
		dataField = new DataField("Work Phone", new JTextField(9));
		mainDataPanel.add(dataField);
		dataFieldsList.add(dataField.text);
		
		dataField = new DataField("Emergency Phone", new JTextField(9));
		mainDataPanel.add(dataField);
		dataFieldsList.add(dataField.text);
		
		dataField = new DataField("Insurance", new JTextField(20));
		mainDataPanel.add(dataField);
		dataFieldsList.add(dataField.text);
		
		dataField = new DataField("Insurance Policy Number", new JTextField(20));
		mainDataPanel.add(dataField);
		dataFieldsList.add(dataField.text);
		
		dataField = new DataField("Insurance Group ID", new JTextField(10));
		mainDataPanel.add(dataField);
		dataFieldsList.add(dataField.text);
		
		saveButton.addActionListener(this);
		mainDataPanel.add(saveButton);
		
		encsScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		encsScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		encsScroll.getVerticalScrollBar().setUnitIncrement(16);
		encsScroll.setBorder(new LineBorder(new Color(0,0,0), 3));
		
		dataScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		dataScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		Label encsLabel = new Label("Encounters", Label.CENTER);
		encsLabel.setFont(new Font(null, Font.BOLD, 14));
		encsPanel.add(encsLabel);
		
		JButton encButton = new JButton("Add Encounter");
		encButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addEncounter(encsPanel);
			}
		});
		encsPanel.add(encButton);
		
		dataField = new DataField("Date", new JTextField(13));
		dataField.add(new Label("Description"));
		dataField.add(new JTextField(18));
		dataField.setBorder(new LineBorder(new Color(180,180,180), 4));
		encsPanel.add(dataField);
		
		for(Component c : dataField.getComponents())
			if(c instanceof JTextField)
				dataFieldsList.add((JTextField)c);
		
		dataPanel.add(dataScroll);
		dataPanel.add(encsScroll);
		
		dataPanel.repaint();
		dataPanel.revalidate();
	}
}
