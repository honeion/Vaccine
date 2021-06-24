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
    private String status;  //예약신청 APPLYED / 예약취소 CANCELED / 예약완료 COMPLETED / 예약불가 CANTAPPLY
    private String userName;
    private String userPhone;
    private Long vaccineId;
    private Long hospitalId;

    @PostPersist
    public void onPostPersist(){
        VaccineReserved vaccineReserved = new VaccineReserved();
        vaccineReserved.setReservationId(this.id);
        vaccineReserved.setReservationStatus(this.status);
        vaccineReserved.setUserName(this.userName);
        vaccineReserved.setUserPhone(this.userPhone);
        vaccineReserved.setHospitalId(this.hospitalId);
        vaccineReserved.setReservationDate(this.date);
        vaccineReserved.publishAfterCommit();
    }

    @PostUpdate
    public void onPostUpdate(){
        if(this.getStatus().equals("CANCELED")){
            CanceledVaccineReservation canceledVaccineReservation = new CanceledVaccineReservation();
            canceledVaccineReservation.setReservationId(this.id);
            canceledVaccineReservation.setReservationStatus(this.status);
            canceledVaccineReservation.setVaccineId(this.vaccineId);
            canceledVaccineReservation.setHospitalId(this.hospitalId);
            canceledVaccineReservation.publishAfterCommit();
        }
    }

    public Long getId()                         {        return id;                         }
    public void setId(Long id)                  {        this.id = id;                      }     
    public Date getDate()                       {        return date;                       }
    public void setDate(Date date)              {        this.date = date;                  }
    public String getStatus()                   {        return status;                     }
    public void setStatus(String status)        {        this.status = status;              }
    public String getUserName()                 {        return userName;                   }
    public void setUserName(String userName)    {        this.userName = userName;          }
    public String getUserPhone()                {        return userPhone;                  }
    public void setUserPhone(String userPhone)  {        this.userPhone = userPhone;        }
    public Long getVaccineId()                  {        return vaccineId;                  }
    public void setVaccineId(Long vaccineId)    {        this.vaccineId = vaccineId;        }
    public Long getHospitalId()                 {        return hospitalId;                 }
    public void setHospitalId(Long hospitalId)  {        this.hospitalId = hospitalId;      }

    @Override
	public String toString() {
		return "Reservation [id=" + id + ", date=" + date + ", status=" + status + ", userName=" + userName
				+ ", userPhone=" + userPhone + ", vaccineId=" + vaccineId + ", hospitalId=" + hospitalId + "]";
	}

}
