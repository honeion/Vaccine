package vaccinereservation;

public class HospitalAssigned extends AbstractEvent {

    private Long hospitalId;
    private String hospitalName;
    private String hospitalLocation;
    private String hospitalStatus;
    private Long vaccineId;
    private String vaccineStatus;
    private Long reservationId;
    public Long getHospitalId()                                 {        return hospitalId;                             }
    public void setHospitalId(Long hospitalId)                  {        this.hospitalId = hospitalId;                  }         
    public String getHospitalName()                             {        return hospitalName;                           }
    public void setHospitalName(String hospitalName)            {        this.hospitalName = hospitalName;              }
    public String getHospitalLocation()                         {        return hospitalLocation;                       }
    public void setHospitalLocation(String hospitalLocation)    {        this.hospitalLocation = hospitalLocation;      }
    public void setHospitalStatus(String hospitalStatus)        {        this.hospitalStatus = hospitalStatus;          }
    public String getHospitalStatus()                           {        return hospitalStatus;                         }
    public Long getVaccineId()                                  {        return vaccineId;                              }
    public void setVaccineId(Long vaccineId)                    {        this.vaccineId = vaccineId;                    }
    public String getVaccineStatus()                            {        return vaccineStatus;                          }
    public void setVaccineStatus(String vaccineStatus)          {        this.vaccineStatus = vaccineStatus;            }
    public Long getReservationId()                  {        return reservationId;                  }
    public void setReservationId(Long reservationId){        this.reservationId = reservationId;    }
}