package eg.edu.alexu.csd.oop.master;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.List;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.io.File;
import java.net.URL;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import eg.edu.alexu.csd.oop.draw.*;

import java.awt.FlowLayout;

import javax.swing.JComboBox;


@SuppressWarnings("serial")
public class View extends JFrame implements Observer{
	private JButton[] colorButton=new JButton[20];
	private String[]  Shapes=new String[20];
	private Color color1 = Color.BLACK;
	private JPanel canvas = new JPanel();
	private Map<String, String> shapesMap = new TreeMap<>();
	private FlowLayout experimentLayout = new FlowLayout();
	private java.util.List<Class<? extends Shape>> list;
	private Integer previous=0,i1;
	private JMenuItem save = new JMenuItem("Save\r\n");
	private JMenuItem load = new JMenuItem("load");
	private JMenuItem addPlugin= new JMenuItem ("Add Plugins");
	private JButton refresh = new JButton("Refresh");
	private JButton update = new JButton("Update");
	private JButton delete = new JButton("Delete");
	private JButton redo1=new JButton();
	private JButton fill=new JButton();
	private JButton undo1=new JButton();
	private JLabel label= new JLabel("Suported Shapes");
	private JComboBox<String> comboBox = new JComboBox<String>();
	private URL resource;	
	@Override
	
	public void update(Observable o,Shape[] shapes) {
		canvas.getGraphics().clearRect(0, 0, 1500, 1000);
    for(int i = 0 ; i < shapes.length ; i++){
		shapes[i].draw(canvas.getGraphics());
	}

	}
	
