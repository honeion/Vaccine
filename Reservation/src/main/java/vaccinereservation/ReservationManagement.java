package vaccinereservation;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;
import java.util.Date;

@Entity
@Table(name="ReservationManagement_table")
public class ReservationManagement {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @PostPersist
    public void onPostPersist(){
        VaccineReserved vaccineReserved = new VaccineReserved();
        BeanUtils.copyProperties(this, vaccineReserved);
        vaccineReserved.publishAfterCommit();


        CanceledVaccineReservation canceledVaccineReservation = new CanceledVaccineReservation();
        BeanUtils.copyProperties(this, canceledVaccineReservation);
        canceledVaccineReservation.publishAfterCommit();


    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }




}
