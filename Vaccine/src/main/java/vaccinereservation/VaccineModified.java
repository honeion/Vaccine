package vaccinereservation;

public class VaccineModified extends AbstractEvent {

    private Long id;

    public VaccineModified(){
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
