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
	
	//Tracks a set of packages that depend on a certain package
	private Map<SoftwarePackage, Set<SoftwarePackage>> reverseDependencies = new HashMap<>();
    

    // === INSTALL PACKAGE ===
    @Override
    public void installPackage(SoftwarePackage p) {
    	if (installedPackages.contains(p)) {
    		System.out.println("Install blocked: " + p.getName() + " is already installed.");
    		return;
    	}
    	
    	boolean success = installPackage(
            p, 
            new HashSet<SoftwarePackage>(), 
            new HashSet<SoftwarePackage>()
        );
    	
    	if (success) {
    		System.out.println("Installed " + p.getName() + " successfully.");
    	} else {
    		System.out.println("Install failed.");
    	}
    }
    
    private boolean installPackage(
        SoftwarePackage p, 
        Set<SoftwarePackage> visited,  //tracks for circular dependencies
        Set<SoftwarePackage> installedDependencies  //tracks installed packages if rollback needed
    ) {
    	  	
        //Checks if visited, stops infinite recursion
        if (visited.contains(p)) {
            System.out.println("Install blocked: Circular dependency detected.");
            
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
            System.out.println("Install failed.");
            installedPackages.removeAll(installedDependencies);  //equivalent to uninstall
            installedDependencies.clear();
            return false;
        }
        return true;
    }


    // === UNINSTALL PACKAGE ===
    @Override
    public void uninstallPackage(SoftwarePackage p) {
    	if (!installedPackages.contains(p)) {
    		System.out.println(p.getName() + " is not installed.");
    		return;
    	}
    	
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
			cleanUpReverseDependencies(packagesToUninstall);
            System.out.println("Uninstalled " + p.getName() + " successfully.");
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
        	//If not on the dep list, p is not needed - add to queue to be uninstalled.
			if (!reverseDependencies.containsKey(p) && !packagesToUninstall.contains(p)){
				packagesToUninstall.add(p);
			}

			//If p is in this set, may still be able to uninstall if all packages that needed it also are queued to uninstall
			if (reverseDependencies.containsKey(p)){
				//Gets the packages that depend on p - replaces iterating over installedPackages.
				Set<SoftwarePackage> dependents = reverseDependencies.get(p);

				//If any deps aren't queued to uninstall, p is still needed
				for (SoftwarePackage deps : dependents){
					if (!packagesToUninstall.contains(deps)){
						System.out.println("Uninstall blocked: " + p.getName() + " is needed by " + deps.getName() + ".");
						return false;
					} 
				}

				//If you get here, p was not needed by any package being kept.
				packagesToUninstall.add(p);
			}
            
            //Attempt to uninstall dependencies exclusive to p
			for(SoftwarePackage dependent : p.getDependencies()){
				uninstallDep(dependent, packagesToUninstall);
			}
			
        } catch (Exception e){
            //If any error, uninstall nothing (revert to restore point).
            System.out.println("Uninstall failed. Error: " + e.getMessage());
            return false;
        }

        return true;
    }

	// === UNINSTALL A DEPENDENT PACKAGE ===
	// This adds unneeded packages to the packagesToUninstall list without blocking the whole uninstall
	private void uninstallDep(SoftwarePackage dep, Set<SoftwarePackage> packagesToUninstall) {
		//Dep can be removed only if all values in reverseDeps are in packagesToUninstall
		boolean canUninstall = true;
		Set<SoftwarePackage> dependents = reverseDependencies.get(dep);

		if (dependents != null) {
			for (SoftwarePackage d : dependents){
				if (!packagesToUninstall.contains(d)){
					canUninstall = false;
					break;
				}
			}
		}

		//If dep is only needed by p or other q'd packages, add to uninstall q, and check its own dependencies.
		if (canUninstall) {
			packagesToUninstall.add(dep);
			
			//Recursively check dep's dependents
			if (dep.getDependencies() != null) {
				for (SoftwarePackage d : dep.getDependencies()) {
					uninstallDep(d, packagesToUninstall);
				}
			}
		}
	}


	// === CLEAN UP REVERSE DEPENDENCY LIST ===
	private void cleanUpReverseDependencies(Set<SoftwarePackage> packagesToUninstall) {
		for (SoftwarePackage p : packagesToUninstall) {
			//Remove p from all dependents sets
			for (Set<SoftwarePackage> dependents : reverseDependencies.values()){
				dependents.remove(p);
			}
			//Remove p as a key
			reverseDependencies.remove(p);
		}
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
 

