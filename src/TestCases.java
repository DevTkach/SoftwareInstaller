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
	 * === TEST: INSTALL ===
	 * Attempts to install a package. 
	 * Expected: Package should be added to installedPackages.
	 */
	public void testInstallPackage() {
		System.out.println("=== TEST: INSTALL ===");
		
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
	 * === TEST: UNINSTALL ===
	 * Attemps to uninstall a package. 
	 * Expected: Package should be removed from installedPackages.
	 */
	public void testUninstallPackage() {
		System.out.println("=== TEST: UNINSTALL ===");
	
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
	 * === TEST: CIRCULAR DEPENDENCY INSTALL ===
	 * Attemps to install a package with a circular dependency.
	 * Expected: Install should fail and installedPackages remains unchanged.
	 */
	public void testCircularInstall() {
		System.out.println("=== TEST: CIRCULAR DEPENDENCY INSTALL ===");
		
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
	 * === TEST: CIRCULAR DEPENDENCY UNINSTALL ===
	 * Attempts to uninstall a package with a circular dependency.
	 * Expected: Uninstall should fail and installedPackages remains unchanged.
	 */
	public void testCircularUninstall() {
		System.out.println("=== TEST: CIRCULAR DEPENDENCY UNINSTALL ===");
	
		SoftwarePackage circularA = makePackage("A");
		SoftwarePackage circularB = makePackage("B");
		SoftwarePackage circularC = makePackage("C");
		
		testInstaller.installPackage(circularA);
		testInstaller.installPackage(circularB);
		testInstaller.installPackage(circularC);
		
		circularA.getDependencies().add(circularB);
		circularB.getDependencies().add(circularA);
		circularC.getDependencies().add(circularA);
		
		//Before
		Set<SoftwarePackage> before = new HashSet<>(testInstaller.getInstalledPackages());
		System.out.println("Installed packages before: " + nameList(before));
    	
		//Test
    	testInstaller.uninstallPackage(circularA);
    	
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
	 * === TEST: FAILED INSTALL ===
	 * Fails to install a package.
	 * Expected: Should not install and installedPackages remains unchanged.
	 */
	public void testFailedInstall() {
		System.out.println("=== TEST: FAILED INSTALL ===");
		
	}
	
	
	/*
	 * === TEST: FAILED UNINSTALL ===
	 * Fails to uninstall a package.
	 * Expected: Should not uninstall and installedPackages remains unchanged.
	 */
	public void testFailedUninstall() {
		System.out.println("=== TEST: FAILED UNINSTALL ===");
		
	}
	
	
	/*
	 * === TEST: ALREADY INSTALLED ===
	 * Attempts to install a package already installed.
	 * Expected: Should not re-install and installed packages remains unchanged.
	 */
	public void testAlreadyInstalled() {
		System.out.println("=== TEST: ALREADY INSTALLED ===");
	
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
