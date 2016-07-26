package eg.edu.alexu.csd.oop.master;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import eg.edu.alexu.csd.oop.draw.ClassFinder;
import eg.edu.alexu.csd.oop.draw.DrawingEngine;
import eg.edu.alexu.csd.oop.draw.Observable;
import eg.edu.alexu.csd.oop.draw.Observer;
import eg.edu.alexu.csd.oop.draw.Shape;
import eg.edu.alexu.csd.oop.draw.ShapeFactory;

public class Control implements DrawingEngine, Observable{
	private View view =null;
	private Model model = new Model() ;
	private String action="no";
	private String shape = null;
	private Map<String, String> shapesMap = new TreeMap<>();
	private boolean selected=false;
	private int  nearShape = -1;
	private List<Class<? extends Shape>> list = new  LinkedList<Class<? extends Shape>>() ;
	private ArrayList<Observer> views=new ArrayList<Observer>();
	
	public Control(){
		try{
	
			ClassFinder loader = new ClassFinder() ;
			
			Set<Class<? extends Shape>>set = loader.getClasses() ; 
			for(java.util.Iterator<Class<? extends Shape>> it = set.iterator() ; ((java.util.Iterator<Class<? extends Shape>>) it).hasNext() ;){
				list.add(it.next()) ;
			}
		}
		catch(Exception e){}
		
	}
	
	public Control(View frame, Model model2) {
		view=frame;
		model=model2;
		addObserver(view);
	}
	public void notifyObserver() {
		for(Observer view : views){
			view.update(this,model.getShapes());
		}
		
	}


	@Override
	public void addObserver(Observer view) {
		views.add(view);
		
	}

	@Override
	public void removeObserver(Observer view) {
		views.remove(view);
		
	}
	public void addPlugins(String path){
		try{

			ClassFinder loader = new ClassFinder() ;
			Set<Class<? extends Shape>>set = loader.getClasses(path) ; 
			for(java.util.Iterator<Class<? extends Shape>> it = set.iterator() ; ((java.util.Iterator<Class<? extends Shape>>) it).hasNext() ;){
				list.add(it.next()) ;
			}
			}
			catch(Exception e){}
	}
	public void refresh(Graphics canvas) {
		try{
			Shape []arr = model.getShapes() ;
			for(int i = 0 ; i < arr.length ; i++){
				arr[i].draw(canvas);
			}
		}
		catch(Exception e){
			return ;
		}
	}

	@Override
	public void addShape(Shape shape) {
		model.addShape(shape) ;
	}

	@Override
	public void removeShape(Shape shape) {
		try{
			model.removeShape(shape) ;
		}
		catch(Exception e){
			throw new RuntimeException() ;
		}
		
	}

	@Override
	public void updateShape(Shape oldShape, Shape newShape) {
		try{
			model.updateShape(oldShape,newShape) ;
		}
		catch(Exception e){
			throw new RuntimeException() ;
		}
		
	}

	@Override
	public Shape[] getShapes() {
		try{
			Shape []arr = model.getShapes() ;
			return arr ;
		}
		catch(Exception e){
			return null ;
		}
	}

	@Override
	public List<Class<? extends Shape>> getSupportedShapes() {
		return list ;
	}

	@Override
	public void undo() {
		try{
			model.undo() ;
		}
		catch(Exception e){
			throw new RuntimeException() ;
		}
		
	}

	@Override
	public void redo() {
		try{
			model.redo();
		}
		catch(Exception e){
			throw new RuntimeException() ;
		}
		
	}

	@Override
	public void save(String path) {
		String extention = (path.substring(path.lastIndexOf(".") + 1, path.length()));
		try{
			if(extention.equalsIgnoreCase("xml")){
				model.saveXML(path);
			}
			else if(extention.equalsIgnoreCase("json")){
				model.saveJSON(path);
			}
			else throw new FileNotFoundException() ;
		}
		catch(FileNotFoundException e){
			throw new RuntimeException() ;
		}
	}

