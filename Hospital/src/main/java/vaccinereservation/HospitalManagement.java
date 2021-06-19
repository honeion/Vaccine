package vaccinereservation;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;
import java.util.Date;

@Entity
@Table(name="HospitalManagement_table")
public class HospitalManagement {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @PostPersist
    public void onPostPersist(){
        HospitalAssigned hospitalAssigned = new HospitalAssigned();
        BeanUtils.copyProperties(this, hospitalAssigned);
        hospitalAssigned.publishAfterCommit();


        CanceledHospitalAssigned canceledHospitalAssigned = new CanceledHospitalAssigned();
        BeanUtils.copyProperties(this, canceledHospitalAssigned);
        canceledHospitalAssigned.publishAfterCommit();


        HospitalInfoModified hospitalInfoModified = new HospitalInfoModified();
        BeanUtils.copyProperties(this, hospitalInfoModified);
        hospitalInfoModified.publishAfterCommit();


        HospitalRegistered hospitalRegistered = new HospitalRegistered();
        BeanUtils.copyProperties(this, hospitalRegistered);
        hospitalRegistered.publishAfterCommit();


    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }




}
