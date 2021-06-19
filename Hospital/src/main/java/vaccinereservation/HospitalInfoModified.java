package vaccinereservation;

public class HospitalInfoModified extends AbstractEvent {

    private Long id;

    public HospitalInfoModified(){
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
