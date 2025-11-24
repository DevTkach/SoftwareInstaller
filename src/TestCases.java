import java.util.HashSet;
import java.util.Set;

public class TestCases {
	
	LinuxSoftwareInstaller testInstaller = new LinuxSoftwareInstaller(new DummyInstaller());
	
	//Test packages
	SoftwarePackage circularA;
	SoftwarePackage circularB;
	
	public TestCases() {
		SoftwarePackage circularA = new SoftwarePackage("A", null, null, new HashSet<>());
		SoftwarePackage circularB = new SoftwarePackage("B", null, null, new HashSet<>());
		
		circularA.getDependencies().add(circularB);
		circularB.getDependencies().add(circularA);
	}

	
	
	
/*===== TEST INSTALL PACKAGE =====*/
	//Test to see if a package can be added to installedPackages
	public void testInstallPackage() {
		
	}
	
/*===== TEST UNINSTALL PACKAGE =====*/
	//Test to see if a package can be removed from installedPackages
	public void testUninstallPackage() {
		
	}
	
	
/*===== TEST CIRCULAR DEPENDENCY =====*/
	//Test to see how it handles circular dependency
	public void testCircularDependency() {
		Set<SoftwarePackage> before = testInstaller.getInstalledPackages();
		Set<SoftwarePackage> after;
		
    	System.out.println("===== TEST CIRCULAR DEPENDENCY =====");
    	System.out.println("Installed packages: " + before);
    	
		testInstaller.uninstallPackage(circularA);
		
		after = testInstaller.getInstalledPackages();
    	System.out.println("Installed packages: " + after);
		if (before == after) {
			System.out.println("SUCCESS");
		} else {
			System.out.println("FAILURE");
		}

	}
	
	
/*===== TEST FAILED INSTALL =====*/
	//Test to see if failed install reverts to restore point.
	public void testFailedInstall() {
		
	}
	
	
/*===== TEST FAILED UNINSTALL =====*/
	//Test to see if failed uninstall reverts to restore point.
	public void testFailedUninstall() {
		
	}
	
	
}
