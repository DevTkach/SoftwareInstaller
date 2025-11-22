import java.util.HashSet;
import java.util.Set;

// Implementation definition
class LinuxSoftwareInstaller implements OperatingSystemInstaller {
    private OperatingSystemInstaller installer;
  
    public LinuxSoftwareInstaller(OperatingSystemInstaller installer) {
        this.installer = installer;
    }

    //Set of all installed packages
    private Set<SoftwarePackage> installedPackages = new HashSet<>()


    @Override
    public void installPackage(SoftwarePackage p) {
        installPackage(p, new HashSet<SoftwarePackage>(), new HashSet<SoftwarePackage>());
    }
    
    private void installPackage(SoftwarePackage p, Set<SoftwarePackage> visited, Set<SoftwarePackage> installedDependencies) {
        //Checks if visited, stops infinite recursion
        if (visited.contains(p)) {
            System.out.println("Circular dependency detected. Install failed.");
            
            //Uninstall the installed packages to restore to before point
            for (SoftwarePackage pkg : installedDependencies){
                uninstallPackage(pkg, installedDependencies);
            }
            return;
        }
        
        visited.add(p);
        
        //Check p.dependencies is not null
        if (p.getDependencies() != null){
            for (SoftwarePackage dependency : p.getDependencies()){
                installPackage(dependency, visited, installedDependencies);
            }
        }
        
        
        try {
            //Only installs if package is not installed.
            if (!installer.isPackageInstalled(p)) {
                installer.installPackage(p);
                System.out.println("Successfully installed " + p.getName());
                installedDependencies.add(p);
                installedPackages.add(p);
            }
        } catch (Exception e) {
            System.out.println("Failed to install " + p.getName());
            
            //Uninstall the installed packages to restore to before point
            System.out.println("Returning to restore point.");
            for (SoftwarePackage pkg : installedDependencies){
                uninstallPackage(pkg, installedDependencies);
            }
        }
    }
    
    @Override
    public void uninstallPackage(SoftwarePackage p) {
        Set<SoftwarePackage> packagesToUninstall = new HashSet<>();
        uninstallPackage(p, packagesToUninstall, installedPackages);
        installedPackages.removeAll(packagesToUninstall);
    }
    
    private void uninstallPackage(SoftwarePackage p, Set<SoftwarePackage> packagesToUninstall, Set<SoftwarePackage> installedPackages) {
        boolean isNeeded = false;
        
        try {
            //Check all installed packages to see if p is needed by any of them
            for (SoftwarePackage pkg : installedPackages) {
                if (pkg.getDependencies() != null && !packagesToUninstall.contains(pkg)) {
                    if (pkg.getDependencies().contains(p)) {
                        isNeeded = true;
                        System.out.println("Package " + p.getName() + " is needed for " + pkg.getName() + ".");
                        break;
                    }
                }
            }
            
            //Remove p only if not a dependency in any other package.
            if (!isNeeded) {
                packagesToUninstall.add(p);

                //Check if p dependencies are exclusive to p, and can also be uninstalled
                if (p.getDependencies() != null){
                    for (SoftwarePackage dependency : p.getDependencies()){
                        uninstallPackage(dependency, packagesToUninstall, installedPackages);
                    }
                }
            }
        } catch (Exception e){
            //Revert to restore point.
        }
    }
    
    @Override
    public boolean isPackageInstalled(SoftwarePackage p) {
        return installedPackages.contains(p);
    }
}

 

