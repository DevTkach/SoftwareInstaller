import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestCases {
	
	/*
	 * === TEST: INSTALL ===
	 * Attempts to install a package. 
	 * Expected: Package should be added to installedPackages.
	 */
	public void testInstallPackage() {
		System.out.println("=== TEST: INSTALL ===");
		
		DummyInstaller dummy = new DummyInstaller();
		LinuxSoftwareInstaller testInstaller = new LinuxSoftwareInstaller(dummy);
		
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
	
		DummyInstaller dummy = new DummyInstaller();
		LinuxSoftwareInstaller testInstaller = new LinuxSoftwareInstaller(dummy);

		SoftwarePackage testB = makePackage("B");
    	testInstaller.installPackage(testB);
		
		//Before
		Set<SoftwarePackage> before = new HashSet<>(testInstaller.getInstalledPackages());
		System.out.println("Installed packages before: " + nameList(before));
    	
		//Test
		testInstaller.uninstallPackage(testB);
    	
    	//After
    	Set<SoftwarePackage>after = new HashSet<>(testInstaller.getInstalledPackages());
    	System.out.println("Installed packages after: " + nameList(after));
		
		//Check
    	boolean passed = before.contains(testB) && !after.contains(testB);
    	
    	System.out.println("Expected: " + testB.getName() + " is removed from installed packages.");
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
		
		DummyInstaller dummy = new DummyInstaller();
		LinuxSoftwareInstaller testInstaller = new LinuxSoftwareInstaller(dummy);

		SoftwarePackage circularC = makePackage("C");
		SoftwarePackage circularD = makePackage("D");
		
		circularC.getDependencies().add(circularD);
		circularD.getDependencies().add(circularC);
		
		//Before
		Set<SoftwarePackage> before = new HashSet<>(testInstaller.getInstalledPackages());
		System.out.println("Installed packages before: " + nameList(before));
    	
		//Test
    	testInstaller.installPackage(circularC);
    	
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
	 * === TEST: DEPENDENCY BLOCKED UNINSTALL ===
	 * Attempts to uninstall a package with a circular dependency.
	 * Expected: Uninstall should fail and installedPackages remains unchanged.
	 */
	public void testDepBlockedUninstall() {
		System.out.println("=== TEST: DEPENDENCY BLOCKED UNINSTALL ===");
	
		DummyInstaller dummy = new DummyInstaller();
		LinuxSoftwareInstaller testInstaller = new LinuxSoftwareInstaller(dummy);

		SoftwarePackage circularE = makePackage("E");
		SoftwarePackage circularF = makePackage("F");
		SoftwarePackage circularG = makePackage("G");
		
		testInstaller.installPackage(circularE);
		testInstaller.installPackage(circularF);
		testInstaller.installPackage(circularG);
		
		circularE.getDependencies().add(circularF);
		circularF.getDependencies().add(circularE);
		circularG.getDependencies().add(circularE);
		
		//Before
		Set<SoftwarePackage> before = new HashSet<>(testInstaller.getInstalledPackages());
		System.out.println("Installed packages before: " + nameList(before));
    	
		//Test
    	testInstaller.uninstallPackage(circularE);
    	
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
		//TODO
	}
	
	
	/*
	 * === TEST: FAILED UNINSTALL ===
	 * Fails to uninstall a package.
	 * Expected: Should not uninstall and installedPackages remains unchanged.
	 */
	public void testFailedUninstall() {
		System.out.println("=== TEST: FAILED UNINSTALL ===");
		//TODO
	}
	
	
	/*
	 * === TEST: ALREADY INSTALLED ===
	 * Attempts to install a package already installed.
	 * Expected: Should not re-install and installed packages remains unchanged.
	 */
	public void testAlreadyInstalled() {
		System.out.println("=== TEST: ALREADY INSTALLED ===");
	
		DummyInstaller dummy = new DummyInstaller();
		LinuxSoftwareInstaller testInstaller = new LinuxSoftwareInstaller(dummy);

		SoftwarePackage testH = makePackage("H");
		SoftwarePackage testJ = makePackage("J");
		
    	testInstaller.installPackage(testH);
    	testInstaller.installPackage(testJ);
		
		//Before
		Set<SoftwarePackage> before = new HashSet<>(testInstaller.getInstalledPackages());
		System.out.println("Installed packages before: " + nameList(before));
    	
		//Test
    	testInstaller.installPackage(testH);
    	
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
