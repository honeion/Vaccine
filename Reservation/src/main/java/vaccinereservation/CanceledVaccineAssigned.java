package vaccinereservation;

public class CanceledVaccineAssigned extends AbstractEvent {

    private Long vaccineId;
    private String vaccineName;
    private Long vaccineType;
    private String vaccineStatus;
    private Long reservationId;
    private String reservationStatus;

    public Long getId() {
        return vaccineId;
    }

    public void setId(Long vaccineId) {
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
    public String getVaccineStatus() {
        return vaccineStatus;
    }

    public void setVaccineStatus(String vaccineStatus) {
        this.vaccineStatus = vaccineStatus;
    }
    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }
    public String getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(String reservationStatus) {
        this.reservationStatus = reservationStatus;
    }
}