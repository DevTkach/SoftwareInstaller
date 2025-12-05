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
        Set<SoftwarePackage> visited, 
        Set<SoftwarePackage> installedDependencies
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
			for (SoftwarePackage pkg : packagesToUninstall) {
				//Remove from packages to uninstall set
				installedPackages.remove(pkg);

				//Remove from reverse dependency map
				reverseDependencies.remove(pkg);

				//Remove from other pkgs reverse dependencies
				for (Set<SoftwarePackage> dependers : reverseDependencies.values()){
					dependers.remove(pkg);
				}
			}
			
            System.out.println("Uninstalled " + p.getName() + " successfully.");
			
        } else {
            System.out.println("Uninstall failed. No changes made.");
        }
    }
    
    private boolean uninstallPackage(
        SoftwarePackage p, 
        Set<SoftwarePackage> packagesToUninstall, 
    ) {
        
        try {
        	//If p already in queue to uninstall, continue
        	if (packagesToUninstall.contains(p)) {
        		return true;
        	}
        	
        	//p in the queue to be uninstalled.
        	packagesToUninstall.add(p);

			//Gets packages that depend on p, replaces iterating over installedPackages.
			Set<SoftwarePackage> dependents = reverseDependencies.get(p);

			//If packages depend on p, those packages must be in packagesToUninstall, otherwise it will block.
			if (dependents != null){
				for (SoftwarePackage dependent : dependents) {
					if(!packagesToUninstall.contains(dependent)){
						System.out.println("Uninstall blocked: " + p.getName() + " is needed by " + dependent.getName() + ".");
						return false;
					}
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
 

