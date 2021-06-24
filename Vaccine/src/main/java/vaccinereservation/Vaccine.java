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
    private String reservationStatus;
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
      
        //백신 할당 시 Request 보내고 가서 백신있는 병원 찾고 상태값(할당가능/불가능), 수량, 체크
        if(this.status.equals("ASSIGNED")){
            String hospitalStatus = "";
            String hospitalId = "";
            String vaccineStatus =this.status;
            try {
                Thread.currentThread().sleep((long) (300 + Math.random() * 220));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                Map<String,String> res = VaccineApplication.applicationContext
                                                           .getBean(vaccinereservation.external.HospitalService.class)
                                                           .assignHospital(this.getType(),this.getId(),this.getReservationId());

                hospitalStatus = res.get("status")==null?"":res.get("status");
                hospitalId = res.get("hospitalId").equals("-1")?"-1":res.get("hospitalId");
                if(hospitalStatus.equals("EMPTYVACCINE")){
                    //병원에 백신이 없음. 이건 병원이 없어도 가능함 서버는 켜져있어야하지만
                    //예약이 불가능하다라고 ReservationStatus가 Update되어야함
                    vaccineStatus = "CANTAPPLY";
                }else if(hospitalStatus.equals("ASSIGNED")){
                    //할당이 되었다면 백신에 병원 아이디를 줘야 어디 병원에 몇번 백신이 있는지 관리가 될 것.
                    vaccineStatus = "ASSIGNED";
                }
                System.out.println("백신상태 : "+vaccineStatus);
            } catch (Exception e) {
                e.printStackTrace();
            }    
        
            VaccineAssigned vaccineAssigned = new VaccineAssigned();
            vaccineAssigned.setVaccineId(this.id);
            vaccineAssigned.setVaccineName(this.name);
            vaccineAssigned.setVaccineType(this.type);
            vaccineAssigned.setVaccineStatus(vaccineStatus);
            vaccineAssigned.setVaccineDate(this.date);
            vaccineAssigned.setVaccineValidationDate(this.validationDate);
            vaccineAssigned.setReservationId(this.reservationId);
            vaccineAssigned.setReservationStatus(vaccineStatus);
            vaccineAssigned.setHospitalId(Long.valueOf(hospitalId)); 
            vaccineAssigned.publishAfterCommit();

           
        }
        else if(this.status.equals("CANUSE")){ //할당되었다가 취소되면 다시 CANUSE 상태로 
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

    public Long getId()                                             {        return id;                                 }
    public void setId(Long id)                                      {        this.id = id;                              }
    public String getName()                                         {        return name;                               }
    public void setName(String name)                                {        this.name = name;                          }
    public Long getType()                                           {        return type;                               }
    public void setType(Long type)                                  {        this.type = type;                          }
    public String getStatus()                                       {        return status;                             }
    public void setStatus(String status)                            {        this.status = status;                      }
    public Date getDate()                                           {        return date;                               }
    public void setDate(Date date)                                  {        this.date = date;                          }
    public Date getValidationDate()                                 {        return validationDate;                     }
    public void setValidationDate(Date validationDate)              {        this.validationDate = validationDate;      }
    public Long getReservationId()                                  {        return reservationId;                      }
    public void setReservationId(Long reservationId)                {        this.reservationId = reservationId;        }
    public String getReservationStatus()                            {        return reservationStatus;                  }
    public void setReservationStatus(String reservationStatus)      {        this.reservationStatus = reservationStatus;}
    public String getUserName()                                     {        return userName;                           }
    public void setUserName(String userName)                        {        this.userName = userName;                  }
    public String getUserPhone()                                    {        return userPhone;                          }
    public void setUserPhone(String userPhone)                      {        this.userPhone = userPhone;                }
    public Long getHospitalId()                                     {        return hospitalId;                         }
    public void setHospitalId(Long hospitalId)                      {        this.hospitalId = hospitalId;              }

    @Override
	public String toString() {
		return "Vaccine [id=" + id + ", name=" + name + ", type=" + type + ", status=" + status + ", date=" + date
				+ ", validationDate=" + validationDate + ", reservationId=" + reservationId + ", userName=" + userName
				+ ", userPhone=" + userPhone + ", hospitalId=" + hospitalId + "]";
	}


}
