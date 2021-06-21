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
        String status = "";
        Map<String,String> res = new HashMap<String,String>();
        Long vaccineType = Long.valueOf(request.getParameter("vaccineType"));
        Long vaccineId = Long.valueOf(request.getParameter("vaccineId"));
        Iterable<Hospital> hosOptional = hospitalRepository.findAll();
        hosOptional.forEach(hospital->{
            System.out.println("["+hospital+"]");
            if(hospital.getType() == vaccineType){
                Long value = hospital.getVaccineCount();
                if(value > 0){
                    status = "ASSIGNED";
                    hospital.setStatus(status);
                    hospital.setVaccineId(vaccineId);
                    hospital.setVaccineCount(value-1);
                    hospitalRepository.save(hospital);
                } else{
                    status = "EMPTY";
                }
                break;    
            }
        });
        if(status.equals("")){
            status = "NOVACCINE";
        }
        res.put("status",status);
        res.put("data",hospital.toString());

        return res;
    }

 }
