class SoftwarePackage {
    private String name;
    private Path path;
    private String information;
    private Set<SoftwarePackage> dependencies;

    public SoftwarePackage(
        String name, 
        Path path, 
        String information, 
        Set<SoftwarePackage> dependencies
    ){
        this.name = name;
        this.path = path;
        this.information = information;
        this.dependencies = dependencies;
    }

    
        

}
