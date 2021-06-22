package vaccinereservation;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;
import java.util.Date;
import java.util.Map;

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
            String hospitalStatus = "";
            String hospitalId = "";
            String vaccineStatus =this.status;
            try {
                //vaccinereservation.external.Hospital hospital = new vaccinereservation.external.Hospital();
                //Application.applicationContext.getBean(vaccinereservation.external.HospitalService.class).assignHospital(hospital);
                Map<String,String> res = VaccineApplication.applicationContext
                                                           .getBean(vaccinereservation.external.HospitalService.class)
                                                           .assignHospital(this.getType(),this.getId());
                System.out.println("#############");
                for(String s : res.keySet()){
                    System.out.println(res.get(s));
                }
                System.out.println("#############");
                hospitalStatus = res.get("status")==null?"":res.get("status");
                hospitalId = res.get("hospitalId")==null?"":res.get("hospitalId");
                if(hospitalStatus.equals("EMPTYVACCINE")){
                    //병원에 백신이 없음. 이건 병원이 없어도 가능함 서버는 켜져있어야하지만
                    //예약이 불가능하다라고 ReservationStatus가 Update되어야함
                    vaccineStatus = "CANTAPPLY";
                }else if(hospitalStatus.equals("ASSIGNED")){
                    //할당이 되었다면 백신에 병원 아이디를 줘야 어디 병원에 몇번 백신이 있는지 관리가 될 것.
                    vaccineStatus = "ASSIGNED2";
                }
                System.out.println("백신상태 : "+vaccineStatus);
                //여기까지 잘되는거 확인 완료했고. 이거를 업데이트를 치고나서 cancel 관련된 부분 확인하고 customerCenter 내용 체크하면 기본 시나리오는 완성
                //내일 새벽까지 또 최대한 해보고 가즈아
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            
            //hospitalId를 비롯한 정보를 가져올 수 있어 - 백신어사인, 캔슬백신어사인 둘 다 있어야함
            //백신에 세팅하려면 이미 업데이트가 된 상태라서 다른방법을 써야해
            //근데 아래쪽 캔슬됐을때 hospitalId가 필요한 거라서 업데이트를 해줘야함
            //그래서 여기서 bean으로 save불러오면 됨. 다시 업데이트를 했을때 assigned랑 canceled를 없애고 
            //업데이트를 2번하는거임. 가져온 거 담아서 save를 하는데. status를 변경함 assgined2 이런식으로 그거를 Reservation의 UpdatedReservationStatus에서 
            //vaccinStatus를 assigned2로 받고 resrvation을 업데이트함 예약관리 백신관리 병원관리 싹 하고나서 고객센터까지 반드시 화욜 밤에 끝낼 것
            //수요일밤에는 각종 명령어 정리하고, db 변경해서 세팅해봐야함.
            VaccineAssigned vaccineAssigned = new VaccineAssigned();
            vaccineAssigned.setVaccineId(this.id);
            vaccineAssigned.setVaccineName(this.name);
            vaccineAssigned.setVaccineType(this.type);
            vaccineAssigned.setVaccineStatus(vaccineStatus);//save 아직 안햇지만 일단은 이걸 줄게요.
            vaccineAssigned.setVaccineDate(this.date);
            vaccineAssigned.setVaccineValidationDate(this.validationDate);
            vaccineAssigned.setReservationId(this.reservationId);
            vaccineAssigned.setHospitalId(Long.valueOf(hospitalId)); //업데이트 안된상태라서 가져온 것으로 줘야함
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
            canceledVaccineAssigned.setHospitalId(this.hospitalId);
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
            vaccineModified.setHospitalId(this.hospitalId);
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

    @Override
	public String toString() {
		return "Vaccine [id=" + id + ", name=" + name + ", type=" + type + ", status=" + status + ", date=" + date
				+ ", validationDate=" + validationDate + ", reservationId=" + reservationId + ", userName=" + userName
				+ ", userPhone=" + userPhone + ", hospitalId=" + hospitalId + "]";
	}


}
