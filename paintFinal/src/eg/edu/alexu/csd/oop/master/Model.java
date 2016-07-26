package eg.edu.alexu.csd.oop.master;
import java.awt.Color;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.Stack;

import javax.swing.JFrame;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import eg.edu.alexu.csd.oop.draw.Observable;
import eg.edu.alexu.csd.oop.draw.Observer;
import eg.edu.alexu.csd.oop.draw.Shape;
import eg.edu.alexu.csd.oop.draw.ShapeFactory;
import eg.edu.alexu.csd.oop.draw.StackNode;

public class Model{
	private LinkedList<Shape> shapes = new LinkedList<Shape>() ;
	private Stack<StackNode>undoStack = new Stack<StackNode>() ;
	private Stack<StackNode>redoStack = new Stack<StackNode>() ;
	private BufferedReader reader;
	private ArrayList<Observer> views=new ArrayList<Observer>();
	public void addShape(Shape shape){
		shapes.add(shape) ;
		StackNode node = new StackNode(null,shape,shapes.size()-1) ;
		undoStack.push(node) ;
		redoStack.clear() ;
		modifyStack(undoStack) ;
	}

	public void removeShape(Shape shape) {
		int x = lastIndex(shape) ;
		if(x == -1){
			throw new RuntimeException();
		}
		shapes.remove(x) ; 
		StackNode node = new StackNode(shape,null,x) ;
		undoStack.push(node) ;
		redoStack.clear() ;
		modifyStack(undoStack) ;
	}

	public void updateShape(Shape oldShape, Shape newShape) {

		int found = lastIndex(oldShape); 
		if(found == -1){
			throw new RuntimeException() ;
		}
		StackNode node = new StackNode(oldShape,newShape,found) ;
		shapes.set(found, newShape) ;
		undoStack.push(node) ;
		redoStack.clear();
		modifyStack(undoStack) ;
	}

	public Shape[] getShapes() {
		Shape[]arr = new Shape[shapes.size()] ;
		int idx = 0 ;
		for(int i = 0 ; i < shapes.size() ; i++){
			arr[idx++] = shapes.get(i)  ;
		}
		return arr ;
	}


	
	public void undo() {
		if(undoStack.isEmpty()){
			throw new RuntimeException() ;
		}
		StackNode node = undoStack.pop() ;
		if(node.getOldShape() == null){
			shapes.add(node.getIndex(), node.getNewShape());
		}
		else if(node.getNewShape() == null){
			shapes.remove(node.getIndex());
		}
		else {
			shapes.set(node.getIndex(), node.getNewShape()) ;
		}
		node = new StackNode(node.getOldShape(),node.getNewShape(),node.getIndex()) ;
		redoStack.push(node) ;
		modifyStack(redoStack) ;
	}

	public void redo() {
		if(redoStack.isEmpty()){
			throw new RuntimeException() ;
		}
		StackNode node = redoStack.pop() ;
		if(node.getOldShape() == null){
			shapes.add(node.getIndex(), node.getNewShape());
		}
		else if(node.getNewShape() == null){
			shapes.remove(node.getIndex());
		}
		else {
			shapes.set(node.getIndex(), node.getNewShape()) ;
		}
		node = new StackNode(node.getOldShape(),node.getNewShape(),node.getIndex()) ;
		undoStack.push(node) ;
		modifyStack(undoStack) ;
	}
	
