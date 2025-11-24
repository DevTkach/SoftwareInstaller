import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class main {
    public static void main(String[] args) {

        // Create dummy installer
        OperatingSystemInstaller testInstaller = new TestInstaller();

        // Create your Linux installer with the dummy as the underlying installer
        LinuxSoftwareInstaller linuxInstaller = new LinuxSoftwareInstaller(testInstaller);

        // Create some packages
        SoftwarePackage libA = new SoftwarePackage(
            "LibA", 
            Path.of("/tmp/libA"), 
            "Library A", 
            new HashSet<>()
        );

        SoftwarePackage libB = new SoftwarePackage(
            "LibB",
            Path.of("/tmp/libB"),
            "Library B",
            Set.of(libA)    // libB depends on libA
        );

        SoftwarePackage app = new SoftwarePackage(
            "App",
            Path.of("/tmp/app"),
            "Main application",
            Set.of(libB)    // app depends on libB -> libA
        );

        // Test install
        System.out.println("\n=== Installing App ===");
        linuxInstaller.installPackage(app);

        // Test uninstall
        System.out.println("\n=== Uninstalling App ===");
        linuxInstaller.uninstallPackage(app);
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













