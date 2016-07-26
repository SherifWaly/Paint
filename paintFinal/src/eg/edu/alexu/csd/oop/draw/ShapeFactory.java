package eg.edu.alexu.csd.oop.draw;


public class ShapeFactory {
	public static Shape create(String s) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		Class<?> classs = Class.forName(s);
		Shape newShape = (Shape)classs.newInstance();
		return newShape;
	}

}