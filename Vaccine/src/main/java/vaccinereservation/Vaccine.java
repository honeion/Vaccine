package vaccinereservation;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;
import java.util.Date;

@Entity
@Table(name="Vaccine_table")
public class Vaccine {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String name;
    private Long type;
    private String status; // 사용가능 CANUSE / 할당완료 ASSIGNED  / 사용완료 USED / 할당취소 CANCELED / 할당불가 CANTUSE 
    private Date date;
    private Date validationDate;
    private Long reservationId;
    private String userName;
    private String userPhone;
    private Long hospitalId;

    @PostPersist
    public void onPostPersist(){
        if(this.status.equals("CANTUSE")){
            // 백신 할당 취소 시
            CanceledVaccineAssigned canceledVaccineAssigned = new CanceledVaccineAssigned();
            canceledVaccineAssigned.setVaccineId(this.id);
            //canceledVaccineAssigned.setVaccineStatus("CANTUSE");
            canceledVaccineAssigned.setReservationId(this.reservationId);
            canceledVaccineAssigned.setReservationStatus("CANTAPPLY");
            canceledVaccineAssigned.publishAfterCommit();
        }
        else{
            if(!this.getStatus().equals("CANUSE")) setStatus("CANUSE");
            VaccineRegistered vaccineRegistered = new VaccineRegistered();
            vaccineRegistered.setId(this.id);
            vaccineRegistered.setVaccineName(this.name);
            vaccineRegistered.setVaccineType(this.type);
            vaccineRegistered.setVaccineStatus(this.status);
            vaccineRegistered.setVaccineDate(this.date);
            vaccineRegistered.setVaccineValidationDate(this.validationDate);
            vaccineRegistered.publishAfterCommit();
        }
    }

    @PostUpdate
    public void onPostUpdate(){
        //여기 상태별로 if else하면 되겠다.
        
        //Following code causes dependency to external APIs
        // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.
        //백신 할당 시 Request 보내고 가서 백신있는 병원 찾고 상태값(할당가능/불가능), 수량, 체크해서 가져오고
        if(this.status.equals("ASSIGNED")){
            try {
                vaccinereservation.external.Hospital hospital = new vaccinereservation.external.Hospital();
                //Application.applicationContext.getBean(vaccinereservation.external.HospitalService.class).assignHospital(hospital);

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            

            VaccineAssigned vaccineAssigned = new VaccineAssigned();
            BeanUtils.copyProperties(this, vaccineAssigned);
            vaccineAssigned.publishAfterCommit();
        }
        else if(this.status.equals("CANCELED")){
            // 백신 할당 취소 시
            CanceledVaccineAssigned canceledVaccineAssigned = new CanceledVaccineAssigned();
            canceledVaccineAssigned.setVaccineId(this.id);
            canceledVaccineAssigned.setVaccineName(this.name);
            canceledVaccineAssigned.setVaccineType(this.type);
            canceledVaccineAssigned.setVaccineStatus("CANUSE");
            canceledVaccineAssigned.setReservationId(this.reservationId);
            canceledVaccineAssigned.setReservationStatus("CANCELED");
            canceledVaccineAssigned.publishAfterCommit();
        }
        else { // this.status.equals("MODIFIED") 이거는 따로 업데이트 하는 것이므로 현재는 상태값으로 판단 x
            // 백신 정보 수정 시
            VaccineModified vaccineModified = new VaccineModified();
            vaccineModified.setId(this.id);
            vaccineModified.setVaccineName(this.name);
            vaccineModified.setVaccineType(this.type);
            vaccineModified.setVaccineStatus(this.status);
            vaccineModified.setVaccineDate(this.date);
            vaccineModified.setVaccineValidationDate(this.validationDate);
            vaccineModified.publishAfterCommit();
        }
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
