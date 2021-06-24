package vaccinereservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
 @RestController
 public class HospitalController {

    @Autowired HospitalRepository hospitalRepository;

    @RequestMapping(value = "/hospitals/assignHospital",
                    method = RequestMethod.GET,
                    produces = "application/json;charset=UTF-8")
    public Map<String,String> assignHospital(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String,String> res = new HashMap<String,String>();
        Long vaccineType = Long.valueOf(request.getParameter("vaccineType"));
        Long vaccineId = Long.valueOf(request.getParameter("vaccineId"));
        Long reservationId = Long.valueOf(request.getParameter("reservationId"));
        String status = "";
        String data = "";
        Long id = -1L;
        try {
            Thread.currentThread().sleep((long) (600 + Math.random() * 250));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Iterable<Hospital> hosOptional = hospitalRepository.findAll();
        for(Hospital hospital : hosOptional){
            System.out.println("["+hospital+"]");
            if(hospital.getVaccineType() == vaccineType){
                Long value = hospital.getVaccineCount();
                if(value > 0){
                    status = "ASSIGNED";
                    id = hospital.getId();
                    hospital.setStatus(status);
                    hospital.setVaccineId(vaccineId);
                    hospital.setVaccineCount(value-1);
                    hospital.setReservationId(reservationId);
                    data = hospital.toString();
                    hospitalRepository.save(hospital);
                    break;
                } 
            }
        };
        if(status.equals("")){
            status = "EMPTYVACCINE";
        }
        res.put("status",status);
        res.put("data",data);
        res.put("hospitalId",id+"");

        return res;
    }

 }
