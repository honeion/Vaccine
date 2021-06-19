package vaccinereservation;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;
import java.util.Date;

@Entity
@Table(name="Hospital_table")
public class Hospital {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String name;
    private String location;
    private Long vaccineId;
    private Long vaccineType;
    private String vaccineName;
    private Long vaccineCount;

    @PostPersist
    public void onPostPersist(){
        HospitalRegistered hospitalRegistered = new HospitalRegistered();
        BeanUtils.copyProperties(this, hospitalRegistered);
        hospitalRegistered.publishAfterCommit();


    }

    @PostUpdate
    public void onPostUpdate(){
        HospitalAssigned hospitalAssigned = new HospitalAssigned();
        BeanUtils.copyProperties(this, hospitalAssigned);
        hospitalAssigned.publishAfterCommit();


        CanceledHospitalAssigned canceledHospitalAssigned = new CanceledHospitalAssigned();
        BeanUtils.copyProperties(this, canceledHospitalAssigned);
        canceledHospitalAssigned.publishAfterCommit();


        HospitalInfoModified hospitalInfoModified = new HospitalInfoModified();
        BeanUtils.copyProperties(this, hospitalInfoModified);
        hospitalInfoModified.publishAfterCommit();


    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    public Long getVaccineId() {
        return vaccineId;
    }

    public void setVaccineId(Long vaccineId) {
        this.vaccineId = vaccineId;
    }
    public Long getVaccineType() {
        return vaccineType;
    }

    public void setVaccineType(Long vaccineType) {
        this.vaccineType = vaccineType;
    }
    public String getVaccineName() {
        return vaccineName;
    }

    public void setVaccineName(String vaccineName) {
        this.vaccineName = vaccineName;
    }
    public Long getVaccineCount() {
        return vaccineCount;
    }

    public void setVaccineCount(Long vaccineCount) {
        this.vaccineCount = vaccineCount;
    }




}
