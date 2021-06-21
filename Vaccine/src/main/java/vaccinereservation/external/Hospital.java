package vaccinereservation.external;

public class Hospital {

    private Long id;
    private String name;
    private String location;
    private String status;
    private Long vaccineId;
    private Long vaccineType;
    private String vaccineName;
    private Long vaccineCount;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public String getStatus() {
        return status;
    }
    public Long getVaccineId() {
        return vaccineId;
    }
    public void setVaccineId(Long vaccineId) {
        this.vaccineId = vaccineId;
    }
    public Long getVaccineType() {
        return vaccineType;
    }
    public void setVaccineType(Long vaccineType) {
        this.vaccineType = vaccineType;
    }
    public String getVaccineName() {
        return vaccineName;
    }
    public void setVaccineName(String vaccineName) {
        this.vaccineName = vaccineName;
    }
    public Long getVaccineCount() {
        return vaccineCount;
    }
    public void setVaccineCount(Long vaccineCount) {
        this.vaccineCount = vaccineCount;
    }

}
