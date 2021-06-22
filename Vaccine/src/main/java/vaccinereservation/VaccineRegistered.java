package vaccinereservation;
import java.util.Date;
public class VaccineRegistered extends AbstractEvent {

    private Long id;
    private String vaccineName;
    private Long vaccineType;
    private String vaccineStatus;
    private Date vaccineDate;
    private Date vaccineValidationDate;
    private Long hospitalId;

    public VaccineRegistered(){
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
    public String getVaccineStatus() {
        return vaccineStatus;
    }

    public void setVaccineStatus(String vaccineStatus) {
        this.vaccineStatus = vaccineStatus;
    }
    public Date getVaccineDate() {
        return vaccineDate;
    }

    public void setVaccineDate(Date vaccineDate) {
        this.vaccineDate = vaccineDate;
    }
    public Date getVaccineValidationDate() {
        return vaccineValidationDate;
    }

    public void setVaccineValidationDate(Date vaccineValidationDate) {
        this.vaccineValidationDate = vaccineValidationDate;
    }
    public Long getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(Long hospitalId) {
        this.hospitalId = hospitalId;
    }
}
