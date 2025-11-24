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
	 * Tries to install a package with a circular dependency.
	 * Should fail and revert to restore point.
	 */
	public void testCircularInstall() {

		Set<SoftwarePackage> before = testInstaller.getInstalledPackages();
		Set<SoftwarePackage> after;
		
    	System.out.println("===== TEST CIRCULAR DEPENDENCY =====");
    	testInstaller.installPackage(circularA);
    	System.out.println("Installed packages before: " + before);
    	
		testInstaller.uninstallPackage(circularA);
		
		after = testInstaller.getInstalledPackages();
    	System.out.println("Installed packages after: " + after);
		if (before.equals(after)) {
			System.out.println("SUCCESS");
		} else {
			System.out.println("FAILURE");
		}

	}
	
	
	/*
	 * === CIRCULAR DEPENDENCY UNINSTALL TEST ===
	 * Tries to uninstall a package with a circular dependency.
	 * Should fail and revert to restore point.
	 */
	public void testCircularUnnstall() {

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
