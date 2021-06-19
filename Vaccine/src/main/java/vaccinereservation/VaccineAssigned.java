package vaccinereservation;

public class VaccineAssigned extends AbstractEvent {

    private Long id;

    public VaccineAssigned(){
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
