package vaccinereservation;

public class HospitalAssigned extends AbstractEvent {

    private Long hospitalId;
    private String hospitalName;
    private String hospitalLocation;
    private Long vaccineId;
    private String vaccineStatus;

    public Long getId() {
        return hospitalId;
    }

    public void setId(Long hospitalId) {
        this.hospitalId = hospitalId;
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
    public String getVaccineStatus() {
        return vaccineStatus;
    }

    public void setVaccineStatus(String vaccineStatus) {
        this.vaccineStatus = vaccineStatus;
    }
}