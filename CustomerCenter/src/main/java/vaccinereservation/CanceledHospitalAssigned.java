
package vaccinereservation;

public class CanceledHospitalAssigned extends AbstractEvent {

    private Long hospitalId;
    private String hospitalName;
    private String hospitalLocation;
    private String hospitalStatus;
    private Long vaccineId;
    private String vaccineStatus;

    public Long getHospitalId()                             {        return hospitalId;                         }
    public void setHospitalId(Long hospitalId)              {        this.hospitalId = hospitalId;              }
    public String getHospitalName()                         {        return hospitalName;                       }
    public void setHospitalName(String hospitalName)        {        this.hospitalName = hospitalName;          }
    public String getHospitalLocation()                     {        return hospitalLocation;                   }
    public void setHospitalLocation(String hospitalLocation){        this.hospitalLocation = hospitalLocation;  }
    public String getHospitalStatus()                       {        return hospitalStatus;                     }
    public void setHospitalStatus(String hospitalStatus)    {        this.hospitalStatus = hospitalStatus;      }
    public Long getVaccineId()                              {        return vaccineId;                          }
    public void setVaccineId(Long vaccineId)                {        this.vaccineId = vaccineId;                }
    public String getVaccineStatus()                        {        return vaccineStatus;                      }
    public void setVaccineStatus(String vaccineStatus)      {        this.vaccineStatus = vaccineStatus;        }
}

