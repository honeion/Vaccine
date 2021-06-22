package vaccinereservation;

import vaccinereservation.config.kafka.KafkaProcessor;

import java.util.Optional;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PolicyHandler{
    @Autowired ReservationRepository reservationRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverCanceledVaccineAssigned_UpdateedReservationStatus(@Payload CanceledVaccineAssigned canceledVaccineAssigned){

        if(!canceledVaccineAssigned.validate()) return;
        System.out.println("\n\n##### listener UpdateedReservationStatus : " + canceledVaccineAssigned.toJson() + "\n\n");
        Optional<Reservation> optional = reservationRepository.findById(canceledVaccineAssigned.getReservationId());
        Reservation reservation = optional.get();
        System.out.println("--------------------------------");
        System.out.println("Reservation : "+reservation);
        System.out.println("--------------------------------");
        //여기도 상태 나눠서 CANTAPPLY랑 CANCELED로 나눠서 처리
        if(canceledVaccineAssigned.getReservationStatus().equals("CANTAPPLY")){
            //신청 불가 - 이미 불가인 상태라서.
            reservation.setStatus("CANTAPPLY");
        }
        else{
            reservation.setStatus("CANCELED");
        }
        reservationRepository.save(reservation);
            
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverVaccineAssigned_UpdateedReservationStatus(@Payload VaccineAssigned vaccineAssigned){

        if(!vaccineAssigned.validate()) return;

        System.out.println("\n\n##### listener UpdateedReservationStatus : " + vaccineAssigned.toJson() + "\n\n");
        //여기서 병원에 백신이 없어서 할당을 할 수 없다. 라고 오면
        
        Optional<Reservation> optional = reservationRepository.findById(vaccineAssigned.getReservationId());
        Reservation reservation = optional.get();
        //할당 받은 경우, 백신 및 병원 정보 업데이트
        if(vaccineAssigned.getVaccineStatus().equals("ASSIGNED2")){
            reservation.setStatus("COMPLETED");
            reservation.setVaccineId(vaccineAssigned.getVaccineId());
            reservation.setHospitalId(vaccineAssigned.getHospitalId());
        }else if(vaccineAssigned.getVaccineStatus().equals("CANTAPPLY")){
            reservation.setStatus("CANTAPPLY");
        }
        reservationRepository.save(reservation);
            
    }


}
