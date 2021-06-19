package vaccinereservation;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;
import java.util.Date;

@Entity
@Table(name="Reservation_table")
public class Reservation {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Date date;
    private String status;
    private String userName;
    private String userPhone;
    private Long vaccineId;
    private Long hospitalId;

    @PostPersist
    public void onPostPersist(){
        VaccineReserved vaccineReserved = new VaccineReserved();
        BeanUtils.copyProperties(this, vaccineReserved);
        vaccineReserved.publishAfterCommit();


    }

    @PostUpdate
    public void onPostUpdate(){
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
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
    public Long getVaccineId() {
        return vaccineId;
    }

    public void setVaccineId(Long vaccineId) {
        this.vaccineId = vaccineId;
    }
    public Long getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(Long hospitalId) {
        this.hospitalId = hospitalId;
    }




}