	@Override
	public void load(String path) {
		String extention = (path.substring(path.lastIndexOf(".") + 1, path.length()));
		try{
			if(extention.equalsIgnoreCase("xml")){
				model.loadXML(path);
			}
			else if(extention.equalsIgnoreCase("json")){
				model.loadJSON(path);
			}
			else throw new FileNotFoundException() ;
		}
		catch(Exception e){
			throw new RuntimeException() ;
		}
		
	}
	public void initialize(){
		view.setShapes(list);
		view.initialize();
		shapesMap=view.getShapesMap();
		view.addSaveActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					try{
					save(view.showFileSaver());
					notifyObserver();
						}
					catch (Exception e1){
						view.showError();
					}
				}
		});
		view.addLoadActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					try{
					load(view.showFileChooser());
				notifyObserver();
						}
					catch (Exception e1){
						view.showError();
					}
				}
			
		});
	
		view.addUndoActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try{
				undo();
				notifyObserver();
				action="no";
				shape=null;}
				catch(Exception e){
					
				}
			}
		});
		view.addRedoActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try{
				redo();
				notifyObserver();
				action="no";
				shape=null;}
					catch(Exception e){}
			}
		});
		view.addRefreshActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				notifyObserver();
			}
		});
		view.addDeleteActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				action="delete";
			}
		});
		view.addUpdateActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				action="update";
				shape=null;
			}
		});
		view.addColorsActionListener(new ActionListener() {
					 public void actionPerformed(ActionEvent arg0) {	
						 view.setColor1(((JButton)arg0.getSource()).getBackground());
						 System.out.println(view.getColor1());
						 shape=null;
						
						}}	
		);
		view.addShapesActionListener(new ActionListener() {
				 public void actionPerformed(ActionEvent arg0) {
					 shape=view.getshap(arg0);
					}}	
	);
		view.addFillActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				action="fill";
			}
		});
		view.addPluginActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
				addPlugins(view.showFileChooser());		
				view.setShapes(list);
				view.createShapes();
				shape=null;
				}
				catch(Exception ex){
				view.showError();
				}
				
				}
		});
		view.addCanvasMouseListener(new MouseListener() {    	
	    	Point position = new Point();
	    	 public void clickCell(MouseEvent me) 
	    	    {
	    	        if(SwingUtilities.isLeftMouseButton(me)){
	    	        	position.x=me.getX();
					    position.y=me.getY();
					    if(shape!=null){
					    	try{
					    		Shape newShape=ShapeFactory.create(shapesMap.get(shape));
					    		newShape.setColor(view.getColor1());
					    		newShape.setFillColor(new Color(250,250,250,0));
					    		String title = "Set Properties";
					    		Map<String, Double> shapeProp=view.setProperties(newShape, title);
					    		
					    		if (shapeProp !=null){
						    		newShape.setProperties(shapeProp);
						    		newShape.setPosition(position);
						    		position = new Point();
						    		addShape(newShape) ;
						    		notifyObserver();
					    		}
					    		else
					    		{
					    		    shape=null;
					    		    position = new Point();
					    		   notifyObserver();
					    		    
					    		}
					    		nearShape=-1;
					    		}
					    	catch(Exception e){
								view.showClassError();
					    	}
					    }
					    else{
					    	
					    	if(action.equals("delete")&&nearShape==-1 ){
					    		int nearShape=model.getNearest(position);
					    		Shape []shapes = getShapes();
					    		if(shapes.length!=0)
					    		removeShape(shapes[nearShape]);
					    		notifyObserver();
					    		 action = "no";
					    	}
					    	else if(action.equals("fill")&&nearShape==-1){
					    		int nearShape=model.getNearest(position);
					    		Shape []shapes = getShapes();
					    		if(shapes.length!=0)
					    		shapes[nearShape].setFillColor(view.getColor1());
					    		notifyObserver();
					    		 action = "no";
					    	}
					    	
					    	else if(action.equals("update")){
					    		try {
					    			Shape []shapes = getShapes();
					    		if(selected&&shapes.length!=0){
					    			Shape new2=(Shape) shapes[nearShape].clone();
					    			new2.setPosition(position);	
					    			String title = "update Properties";
									Map<String, Double> shapeProp=view.setProperties(new2, title);								
									if (shapeProp != null){						    		
							    		new2.setProperties(shapeProp);
							    		updateShape(shapes[nearShape], new2);
							    		notifyObserver();	
							    		position = new Point();
						    		}
									else
						    		{
						    		    shape=null;
						    		    position = new Point();
						    		   notifyObserver();
						    		    
						    		}
						    		
						    		selected=false;
						    		nearShape=-1;
						    		 action = "no";
									
					    		}
					    		else if(!selected&&shapes.length!=0){
					    			selected=true;
					    			 nearShape=model.getNearest(position);
					    			 System.out.println(nearShape);
					    			position = new Point();
					   
					    		}
								} catch (CloneNotSupportedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
					    		
					    	}
					    
					    }
					    shape = null ;
					   
	    	          }
	    	    }
			@Override
			public void mouseClicked(MouseEvent e) {
					clickCell(e);
			}
			@Override
			public void mouseEntered(MouseEvent e) {		
			}
			@Override
			public void mouseExited(MouseEvent e) {
			}
			@Override
			public void mousePressed(MouseEvent e) {	
			}
			@Override
			public void mouseReleased(MouseEvent e) {	
			}
	    });

	}

	
}
