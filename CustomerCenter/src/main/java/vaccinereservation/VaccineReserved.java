package vaccinereservation;
import java.util.Date;
public class VaccineReserved extends AbstractEvent {

    private Long reservationId;
    private String reservationStatus;
    private Date reservationDate;
    private String userName;
    private String userPhone;
    private Long hospitalId;

    public Long getReservationId()                              {        return reservationId;                          }
    public void setReservationId(Long reservationId)            {        this.reservationId = reservationId;            }
    public String getReservationStatus()                        {        return reservationStatus;                      }
    public void setReservationStatus(String reservationStatus)  {        this.reservationStatus = reservationStatus;    }
    public Date getReservationDate()                            {        return reservationDate;                        }
    public void setReservationDate(Date reservationDate)        {        this.reservationDate = reservationDate;        }
    public String getUserName()                                 {        return userName;                               }  
    public void setUserName(String userName)                    {        this.userName = userName;                      }
    public String getUserPhone()                                {        return userPhone;                              }
    public void setUserPhone(String userPhone)                  {        this.userPhone = userPhone;                    }
    public Long getHospitalId()                                 {        return hospitalId;                             }
    public void setHospitalId(Long hospitalId)                  {        this.hospitalId = hospitalId;                  }
}