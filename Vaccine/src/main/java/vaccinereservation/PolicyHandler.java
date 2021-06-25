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

        Vaccine vaccine;
        boolean check = false;
        
        final Iterable<Vaccine> list = vaccineRepository.findAll();

        for(Vaccine v : list){
            if(v.getStatus().equals("CANUSE")){
                vaccine = v;
                vaccine.setStatus("ASSIGNED");
                vaccine.setReservationId(vaccineReserved.getReservationId());
                vaccine.setUserName(vaccineReserved.getUserName());
                vaccine.setUserPhone(vaccineReserved.getUserPhone());
                vaccine.setHospitalId(vaccineReserved.getHospitalId()); //null로 초기화
                check = true;
                vaccineRepository.save(vaccine);
                break;
            }
        }
        if(!check){
            Vaccine cantVaccine = new Vaccine();
            cantVaccine.setStatus("CANTUSE");
            cantVaccine.setReservationId(vaccineReserved.getReservationId());
            vaccineRepository.save(cantVaccine);
        }
        
            
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverCanceledVaccineReservation_CancelReservation(@Payload CanceledVaccineReservation canceledVaccineReservation){

        if(!canceledVaccineReservation.validate()) return;

        System.out.println("\n\n##### listener CancelReservation : " + canceledVaccineReservation.toJson() + "\n\n");
        Vaccine vaccine = vaccineRepository.findByReservationId(canceledVaccineReservation.getReservationId());
        vaccine.setStatus("CANUSE"); //CANCELD라서 CANUSE로 변경
        vaccine.setReservationStatus("CANCELED"); 
        vaccine.setHospitalId(canceledVaccineReservation.getHospitalId());
        vaccineRepository.save(vaccine);
            
    }


}
