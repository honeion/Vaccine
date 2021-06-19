package vaccinereservation;

public class VaccineReserved extends AbstractEvent {

    private Long reservationId;
    private String reservationStatus;
    private String userName;
    private String userPhone;

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
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
}