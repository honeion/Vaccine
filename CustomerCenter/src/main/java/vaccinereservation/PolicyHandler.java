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

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverHospitalAssigned_SendAlarm(@Payload HospitalAssigned hospitalAssigned){

        if(!hospitalAssigned.validate()) return;

        System.out.println("\n\n##### listener SendAlarm : " + hospitalAssigned.toJson() + "\n\n");

        // Sample Logic //
            
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverCanceledHospitalAssigned_SendAlarm(@Payload CanceledHospitalAssigned canceledHospitalAssigned){

        if(!canceledHospitalAssigned.validate()) return;

        System.out.println("\n\n##### listener SendAlarm : " + canceledHospitalAssigned.toJson() + "\n\n");

        // Sample Logic //
            
    }


    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString){}


}
