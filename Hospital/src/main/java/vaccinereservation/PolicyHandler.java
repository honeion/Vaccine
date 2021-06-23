package vaccinereservation;

import vaccinereservation.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import java.util.Optional;
@Service
public class PolicyHandler{
    @Autowired HospitalRepository hospitalRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverCanceledVaccineAssigned_CancelAssignedHospital(@Payload CanceledVaccineAssigned canceledVaccineAssigned){

        if(!canceledVaccineAssigned.validate()) return;
        //상태가 CANCELED일때만 처리를 해줘. 업데이트를 해주는데 hospital의 상태가 canceled가 되도록
        System.out.println("\n\n##### listener CancelAssignedHospital : " + canceledVaccineAssigned.toJson() + "\n\n");
        Long hospitalId = canceledVaccineAssigned.getHospitalId()==null?-1L:canceledVaccineAssigned.getHospitalId();
        if(hospitalId != -1L){
            Optional<Hospital> optional = hospitalRepository.findById(hospitalId); //어떤 병원에 할당된 백신인지 찾아야하므로 hospitalId를 줘야함
            Hospital hospital = optional.get();
            hospital.setStatus("CANCELED");
            hospital.setVaccineCount(hospital.getVaccineCount()+1);
            hospitalRepository.save(hospital);
        }
                   
    }

}
