import java.util.HashSet;
import java.util.Set;

// Implementation definition
class LinuxSoftwareInstaller implements OperatingSystemInstaller {
    private OperatingSystemInstaller installer;
  
    public LinuxSoftwareInstaller(OperatingSystemInstaller installer) {
        this.installer = installer;
    }

    //Set of all installed packages
    private Set<SoftwarePackage> installedPackages = new HashSet<>();
    

    // === INSTALL PACKAGE ===
    @Override
    public void installPackage(SoftwarePackage p) {
    	if (installedPackages.contains(p)) {
    		System.out.println(p.getName() + " is already installed.");
    		return;
    	}
        installPackage(
            p, 
            new HashSet<SoftwarePackage>(), 
            new HashSet<SoftwarePackage>()
        );
    }
    
    private boolean installPackage(
        SoftwarePackage p, 
        Set<SoftwarePackage> visited, 
        Set<SoftwarePackage> installedDependencies
    ) {
    	  	
        //Checks if visited, stops infinite recursion
        if (visited.contains(p)) {
            System.out.println("Circular dependency detected. Install failed.");
            
            //Roll back: force uninstall pkgs already installed.
            installedPackages.removeAll(installedDependencies);  //equivalent to uninstall
            installedDependencies.clear();
            return false;
        }
        
        visited.add(p);
        
        //Install dependencies
        if (p.getDependencies() != null){
            for (SoftwarePackage dependency : p.getDependencies()){
            	boolean success = installPackage(dependency, visited, installedDependencies);
            	
            	if (!success) {
            		return false;
            	}
            }
        }
        
        try {
            //Only installs if package is not installed.
            if (!installer.isPackageInstalled(p)) {
                installer.installPackage(p);                
                installedDependencies.add(p);
                installedPackages.add(p);
            }
        } catch (Exception e) {
            //Roll back: force uninstall pkgs already installed.
            System.out.println("Installation failed.");
            installedPackages.removeAll(installedDependencies);  //equivalent to uninstall
            installedDependencies.clear();
            return false;
        }
        return true;
    }


    // === UNINSTALL PACKAGE ===
    @Override
    public void uninstallPackage(SoftwarePackage p) {
        //Creates a set of pkgs to uninstall, uninstalls all at once
        Set<SoftwarePackage> packagesToUninstall = new HashSet<>();

        //Proceeds only if there are no errors
        boolean success = uninstallPackage(
            p, 
            packagesToUninstall,
            installedPackages
        );

        if (success){
            installedPackages.removeAll(packagesToUninstall);
        } else {
            System.out.println("Uninstall failed. No changes made.");
        }
    }
    
    private boolean uninstallPackage(
        SoftwarePackage p, 
        Set<SoftwarePackage> packagesToUninstall, 
        Set<SoftwarePackage> installedPackages
    ) {
        
        try {
        	//If p already in queue to uninstall, continue
        	if (packagesToUninstall.contains(p)) {
        		return true;
        	}
        	
        	//p in the queue to be uninstalled.
        	packagesToUninstall.add(p);
        	
        	//Check all installed packages to see if p is needed by any of them
            for (SoftwarePackage pkg : installedPackages) {
             	//Skips packages already on the uninstall list
            	if (packagesToUninstall.contains(pkg)) {
            		continue;  
            	}
            	
            	//If p is needed by another package, uninstall will fail.
            	if (pkg.getDependencies() != null &&
            	    pkg.getDependencies().contains(p)) {
            		System.out.println("Uninstall blocked: " + p.getName() + " is needed by " + pkg.getName() + ".");
            		return false;
            	}
            }
            
            //Attempt to uninstall dependencies exclusive to p
            if (p.getDependencies() != null){
            	for (SoftwarePackage dependency : p.getDependencies()){
            		//If dependency already in queue to uninstall, continue
                	if (packagesToUninstall.contains(dependency)) {
                		continue;
                	}
                	
                	//Removes packages only p needed.
            		boolean success = uninstallPackage(
            			dependency, 
            			packagesToUninstall, 
            			installedPackages
            		);
            		
            		if (!success){
            			return false;
            		}
                }
            }
        } catch (Exception e){
            //If any error, uninstall nothing (revert to restore point).
            System.out.println("Uninstall failed. Error: " + e.getMessage());
            return false;
        }

        return true;
    }


    // === PACKAGE INSTALL STATE ===
    @Override
    public boolean isPackageInstalled(SoftwarePackage p) {
        return installedPackages.contains(p);
    }


    // === GET INSTALLED PACKAGES ===
	public Set<SoftwarePackage> getInstalledPackages(){
		return new HashSet<>(installedPackages);
	}
	
}
 

