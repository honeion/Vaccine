package vaccinereservation;

import javax.persistence.*;
import java.util.List;
import java.util.Date;
@Entity
@Table(name="ReservationStatus_table")
public class ReservationStatus {

        @Id
        @GeneratedValue(strategy=GenerationType.AUTO)
        private Long id;
        private Long reservationId;
        private Long vaccineId;
        private Long hospitalId;
        private String reservationStatus;
        private Date reservationDate;
        private String vaccineName;
        private Date vaccineDate;
        private Date vaccineValidationDate;
        private String hospitalName;
        private String hospitalLocation;
        private String userName;
        private String userPhone;


        public Long getId()                                             {            return id;                                                 }            
        public void setId(Long id)                                      {            this.id = id;                                              }
        public Long getReservationId()                                  {            return reservationId;                                      }              
        public void setReservationId(Long reservationId)                {            this.reservationId = reservationId;                        }
        public Long getVaccineId()                                      {            return vaccineId;                                          }
        public void setVaccineId(Long vaccineId)                        {            this.vaccineId = vaccineId;                                }
        public Long getHospitalId()                                     {            return hospitalId;                                         }
        public void setHospitalId(Long hospitalId)                      {            this.hospitalId = hospitalId;                              }
        public String getReservationStatus()                            {            return reservationStatus;                                  }
        public void setReservationStatus(String reservationStatus)      {            this.reservationStatus = reservationStatus;                }
        public Date getReservationDate()                                {            return reservationDate;                                    }
        public void setReservationDate(Date reservationDate)            {            this.reservationDate = reservationDate;                    }
        public String getVaccineName()                                  {            return vaccineName;                                        }
        public void setVaccineName(String vaccineName)                  {            this.vaccineName = vaccineName;                            }
        public Date getVaccineDate()                                    {            return vaccineDate;                                        }
        public void setVaccineDate(Date vaccineDate)                    {            this.vaccineDate = vaccineDate;                            }
        public Date getVaccineValidationDate()                          {            return vaccineValidationDate;                              }
        public void setVaccineValidationDate(Date vaccineValidationDate){            this.vaccineValidationDate = vaccineValidationDate;        }
        public String getHospitalName()                                 {            return hospitalName;                                       }
        public void setHospitalName(String hospitalName)                {            this.hospitalName = hospitalName;                          }
        public String getHospitalLocation()                             {            return hospitalLocation;                                   }
        public void setHospitalLocation(String hospitalLocation)        {            this.hospitalLocation = hospitalLocation;                  }
        public String getUserName()                                     {            return userName;                                           }
        public void setUserName(String userName)                        {            this.userName = userName;                                  }         
        public String getUserPhone()                                    {            return userPhone;                                          }
        public void setUserPhone(String userPhone)                      {            this.userPhone = userPhone;                                }

}
