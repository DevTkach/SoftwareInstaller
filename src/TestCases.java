import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
		System.out.println("=== INSTALL TEST ===");
		
		SoftwarePackage testA = makePackage("A");
		
		//Before
		Set<SoftwarePackage> before = new HashSet<>(testInstaller.getInstalledPackages());
		System.out.println("Installed packages before: " + nameList(before));
    	
		//Test
    	testInstaller.installPackage(testA);
    	
    	//After
    	Set<SoftwarePackage>after = new HashSet<>(testInstaller.getInstalledPackages());
    	System.out.println("Installed packages after: " + nameList(after));
		
		//Check
    	boolean passed = !before.contains(testA) && after.contains(testA);
    	
    	System.out.println("Expected: " + testA.getName() + " is added to installed packages.");
    	System.out.println("Result: " + (passed ? "PASS" : "FAIL"));
    	System.out.println();		
	}
	
	
	/*
	 * === UNINSTALL TEST ===
	 * Attemps to uninstall a package. 
	 * Expected: Package should be removed from installedPackages.
	 */
	public void testUninstallPackage() {
		System.out.println("=== UNINSTALL TEST ===");
	
		SoftwarePackage testA = makePackage("A");
    	testInstaller.installPackage(testA);
		
		//Before
		Set<SoftwarePackage> before = new HashSet<>(testInstaller.getInstalledPackages());
		System.out.println("Installed packages before: " + nameList(before));
    	
		//Test
		testInstaller.uninstallPackage(testA);
    	
    	//After
    	Set<SoftwarePackage>after = new HashSet<>(testInstaller.getInstalledPackages());
    	System.out.println("Installed packages after: " + nameList(after));
		
		//Check
    	boolean passed = before.contains(testA) && !after.contains(testA);
    	
    	System.out.println("Expected: " + testA.getName() + " is removed from installed packages.");
    	System.out.println("Result: " + (passed ? "PASS" : "FAIL"));
    	System.out.println();		
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
		System.out.println("Installed packages before: " + nameList(before));
    	
		//Test
    	testInstaller.installPackage(circularA);
    	
    	//After
    	Set<SoftwarePackage>after = new HashSet<>(testInstaller.getInstalledPackages());
    	System.out.println("Installed packages after: " + nameList(after));

    	//Check
    	boolean passed = before.equals(after);
    	
    	System.out.println("Expected: No changes to installed packages.");
    	System.out.println("Result: " + (passed ? "PASS" : "FAIL"));
    	System.out.println();
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
		System.out.println("=== FAILED INSTALL TEST ===");
		
	}
	
	
	/*
	 * === FAILED UNINSTALL TEST ===
	 * Fails to uninstall a package.
	 * Expected: Should not uninstall and installedPackages remains unchanged.
	 */
	public void testFailedUninstall() {
		System.out.println("=== FAILED UNINSTALL TEST ===");
		
	}
	
	
	/*
	 * === ALREADY INSTALLED TEST ===
	 * Attempts to install a package already installed.
	 * Expected: Should not re-install and installed packages remains unchanged.
	 */
	public void testAlreadyInstalled() {
		System.out.println("=== ALREADY INSTALLED TEST ===");
	
		SoftwarePackage testA = makePackage("A");
    	testInstaller.installPackage(testA);
		
		//Before
		Set<SoftwarePackage> before = new HashSet<>(testInstaller.getInstalledPackages());
		System.out.println("Installed packages before: " + nameList(before));
    	
		//Test
    	testInstaller.installPackage(testA);
    	
    	//After
    	Set<SoftwarePackage>after = new HashSet<>(testInstaller.getInstalledPackages());
    	System.out.println("Installed packages after: " + nameList(after));
		
		//Check
    	boolean passed = before.equals(after);
    	
    	System.out.println("Expected: No changes to installed packages.");
    	System.out.println("Result: " + (passed ? "PASS" : "FAIL"));
    	System.out.println();
	}
	
	
	//Creates test package
	private SoftwarePackage makePackage(String name) {
		return new SoftwarePackage(name, null, null, new HashSet<>());
	}
	
	//Convert HashSet to List to print pkg names
	private static List<String> nameList(Set<SoftwarePackage> set){
		List<String> names = new ArrayList<>();
		for (SoftwarePackage p : set) {
			names.add(p.getName());
		}
		return names;
	}
	
	
}
