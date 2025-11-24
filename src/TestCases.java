import java.util.HashSet;
import java.util.Set;

public class TestCases {
	
	LinuxSoftwareInstaller testInstaller = new LinuxSoftwareInstaller(new DummyInstaller());
	
	//Test packages
	SoftwarePackage circularA;
	SoftwarePackage circularB;
	
	public TestCases() {
		circularA = new SoftwarePackage("A", null, null, new HashSet<>());
		circularB = new SoftwarePackage("B", null, null, new HashSet<>());
		
		circularA.getDependencies().add(circularB);
		circularB.getDependencies().add(circularA);
	}

	
	/*
	 * === INSTALL TEST ===
	 * Tries to install a package. 
	 * Should add the package to installedPackages.
	 */
	public void testInstallPackage() {
		
	}
	
	
	/*
	 * === UNINSTALL TEST ===
	 * Tries to uninstall a package. 
	 * Should remove the package from installedPackages.
	 */
	public void testUninstallPackage() {
		
	}
	
	
	
	/*
	 * === CIRCULAR DEPENDENCY INSTALL TEST ===
	 * Attemps to install a package with a circular dependency.
	 * Expected: install should fail and installedPackages remains unchanged.
	 */
	public void testCircularInstall() {
		System.out.println("=== CIRCULAR DEPENDENCY INSTALL TEST ===");
		
		//Before
		Set<SoftwarePackage> before = testInstaller.getInstalledPackages();
		System.out.println("Installed packages before install: " + before);
    	
		//Test
    	testInstaller.installPackage(circularA);
    	
    	//After
    	Set<SoftwarePackage>after = testInstaller.getInstalledPackages();
    	System.out.println("Installed packages after install: " + after);

    	//Check
    	boolean passed = before.equals(after);
    	
    	System.out.println("Expected: no changes to installed packages.");
    	System.out.println("Result: " + (passed ? "PASS" : "FAIL"));

	}
	
	
	/*
	 * === CIRCULAR DEPENDENCY UNINSTALL TEST ===
	 * Tries to uninstall a package with a circular dependency.
	 * Should fail and revert to restore point.
	 */
	public void testCircularUninstall() {

	}
	
	
	/*
	 * === FAILED INSTALL TEST ===
	 * Fails to install a package.
	 * Should not install and should revert to restore point.
	 */
	public void testFailedInstall() {
		
	}
	
	
	/*
	 * === FAILED UNINSTALL TEST ===
	 * Fails to uninstall a package.
	 * Should not uninstall and should revert to restore point.
	 */
	public void testFailedUninstall() {
		
	}
	
	
}
