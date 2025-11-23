// Behavior declaration 
public interface OperatingSystemInstaller { 
	public void installPackage(SoftwarePackage p); 
	public void uninstallPackage(SoftwarePackage p); 
	public boolean isPackageInstalled(SoftwarePackage p); 
} 
