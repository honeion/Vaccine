package vaccinereservation;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;
import java.util.Date;

@Entity
@Table(name="VaccineManagement_table")
public class VaccineManagement {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @PostPersist
    public void onPostPersist(){
        VaccineAssigned vaccineAssigned = new VaccineAssigned();
        BeanUtils.copyProperties(this, vaccineAssigned);
        vaccineAssigned.publishAfterCommit();

        //Following code causes dependency to external APIs
        // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.

        vaccinereservation.external.HospitalManagement hospitalManagement = new vaccinereservation.external.HospitalManagement();
        // mappings goes here
        Application.applicationContext.getBean(vaccinereservation.external.HospitalManagementService.class)
            .assignHospital(hospitalManagement);


        CanceledVaccineAssigned canceledVaccineAssigned = new CanceledVaccineAssigned();
        BeanUtils.copyProperties(this, canceledVaccineAssigned);
        canceledVaccineAssigned.publishAfterCommit();


        VaccineRegistered vaccineRegistered = new VaccineRegistered();
        BeanUtils.copyProperties(this, vaccineRegistered);
        vaccineRegistered.publishAfterCommit();


        VaccineModified vaccineModified = new VaccineModified();
        BeanUtils.copyProperties(this, vaccineModified);
        vaccineModified.publishAfterCommit();


    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }




}
