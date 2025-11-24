import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class main {
    public static void main(String[] args) {
    	
    	TestCases tests = new TestCases();
    	

    	tests.testCircularInstall();


    }
}

/* 
We are in charge of designing a system to install software packages. 
We are required to support the installation of a package and all of its 
dependencies. Here is an example of a package structure that we would 
need to install: A depends on B, C B depends on D, E, F C depends on F F 
depends on G Write code to implement installing package A along with its
 dependencies 
*/ 













