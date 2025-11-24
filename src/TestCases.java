import java.util.HashSet;
import java.util.Set;

public class TestCases {
	
	private DummyInstaller dummy;
	private LinuxSoftwareInstaller testInstaller;
	
	public TestCases() {
		dummy = new DummyInstaller();
		testInstaller = new LinuxSoftwareInstaller(dummy);
	}

	
	/*
	 * === INSTALL TEST ===
	 * Attempts to install a package. 
	 * Expected: Package should be added to installedPackages.
	 */
	public void testInstallPackage() {
		
	}
	
	
	/*
	 * === UNINSTALL TEST ===
	 * Attemps to uninstall a package. 
	 * Expected: Package should be removed from installedPackages.
	 */
	public void testUninstallPackage() {
		
	}
	
	
	
	/*
	 * === CIRCULAR DEPENDENCY INSTALL TEST ===
	 * Attemps to install a package with a circular dependency.
	 * Expected: Install should fail and installedPackages remains unchanged.
	 */
	public void testCircularInstall() {
		System.out.println("=== CIRCULAR DEPENDENCY INSTALL TEST ===");
		
		SoftwarePackage circularA = makePackage("A");
		SoftwarePackage circularB = makePackage("B");
		
		circularA.getDependencies().add(circularB);
		circularB.getDependencies().add(circularA);
		
		//Before
		Set<SoftwarePackage> before = new HashSet<>(testInstaller.getInstalledPackages());
		System.out.println("Installed packages before install: " + before);
    	
		//Test
    	testInstaller.installPackage(circularA);
    	
    	//After
    	Set<SoftwarePackage>after = new HashSet<>(testInstaller.getInstalledPackages());
    	System.out.println("Installed packages after install: " + after);

    	//Check
    	boolean passed = before.equals(after);
    	
    	System.out.println("Expected: No changes to installed packages.");
    	System.out.println("Result: " + (passed ? "PASS" : "FAIL"));

	}
	
	
	/*
	 * === CIRCULAR DEPENDENCY UNINSTALL TEST ===
	 * Attempts to uninstall a package with a circular dependency.
	 * Expected: Uninstall should fail and installedPackages remains unchanged.
	 */
	public void testCircularUninstall() {
		System.out.println("=== CIRCULAR DEPENDENCY UNINSTALL TEST ===");
		
	}
	
	
	/*
	 * === FAILED INSTALL TEST ===
	 * Fails to install a package.
	 * Expected: Should not install and installedPackages remains unchanged.
	 */
	public void testFailedInstall() {
		
	}
	
	
	/*
	 * === FAILED UNINSTALL TEST ===
	 * Fails to uninstall a package.
	 * Expected: Should not uninstall and installedPackages remains unchanged.
	 */
	public void testFailedUninstall() {
		
	}
	
	
	/*
	 * === ALREADY INSTALLED TEST ===
	 * Attempts to install a package already installed.
	 * Expected: Should not re-install and installed packages remains unchanged.
	 */
	public void testAlreadyInstalled() {
		
	}
	
	
	//Construct test package
	private SoftwarePackage makePackage(String name) {
		return new SoftwarePackage(name, null, null, new HashSet<>());
	}
	
	
}