	public void initialize() {
	getContentPane().setForeground(new Color(0, 0, 0));
		getContentPane().setBackground(new Color(230, 230, 250));
		setBounds(100, 100, 1648, 974);
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		setResizable(false);
		canvas.setBackground(Color.WHITE);
		canvas.setBounds(165, 25, 1455, 879);
		canvas.setVisible(true) ;
		comboBox.setBounds(12, 70, 141, 22);
		label.setBounds(12, 40, 100, 22);
		label.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		getContentPane().add(label);
		getContentPane().add(comboBox);
		getContentPane().add(canvas);
		generateColorsPanel();
        createShapes();
        generateMenuBar();
        generateOptionPanel();
		
		
		fill.setBackground(new Color(230, 230, 250));
		resource = View.class.getClassLoader()
			      .getResource("resources/fill.png");
		ImageIcon picFill = new ImageIcon(resource);
		fill.setIcon(picFill);
		fill.setBounds(15, 410,50, 50);
		getContentPane().add(fill);
		
		JButton colors = new JButton("");
		colors.setBackground(new Color(230, 230, 250));
		resource = View.class.getClassLoader().getResource("resources/colors.png");
		ImageIcon colorImage = new ImageIcon(resource);
	    colors.setIcon(colorImage);
		colors.setBounds(95, 410, 50, 50);
		colors.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				color1=JColorChooser.showDialog(null,"set color", Color.RED);
			}
		});
		getContentPane().add(colors);		
	}
 
	private void generateColorsPanel(){
		JPanel panel_1 = new JPanel();
		panel_1.setForeground(new Color(0, 0, 0));
		panel_1.setBorder(new CompoundBorder(new LineBorder(new Color(30, 144, 255), 2, true), 
	    new TitledBorder(UIManager.getBorder("TitledBorder.border"), "colors", TitledBorder.LEADING, TitledBorder.TOP, null, 
	    new Color(0, 0, 0))));
		panel_1.setBounds(12, 130, 141, 243);
		panel_1.setLayout(experimentLayout);
		panel_1.setComponentOrientation( ComponentOrientation.LEFT_TO_RIGHT);
		for(int i=0;i<18;i++){
			colorButton[i] = new JButton("\n");
		panel_1.add(colorButton[i]);
	}		
		colorButton[0].setBackground(new Color(0, 0, 0));
		colorButton[1].setBackground(new Color(0, 0, 205));
		colorButton[2].setBackground(new Color(0, 128, 0));
		colorButton[3].setBackground(new Color(0, 191, 255));
		colorButton[4].setBackground(new Color(0, 255, 0));
		colorButton[5].setBackground(new Color(72, 61, 139));
		colorButton[6].setBackground(new Color(143, 188, 143));
		colorButton[7].setBackground(new Color(178, 34, 34));
		colorButton[8].setBackground(new Color(186, 85, 211));
		colorButton[9].setBackground(new Color(173, 255, 47));
		colorButton[10].setBackground(new Color(184, 134, 11));
		colorButton[11].setBackground(new Color(192, 192, 192));
		colorButton[12].setBackground(new Color(238, 130, 238));
		colorButton[13].setBackground(new Color(255, 140, 0));
		colorButton[14].setBackground(new Color(255, 200, 80));
		colorButton[15].setBackground(new Color(255, 235, 205));
		colorButton[16].setBackground(new Color(255, 255, 0));
		colorButton[17].setBackground(new Color(255, 255, 255));
		getContentPane().add(panel_1);
	}
	
	public void createShapes() {
		shapesMap.clear();
		comboBox.removeAllItems();
		for( i1=0;i1<previous;i1++){
			 Shapes[i1]= new String();
			}
		for( i1=0;i1<list.size();i1++){
			shapesMap.put(list.get(i1).getSimpleName(), list.get(i1).getName());
			 Shapes[i1]= list.get(i1).getSimpleName();
			 comboBox.addItem(Shapes[i1]);
			}
	}
		
	private void generateMenuBar(){
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 1630, 26);
		getContentPane().add(menuBar);
		JMenu file = new JMenu("File  ");
		JMenu options= new JMenu("Options ");
		file.setFont(new Font("Arial Black", Font.BOLD, 16));
		options.setFont(new Font("Arial Black", Font.BOLD, 16));
		menuBar.add(file);
		menuBar.add(options);
		options.add(addPlugin);
		file.add(save);
		file.add(load);
	}
	
	private void generateOptionPanel(){
		 JPanel panel_2 = new JPanel();
			panel_2.setLayout(null);
			panel_2.setBorder(new CompoundBorder(new LineBorder(new Color(30, 144, 255), 2), 
					new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Options", 
							TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0))));
			panel_2.setBounds(10, 500, 140, 258);
			getContentPane().add(panel_2);
			
			
			 resource = View.class.getClassLoader()
				      .getResource("resources/undo.png");
			ImageIcon picUndo = new ImageIcon(resource);
			undo1.setIcon(picUndo);
			undo1.setBounds(12, 36, 45, 46);
			panel_2.add(undo1);
			
			resource = View.class.getClassLoader()
				      .getResource("resources/redo.png");
			ImageIcon picRedo = new ImageIcon(resource);
			redo1.setIcon(picRedo);
			redo1.setBounds(77, 36, 45, 46);
			panel_2.add(redo1);
			
			
			refresh.setBackground(new Color(175, 238, 238));
			refresh.setFont(new Font("Times New Roman", Font.PLAIN, 16));
			refresh.setBounds(12, 91, 116, 37);
			panel_2.add(refresh);
			
			
			update.setBackground(new Color(175, 238, 238));
			update.setFont(new Font("Times New Roman", Font.PLAIN, 16));
			update.setBounds(12, 141, 116, 37);
			panel_2.add(update);
			
			
			delete.setBackground(new Color(175, 238, 238));
			delete.setFont(new Font("Times New Roman", Font.PLAIN, 16));
			delete.setBounds(12, 193, 116, 37);
			panel_2.add(delete);
			
	}
	
	public Map<String, Double> setProperties(Shape newShape,String title){
		Map<String, Double> shapeProperties=newShape.getProperties();
		Object[] arrProp=new Object[shapeProperties.size()*2];
		JTextField[] m = new JTextField[shapeProperties.size()] ;
		for(int i =  0 ; i < shapeProperties.size()*2; i++){
			 arrProp[i] =new Object();
		}
		for(int i =  0 ; i < shapeProperties.size(); i++){
			 m[i] =new JTextField();
		      }
	   int i=0;
		int j=0;
		for(Map.Entry<String,Double> entry : shapeProperties.entrySet()){
			arrProp[i]=entry.getKey().toString();
			m[j] = new JTextField();
			arrProp[i+1]=m[j++];
			i+=2 ;
		}
		int reply = JOptionPane.showConfirmDialog(null,arrProp  ,title, JOptionPane.YES_NO_OPTION);
		i=0;
		if (reply == JOptionPane.YES_OPTION){
    		for(Map.Entry<String,Double> entry : shapeProperties.entrySet()){
    			Double x = Double.parseDouble(m[i].getText());
    			shapeProperties.put(entry.getKey(),x) ;
    			i++ ;
    		}
    		return shapeProperties;
		}
		if (reply == JOptionPane.NO_OPTION)
		{
			return null;
		    
		}
		return shapeProperties;
		
	}
	
	public void addSaveActionListener(ActionListener saveActionListener){
		save.addActionListener(saveActionListener);
	}
	
	public void addLoadActionListener(ActionListener loadActionListener){
		load.addActionListener(loadActionListener);
	}
	
	public void addUndoActionListener(ActionListener undoActionListener){
		undo1.addActionListener(undoActionListener);
	}
	
	public void addRedoActionListener(ActionListener RedoActionListener){
		redo1.addActionListener(RedoActionListener);
	}
		
	public void addCanvasMouseListener(MouseListener canvasMouseListener){
		canvas.addMouseListener(canvasMouseListener);
	}
	
	public void addRefreshActionListener(ActionListener RefreshActionListener){
		refresh.addActionListener(RefreshActionListener);
	}
	
	public void addUpdateActionListener(ActionListener updateActionListener){
		update.addActionListener(updateActionListener);
	}
	
	public void addDeleteActionListener(ActionListener deleteActionListener){
		delete.addActionListener(deleteActionListener);
	}
	
	public void addFillActionListener(ActionListener fillActionListener){
		fill.addActionListener(fillActionListener);
	}
	
	public void addShapesActionListener(ActionListener shapesActionListener){
		comboBox.addActionListener(shapesActionListener);
	}
	
	public void addColorsActionListener(ActionListener colorsActionListener){
		for(int i=0;i<18;i++){
			colorButton[i].addActionListener(colorsActionListener);
			}
	}
	
	public void setShapes(java.util.List<Class<? extends Shape>> l){
		if(previous!=0)previous=list.size();
		list=l;
		
	}
	
	public void addPluginActionListener(ActionListener plugActionListener){
		addPlugin.addActionListener(plugActionListener);
	}

	public String showFileChooser(){
		 JFileChooser fileChooser=new JFileChooser(new File("c:\\"));
		 int value= fileChooser.showOpenDialog(null);
		 if(value==JFileChooser.APPROVE_OPTION){
			 
		 }
		 return fileChooser.getSelectedFile().toString().replace("\\", "/");
	    
	}
	
	public  LinkedList<String> saveJoptionShow(){
		JTextField t=new JTextField();
		Object[] arr={"Enter file path to save",t};
		Integer reply=JOptionPane.showConfirmDialog(null, arr, "save", JOptionPane.YES_NO_OPTION);
		 LinkedList<String> list=new  LinkedList<String>();
		list.add(reply.toString());
		list.add(t.getText());
		return list;
	}
	
	public void showError(){
		JOptionPane.showMessageDialog(null,"Error","warning",JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void showClassError(){
		JOptionPane.showMessageDialog(null,"Class not found","warning",JOptionPane.INFORMATION_MESSAGE);
	}
	
	public Color getColor1() {
		return color1;
	}
	
	public void setColor1(Color c) {
		color1=c;
	}
	
	public Map<String, String> getShapesMap() {
		return shapesMap;
	}

	public String getshap(ActionEvent arg0) {
	@SuppressWarnings("unchecked")
	JComboBox<String> c=(JComboBox<String>) arg0.getSource();
	return (String) c.getSelectedItem();
   }

	public String showFileSaver() {
	   JFileChooser fileChooser=new JFileChooser(new File("c:\\"));
	   fileChooser.setDialogTitle("Save File");
		 int value=fileChooser.showSaveDialog(null);
		 if(value==JFileChooser.APPROVE_OPTION){		 
		 }
		 return fileChooser.getSelectedFile().toString().replace("\\", "/");
	
   }

		}