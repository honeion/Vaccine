package vaccinereservation;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;
import java.util.Date;

@Entity
@Table(name="Vaccine_table")
public class Vaccine {

    private Long id;
    private String name;
    private Long type;
    private String status;
    private Date date;
    private Date validationDate;
    private Long reservationId;
    private String userName;
    private String userPhone;
    private Long hospitalId;

    @PostPersist
    public void onPostPersist(){
        VaccineRegistered vaccineRegistered = new VaccineRegistered();
        BeanUtils.copyProperties(this, vaccineRegistered);
        vaccineRegistered.publishAfterCommit();


    }

    @PostUpdate
    public void onPostUpdate(){
        VaccineAssigned vaccineAssigned = new VaccineAssigned();
        BeanUtils.copyProperties(this, vaccineAssigned);
        vaccineAssigned.publishAfterCommit();

        //Following code causes dependency to external APIs
        // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.

        vaccinereservation.external.Hospital hospital = new vaccinereservation.external.Hospital();
        // mappings goes here
        Application.applicationContext.getBean(vaccinereservation.external.HospitalService.class)
            .assignHospital(hospital);


        CanceledVaccineAssigned canceledVaccineAssigned = new CanceledVaccineAssigned();
        BeanUtils.copyProperties(this, canceledVaccineAssigned);
        canceledVaccineAssigned.publishAfterCommit();


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
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    public Date getValidationDate() {
        return validationDate;
    }

    public void setValidationDate(Date validationDate) {
        this.validationDate = validationDate;
    }
    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
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
    public Long getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(Long hospitalId) {
        this.hospitalId = hospitalId;
    }




}
