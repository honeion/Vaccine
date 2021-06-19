package vaccinereservation;

import vaccinereservation.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PolicyHandler{
    @Autowired ReservationManagementRepository reservationManagementRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverCanceledHospitalAssigned_UpdateedReservationStatus(@Payload CanceledHospitalAssigned canceledHospitalAssigned){

        if(!canceledHospitalAssigned.validate()) return;

        System.out.println("\n\n##### listener UpdateedReservationStatus : " + canceledHospitalAssigned.toJson() + "\n\n");

        // Sample Logic //
        ReservationManagement reservationManagement = new ReservationManagement();
        reservationManagementRepository.save(reservationManagement);
            
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverHospitalAssigned_UpdateedReservationStatus(@Payload HospitalAssigned hospitalAssigned){

        if(!hospitalAssigned.validate()) return;

        System.out.println("\n\n##### listener UpdateedReservationStatus : " + hospitalAssigned.toJson() + "\n\n");

        // Sample Logic //
        ReservationManagement reservationManagement = new ReservationManagement();
        reservationManagementRepository.save(reservationManagement);
            
    }


    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString){}


}
