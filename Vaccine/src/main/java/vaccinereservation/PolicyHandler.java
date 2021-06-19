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
    @Autowired VaccineRepository vaccineRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverVaccineReserved_ReserveVaccine(@Payload VaccineReserved vaccineReserved){

        if(!vaccineReserved.validate()) return;

        System.out.println("\n\n##### listener ReserveVaccine : " + vaccineReserved.toJson() + "\n\n");

        // Sample Logic //
        Vaccine vaccine = new Vaccine();
        vaccineRepository.save(vaccine);
            
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverCanceledVaccineReservation_CancelReservation(@Payload CanceledVaccineReservation canceledVaccineReservation){

        if(!canceledVaccineReservation.validate()) return;

        System.out.println("\n\n##### listener CancelReservation : " + canceledVaccineReservation.toJson() + "\n\n");

        // Sample Logic //
        Vaccine vaccine = new Vaccine();
        vaccineRepository.save(vaccine);
            
    }


    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString){}


}
