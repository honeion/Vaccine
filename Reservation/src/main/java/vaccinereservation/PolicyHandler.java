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
    @Autowired ReservationRepository reservationRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverCanceledVaccineAssigned_UpdateedReservationStatus(@Payload CanceledVaccineAssigned canceledVaccineAssigned){

        if(!canceledVaccineAssigned.validate()) return;

        System.out.println("\n\n##### listener UpdateedReservationStatus : " + canceledVaccineAssigned.toJson() + "\n\n");

        // Sample Logic //
        Reservation reservation = new Reservation();
        reservationRepository.save(reservation);
            
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverVaccineAssigned_UpdateedReservationStatus(@Payload VaccineAssigned vaccineAssigned){

        if(!vaccineAssigned.validate()) return;

        System.out.println("\n\n##### listener UpdateedReservationStatus : " + vaccineAssigned.toJson() + "\n\n");

        // Sample Logic //
        Reservation reservation = new Reservation();
        reservationRepository.save(reservation);
            
    }


    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString){}


}
