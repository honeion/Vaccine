
package vaccinereservation.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Map;
import java.util.Date;

@FeignClient(name="Hospital", url="http://localhost:8083")//"http://Hospital:8080")
public interface HospitalService {

    // @RequestMapping(method= RequestMethod.GET, path="/hospitals")
    // public void assignHospital(@RequestBody Hospital hospital);
    @RequestMapping(method= RequestMethod.GET, path="/hospitals/assignHospital")
    public Map<String,String> assignHospital(@RequestParam("vaccineType") Long vaccineType, @RequestParam("vaccineId") Long vaccineId, @RequestParam("reservationId") Long reservationId);
}