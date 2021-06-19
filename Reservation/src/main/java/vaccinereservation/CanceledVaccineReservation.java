package vaccinereservation;

public class CanceledVaccineReservation extends AbstractEvent {

    private Long reservationId;
    private String reservationStatus;

    public Long getId() {
        return reservationId;
    }

    public void setId(Long reservationId) {
        this.reservationId = reservationId;
    }
    public String getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(String reservationStatus) {
        this.reservationStatus = reservationStatus;
    }
}