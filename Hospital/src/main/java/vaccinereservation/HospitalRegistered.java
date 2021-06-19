package vaccinereservation;

public class HospitalRegistered extends AbstractEvent {

    private Long id;

    public HospitalRegistered(){
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
