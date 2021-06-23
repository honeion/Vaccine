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
    private String status;  //병원의 상태는 오로지 어떤 이벤트로 인한 것인지 판단하기 위함
                            //PERSIST / MODIFIED / ASSIGNED / CANCELED /  EMPTYVACCINE (모든 병원에 사용 가능한 백신이 없음)
    private Long vaccineId;
    private Long vaccineType;
    private String vaccineName;
    private Long vaccineCount;

    private Long reservationId;

    @PostPersist
    public void onPostPersist(){
        HospitalRegistered hospitalRegistered = new HospitalRegistered();
        hospitalRegistered.setHospitalName(this.name);
        hospitalRegistered.setHospitalLocation(this.location);
        hospitalRegistered.setHospitalStatus(this.status);
        hospitalRegistered.setVaccineId(this.vaccineId);
        hospitalRegistered.setVaccineType(this.vaccineType);
        hospitalRegistered.setVaccineName(this.vaccineName);
        hospitalRegistered.setVaccineCount(this.vaccineCount);
        hospitalRegistered.publishAfterCommit();
    }

    @PostUpdate
    public void onPostUpdate(){

        if(this.status.equals("ASSIGNED")){
            HospitalAssigned hospitalAssigned = new HospitalAssigned();
            hospitalAssigned.setHospitalId(this.id);
            hospitalAssigned.setHospitalName(this.name);
            hospitalAssigned.setHospitalLocation(this.location);
            hospitalAssigned.setHospitalStatus("ASSIGNED");
            hospitalAssigned.setVaccineId(this.vaccineId); 
            hospitalAssigned.setVaccineStatus("ASSIGNED");
            hospitalAssigned.setReservationId(this.reservationId); 
            hospitalAssigned.publishAfterCommit();
        }
        // 백신이 취소된 CancelAssignedHopital인 경우
        else if(this.status.equals("CANCELED")){
            CanceledHospitalAssigned canceledHospitalAssigned = new CanceledHospitalAssigned();
            canceledHospitalAssigned.setHospitalId(this.id);
            canceledHospitalAssigned.setHospitalName(this.name);
            canceledHospitalAssigned.setHospitalLocation(this.location);
            canceledHospitalAssigned.setHospitalStatus("CANCELED");
            canceledHospitalAssigned.setVaccineId(this.vaccineId); 
            canceledHospitalAssigned.setVaccineStatus("CANCELED");
            canceledHospitalAssigned.publishAfterCommit();
        }
        else{ //MODIFIED
            HospitalInfoModified hospitalInfoModified = new HospitalInfoModified();
            BeanUtils.copyProperties(this, hospitalInfoModified);
            hospitalInfoModified.setHospitalStatus("MODIFIED");
            hospitalInfoModified.publishAfterCommit();
        }

    }

    public Long getId()                             {        return id;                             }
    public void setId(Long id)                      {        this.id = id;                          }
    public String getName()                         {        return name;                           }
    public void setName(String name)                {        this.name = name;                      }
    public String getLocation()                     {        return location;                       }
    public void setLocation(String location)        {        this.location = location;              }
    public String getStatus()                       {        return status;                         }
    public void setStatus(String status)            {        this.status = status;                  }
    public Long getVaccineId()                      {        return vaccineId;                      }
    public void setVaccineId(Long vaccineId)        {        this.vaccineId = vaccineId;            }
    public Long getVaccineType()                    {        return vaccineType;                    }
    public void setVaccineType(Long vaccineType)    {        this.vaccineType = vaccineType;        }    
    public String getVaccineName()                  {        return vaccineName;                    }
    public void setVaccineName(String vaccineName)  {        this.vaccineName = vaccineName;        }   
    public Long getVaccineCount()                   {        return vaccineCount;                   }
    public void setVaccineCount(Long vaccineCount)  {        this.vaccineCount = vaccineCount;      }
    public Long getReservationId()                  {        return reservationId;                  }
    public void setReservationId(Long reservationId){        this.reservationId = reservationId;    }

    @Override
	public String toString() {
		return "Hospital [id=" + id + ", name=" + name + ", location=" + location + ", vaccineId=" + vaccineId
				+ ", vaccineType=" + vaccineType + ", vaccineName=" + vaccineName + ", vaccineCount=" + vaccineCount
				+ "]";
	}


}
