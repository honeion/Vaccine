package vaccinereservation;

public class CanceledVaccineReservation extends AbstractEvent {
    
    private Long reservationId;
    private String reservationStatus;
    private Long vaccineId;
    private Long hospitalId;
    
    public Long getReservationId()                              {        return reservationId;    }
    public void setReservationId(Long reservationId)            {        this.reservationId = reservationId;    }
    public String getReservationStatus()                        {        return reservationStatus;    }
    public void setReservationStatus(String reservationStatus)  {        this.reservationStatus = reservationStatus;    }
    public Long getVaccineId()                                  {        return vaccineId;                              }
    public void setVaccineId(Long vaccineId)                    {        this.vaccineId = vaccineId;                    }
    public Long getHospitalId()                                 {        return hospitalId;                             }
    public void setHospitalId(Long hospitalId)                  {        this.hospitalId = hospitalId;                  } 
}