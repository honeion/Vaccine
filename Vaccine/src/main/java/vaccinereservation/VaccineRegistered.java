package vaccinereservation;

public class VaccineRegistered extends AbstractEvent {

    private Long id;

    public VaccineRegistered(){
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
