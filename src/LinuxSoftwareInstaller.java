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
    

/*===== INSTALL PACKAGE ============================*/
    @Override
    public void installPackage(SoftwarePackage p) {
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
            installedPackages.removeAll(installedDependencies);  //equivalent to uninstall
            installedDependencies.clear();
            return false;
        }
        return true;
    }


/*===== UNINSTALL PACKAGE ==============================*/
    @Override
    public void uninstallPackage(SoftwarePackage p) {
        //Creates a set of pkgs to uninstall, uninstalls all at once
        Set<SoftwarePackage> packagesToUninstall = new HashSet<>();

        //Creates a visited set to protect against infinite recursion
        Set<SoftwarePackage> visited = new HashSet<>();

        //Proceeds only if there are no errors
        boolean success = uninstallPackage(
            p, 
            packagesToUninstall, 
            visited, 
            installedPackages
        );

        if (success){
            installedPackages.removeAll(packagesToUninstall);
            System.out.println("Successfully uninstalled " + p.getName() + " and all unneeded dependencies.");
        } else {
            System.out.println("Uninstall failed, no changes made.");
        }
    }
    
    private boolean uninstallPackage(
        SoftwarePackage p, 
        Set<SoftwarePackage> packagesToUninstall, 
        Set<SoftwarePackage> visited, 
        Set<SoftwarePackage> installedPackages
    ) {
        
        //If already visited, infinite recursion detected, abort and rollback
        if(visited.contains(p)){
            System.out.println("Circular dependency detected. Uninstall failed.");
            return false;
        }

        visited.add(p);

        //Tracks shared dependencies
        boolean isNeeded = false;
        
        try {
            //Check all installed packages to see if p is needed by any of them
            for (SoftwarePackage pkg : installedPackages) {
                if (pkg.getDependencies() != null && 
                    !packagesToUninstall.contains(pkg) &&
                    pkg.getDependencies().contains(p)) 
                {
                    isNeeded = true;
                    System.out.println("Package " + p.getName() + " is needed for " + pkg.getName() + ".");
                    break;
                }
            }
            
            //Remove p only if not a dependency in any other package.
            if (!isNeeded) {
                packagesToUninstall.add(p);

                //Check if p dependencies are exclusive to p, and can also be uninstalled
                if (p.getDependencies() != null){
                    for (SoftwarePackage dependency : p.getDependencies()){
                        boolean success = uninstallPackage(
                            dependency, 
                            packagesToUninstall, 
                            visited, 
                            installedPackages
                        );

                        if (!success){
                            return false;
                        }
                    }
                }
            }
        } catch (Exception e){
            //If any error, uninstall nothing (revert to restore point).
            System.out.println("Uninstall failed.");
            return false;
        }

        return true;
    }


/*===== PACKAGE INSTALL STATE =====================*/
    @Override
    public boolean isPackageInstalled(SoftwarePackage p) {
        return installedPackages.contains(p);
    }


/*===== GET INSTALLED PACKAGES =====*/
	public Set<SoftwarePackage> getInstalledPackages(){
		return new HashSet<>(installedPackages);
	}
	
}
 

