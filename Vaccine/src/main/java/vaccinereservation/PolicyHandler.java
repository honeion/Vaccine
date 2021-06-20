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
        Vaccine vaccine;
        boolean check = false;

        //여기서 백신 CAN USE인 애 찾고 있으면 업뎃 없으면 예약불가로 만들고 업데이트 되니까 post update에서 처리하면 됨
        
        final Iterable<Vaccine> list = vaccineRepository.findAll();

        for(Vaccine v : list){
            if(v.getStatus().equals("CANUSE")){
                vaccine = v;
                vaccine.setVaccineStatus("ASSIGNED");
                vaccine.setReservationId(vaccineReserved.getReservationId());
                check = true;
                vaccineRepository.save(vaccine);
                break;
            }
        }
        if(!check){
            Vaccine cantVaccine = new Vaccine();
            cantVaccine.setVaccineStatus("CANTUSE");
            vaccineRepository.save(cantVaccine);
            System.out.println("######################");
            System.out.println("백신부족으로 예약불가");
            System.out.println("######################");
        }
        
            
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverCanceledVaccineReservation_CancelReservation(@Payload CanceledVaccineReservation canceledVaccineReservation){

        if(!canceledVaccineReservation.validate()) return;

        System.out.println("\n\n##### listener CancelReservation : " + canceledVaccineReservation.toJson() + "\n\n");
        // 여기서는 Reseravtion Id, STATUS : CANCELED 가 옴 업데이트를 
        // Sample Logic //
        Vaccine vaccine = vaccineRepository.findByReservationId(canceledVaccineReservation.getReservationId());
        vaccine.setStatus("CANCELED");
        vaccineRepository.save(vaccine);
            
    }


}
