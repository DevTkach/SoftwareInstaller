import java.nio.file.Path;
import java.util.Set;


//Software Package object - attributes
class SoftwarePackage { 
	private String name; 
	private Path path; 
	private String information; 
	private Set<SoftwarePackage> dependencies; 

	public SoftwarePackage(String name, Path path, String information, Set<SoftwarePackage> dependencies) { 
		this.name = name; 
		this.path = path; 
		this.information = information; 
		this.dependencies = dependencies; 
	} 

	public String getName() { 
		return this.name; 
	} 
	
	public Path getPath() { 
		return this.path; 
	} 

	public Set<SoftwarePackage> getDependencies() { 
		return this.dependencies; 
	} 
	
	@Override
	public boolean equals(Object obj) {
	    if (this == obj) return true;
	    if (obj == null || getClass() != obj.getClass()) return false;
	    SoftwarePackage other = (SoftwarePackage) obj;
	    return name != null && name.equals(other.name);
	}
	
	@Override
	public int hashCode() {
	    return name == null ? 0 : name.hashCode();
	}


} 