	private void modifyStack(Stack<StackNode>stack){
		if(stack.size() > 20){
			StackNode []assistStack = new StackNode[20] ;

			for(int i = 0 ; i < 20 ; i++){
				assistStack[i] = stack.pop() ;
			}
			for(int i = 19 ; i >= 0 ; i--){
				stack.push(assistStack[i]) ;
			}
		}
	}
	public void saveXML(String path){
		try{
			PrintWriter writer = new PrintWriter(path) ;
			writer.println("<?xml version=" +"\"" +"1.0" + "\"" + "?>");
			writer.println("<Classes>");
			ListIterator<Shape> listIterator = shapes.listIterator();
		    while (listIterator.hasNext()) {
	            Shape shape = listIterator.next() ;
	            if(shape == null){
		            writer.println("<Class name=\""+"null"+"\">");
		            writer.println("</Class>");
		            continue ;
	            }
	            writer.println("<Class name=\""+shape.getClass().getName()+"\">");
	            Boolean flag = (shape.getPosition() == null) ;
	            writer.println("<x>" + (flag ? "" : shape.getPosition().x) + "</x>") ;
	            writer.println("<y>" + (flag ? "" : shape.getPosition().y) + "</y>") ;
	            
	            writer.println("<inner>") ;
	            try{
	            Color x = shape.getColor() ;
	            writer.println("<RGB>"+((x == null) ? "" : x.getRGB())+"</RGB>");
	            }catch(Exception e){}
	            writer.println("</inner>") ;
	            
	            writer.println("<outer>") ;
	            try{
	            Color x = shape.getFillColor() ;
	            writer.println("<RGB>"+((x == null) ? "" : x.getRGB())+"</RGB>");
	            }catch(Exception e ){}
	            writer.println("</outer>") ;
	            
	            Map<String,Double>map = shape.getProperties() ;
	            try{
	            if(map != null){
		            for (Map.Entry<String, Double> entry : map.entrySet()){
		            	Double y = entry.getValue() ;
		                writer.println("<" + entry.getKey() + ">" + ((y == null) ? "" : entry.getValue().toString() )+ "</" + entry.getKey() + ">");
		            }
	            }
	            }
	            catch(Exception e){}
	            writer.println("</Class>");
	        }
		    
		    writer.println("</Classes>");
		    writer.close();
		}
		catch(Exception e){
		}
	}
	public void loadXML(String path){
		try{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder() ;
			Document document ;
			document = builder.parse(path) ;
			LinkedList<Shape> loadShapes = new LinkedList<Shape>() ;
			NodeList classes = document.getElementsByTagName("Class") ;
			for(int i = 0 ; i < classes.getLength() ; i++){
				Node c = classes.item(i) ;
				if(c.getNodeType() == Node.ELEMENT_NODE){
					Element clazz = (Element)c ;
					String name = clazz.getAttribute("name") ;
					if(name.equals("null")){
						loadShapes.add(null) ;
						continue ;
					}
					Shape shape = ShapeFactory.create(name) ;
					loadShapes.add(shape) ;
					NodeList elements = clazz.getChildNodes() ;
					String x = ((Element)(elements.item(1))).getTextContent() ;
					String y = ((Element)(elements.item(3))).getTextContent() ;
					if(x != null && y != null && x != "" && y != ""){
						shape.setPosition(new Point(Integer.parseInt(x),Integer.parseInt(y)));
					}
					NodeList innerColors = elements.item(5).getChildNodes() ;
					int cc = 0 ;
					for(int j = 0 ; j < innerColors.getLength() ; j++){
						Node color = innerColors.item(j) ;
						if(color.getNodeType() == Node.ELEMENT_NODE){
							Element el = (Element)color ;
							if(el.getTextContent() != null && el.getTextContent() != ""){
								cc =  Integer.parseInt(el.getTextContent()) ;
							}
						}
					}
					if(cc != 0){
						shape.setColor(new Color(cc));
					}
					cc = 0 ;
					NodeList outerColors = elements.item(7).getChildNodes() ;
					for(int j = 0 ; j < outerColors.getLength() ; j++){
						Node color = outerColors.item(j) ;
						if(color.getNodeType() == Node.ELEMENT_NODE){
							Element el = (Element)color ;
							if(el.getTextContent() != null && el.getTextContent() != ""){
								cc =  Integer.parseInt(el.getTextContent()) ;
							}
						}
					}
					if(cc != 0){
						shape.setFillColor(new Color(cc));
					}
					Map<String,Double>map = shape.getProperties();
					if(map != null){
						for(int j = 8 ; j < elements.getLength() ; j++){
							Node element = elements.item(j) ;
							if(element.getNodeType() == Node.ELEMENT_NODE){
								Element el = (Element)element ;
								String n = el.getTextContent() ;
								Boolean flag = (n == "")||(n == null) ;
								System.out.println(flag) ;
								map.put(el.getTagName(),(flag ? null : Double.parseDouble(el.getTextContent()))) ;
							}
						}
					}
					shape.setProperties(map);	
				}
			}
			shapes = loadShapes ;
			undoStack.clear();
			redoStack.clear();
		}
		catch(Exception e){
			throw new RuntimeException() ;
		}
		
	}
	public void saveJSON(String path) throws FileNotFoundException{
		try{
			PrintWriter writer = new PrintWriter(path) ;
			writer.println("{");
			writer.println("\t\"Classes\" : ");
			writer.println("\t\t[");
			ListIterator<Shape> listIterator = shapes.listIterator();
		    while (listIterator.hasNext()) {
	            Shape shape = listIterator.next() ;
	            writer.println("\t\t\t{");
	            writer.println("\t\t\t\t\"ClassName\" : "+"\""+shape.getClass().getName()+"\",");
	            boolean flag = (shape.getPosition() == null) ;
	            writer.println("\t\t\t\t\"x\""+" : \"" + (flag ?"": shape.getPosition().x)+ "\",") ;
	            writer.println("\t\t\t\t\"y\""+" : \"" + (flag ?"": shape.getPosition().y)+ "\",") ;
	            flag = (shape.getColor() == null) ;
	            writer.println("\t\t\t\t\"innerRGB\" : \"" + (flag ? "" :shape.getColor().getRGB()) + "\",") ;
	            flag = (shape.getFillColor() == null) ;
	            writer.println("\t\t\t\t\"outerRGB\" : \"" + (flag ? "" :shape.getFillColor().getRGB()) + "\",") ;
	            Map<String,Double>map = shape.getProperties() ;
	            if(map != null){
		            for (Map.Entry<String, Double> entry : map.entrySet()){
		            	flag = (entry.getValue() == null) ;
		                writer.println("\t\t\t\t\""+entry.getKey() + "\" : \"" + (flag ? "" :entry.getValue().toString()) + "\",");
		            }
	            }
	            
	            writer.print("\t\t\t}");
	            if(listIterator.hasNext()){
	            	writer.print(",");
	            }
	            writer.println();
	        }
		    writer.println("\t\t]");
		    
		    writer.println("}");
		    writer.close();
		}
		catch(Exception e){
			throw new FileNotFoundException() ;
		}
	}
	public void loadJSON(String path) throws FileNotFoundException{
		try{
			reader = new BufferedReader(new FileReader(path));
			reader.readLine() ;
			reader.readLine() ;
			reader.readLine() ;
			LinkedList<Shape> loadShapes = new LinkedList<Shape>() ;
			while(true){
				String s = reader.readLine();
				s = s.replaceAll("\\s", "");
				if(s.charAt(0) == ']')break ;
				s = reader.readLine();
				s = s.replaceAll("\\s", "");
				s = s.replaceAll(",","");
				s = s.replaceAll("\"","") ;
				String []string = s.split(":");
				Point point = null ;
				Map<String,Double>map = new HashMap<String, Double>() ;
				Shape shape = ShapeFactory.create(string[1]) ;
				s = reader.readLine();
				s = s.replaceAll("\\s", "");
				s = s.replaceAll("\"","") ;
				s = s.replaceAll(",","");
				string = s.split(":");
				if(string.length < 2);
				else {
					point = new Point() ;
					point.x = Integer.parseInt(string[1]) ;
				}
				s = reader.readLine();
				s = s.replaceAll("\\s", "");
				s = s.replaceAll("\"","") ;
				s = s.replaceAll(",","");
				string = s.split(":");
				if(string.length < 2);
				else point.y = Integer.parseInt(string[1]) ;
				shape.setPosition(point);
				Color color = null ;
				int x = 0 , y = 0 ;
				s = reader.readLine();
				s = s.replaceAll("\\s", "");
				s = s.replaceAll("\"","") ;
				s = s.replaceAll(",","");
				string = s.split(":");
				x=0 ;
				if(string.length < 2);
				else x = Integer.parseInt(string[1]) ;
				if(x != 0)color = new Color(x) ;
				shape.setColor(new Color(x));
				s = reader.readLine();
				s = s.replaceAll("\\s", "");
				s = s.replaceAll("\"","") ;
				s = s.replaceAll(",","");
				string = s.split(":");
				y = 0 ;
				if(string.length < 2);
				else y = Integer.parseInt(string[1]) ;
				if(y != 0)color = new Color(y) ;
				shape.setFillColor(color);
				loadShapes.add(shape) ;
				map = shape.getProperties() ;
				while(true){
					s = reader.readLine();
					s = s.replaceAll("\\s", "");
					s = s.replaceAll("\"","") ;
					s = s.replaceAll(",","");
					if(s.charAt(0) == '}')break ;
					string = s.split(":");
					map.put(string[0], null) ;
					if(string.length == 2)map.put(string[0], Double.parseDouble(string[1])) ;
				}
				shape.setProperties(map);	
			}
			shapes = loadShapes ;
			undoStack.clear();
			redoStack.clear();		
		}
		catch(Exception e){
			throw new FileNotFoundException() ;
		}
	}
	private int lastIndex(Shape shape){
		int index = -1 ;
		for(int i = shapes.size()-1 ; i >= 0 ; i--){
			if(compare(shape,shapes.get(i)))return i ;
		}
		return index ;
	}
	private boolean compare(Shape s1 , Shape s2){
		boolean flag = false ;
		if(s1.getColor() == null && s2.getColor() != null || s1.getColor() != null && s2.getColor() == null)return false ;
		if(s1.getColor() == null && s2.getColor() == null){
			flag = true ;
		}
		if(!flag){
			if(s1.getColor().getRGB()!=s2.getColor().getRGB())return false; 
		}
		flag = false ;
		if(s1.getFillColor() == null && s2.getFillColor() != null || s1.getFillColor() != null && s2.getFillColor() == null)return false ;
		if(s1.getFillColor() == null && s2.getFillColor() == null){
			flag = true ;
		}
		if(!flag){
			if(s1.getFillColor().getRGB()!=s2.getFillColor().getRGB())return false; 
		}
		flag = false ;
		if(s1.getPosition() == null && s2.getPosition() != null || s1.getPosition() != null && s2.getPosition() == null)return false ;
		if(s1.getPosition() == null && s2.getPosition() == null){
			flag = true ;
		}
		if(!flag){
			if(s1.getPosition().x != s2.getPosition().x || s1.getPosition().y !=s2.getPosition().y )return false; 
		}
		Map<String,Double>map1,map2;
		map1 = s1.getProperties();
		map2 = s2.getProperties();
		if(map1 == null && map2 == null)return true ;
		if(map1.size() != map2.size())return false ;
		for(Map.Entry<String, Double>entry : map1.entrySet()){
			if(map2.containsKey(entry.getKey()) == false)return false ;
			Double d1 = entry.getValue() ;
			Double d2 = map2.get(entry.getKey()) ;
			if(d1 == null && d2 == null)continue ;
			if(d1 == null && d2 != null)return false ;
			if(d1 != null && d2 == null)return false ;
			if(d1.equals(d2) == false)return false ;
		}
		return true ;
	}
	public int getNearest(Point p){
		Shape []shapes = getShapes();
		shapes=getShapes();
		double min=100000000;
		int minIndex=0;
		for(int i=shapes.length-1;i>=0;i--){
			System.out.println(i);
			double x=Math.sqrt(Math.pow((double)(shapes[i].getPosition().getX()-p.getX()), (double)2)+Math.pow((double)(shapes[i].getPosition().getY()-p.getY()), (double)2));
			System.out.println(x);
			if(x<min){
				min=x;
				minIndex=i;
			}
		}
		return minIndex;
		
	}


	
	
}
