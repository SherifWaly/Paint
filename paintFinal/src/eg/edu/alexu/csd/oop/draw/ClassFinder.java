package eg.edu.alexu.csd.oop.draw;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;


public class ClassFinder {

    @SuppressWarnings({ "unused" })
	private Class<Shape> superClass = null;
  
    private Set <Class <? extends Shape>> classes = new HashSet<Class <? extends Shape>>(2000);

    public ClassFinder() {
        this.superClass = Shape.class;

    }

    public Set<Class<? extends Shape>> getClasses() {

        String classpath = System.getProperty("java.class.path");
        String pathSeparator = System.getProperty("path.separator");

        String []string = classpath.split(pathSeparator)  ;

        for(int i = 0 ; i < string.length ; i++) {
        	if(isArchive(string[i])){
        		//System.out.println(string[i]);
        		try {
					processJar(string[i]) ;
				} catch (MalformedURLException e) {
					// TODO Auto-generated caclassToAddh block
				}
        	}
        }

        return this.classes;
    }
    public Set<Class<? extends Shape>> getClasses(String string) {

        if(isArchive(string)){
       		System.out.println(string);
       		try {
				processJar(string) ;
			} catch (MalformedURLException e) {		
			}
        }

        return this.classes;
    }

    private boolean isArchive(String name) {
        if (name.endsWith(".jar")) {

            return true;
        } else {
            return false;
        }
    }

	@SuppressWarnings("unchecked")
	private void processJar(String p) throws MalformedURLException {
		ClassLoader loader = getClass().getClassLoader();
		String path = (new File(p)).getPath();

		try {
			JarInputStream jis = new JarInputStream(new FileInputStream(path));
			JarEntry je = jis.getNextJarEntry();
			while (je != null) {
				if (je.getName().endsWith(".class")) {
					String x = je.getName().replaceAll("/", ".");
					x = x.split(".class")[0];
					String className = je.getName().substring(0, je.getName().length() - 6);
					className = className.replace('/', '.');
					try {
						Class<?> classToAdd = Class.forName(x, true, loader);
						if (!classToAdd.isInterface()
								&& !Modifier.isAbstract(classToAdd.getModifiers()) && classToAdd.newInstance() instanceof Shape) {
							this.classes.add(((Class<? extends Shape>) classToAdd));
						}
					} catch (Exception ex) {
					}
				}
				je = jis.getNextJarEntry();

			}
			jis.close();
		} catch (Exception ex) {
		}
	}
}
