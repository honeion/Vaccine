package vaccinereservation;

public class HospitalRegistered extends AbstractEvent {

    private Long id;
    private String hospitalName;
    private String hospitalLocation;
    private Long vaccineId;
    private String vaccineName;
    private Long vaccineType;
    private Long vaccineCount;

    public HospitalRegistered(){
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }
    public String getHospitalLocation() {
        return hospitalLocation;
    }

    public void setHospitalLocation(String hospitalLocation) {
        this.hospitalLocation = hospitalLocation;
    }
    public Long getVaccineId() {
        return vaccineId;
    }

    public void setVaccineId(Long vaccineId) {
        this.vaccineId = vaccineId;
    }
    public String getVaccineName() {
        return vaccineName;
    }

    public void setVaccineName(String vaccineName) {
        this.vaccineName = vaccineName;
    }
    public Long getVaccineType() {
        return vaccineType;
    }

    public void setVaccineType(Long vaccineType) {
        this.vaccineType = vaccineType;
    }
    public Long getVaccineCount() {
        return vaccineCount;
    }

    public void setVaccineCount(Long vaccineCount) {
        this.vaccineCount = vaccineCount;
    }
}
