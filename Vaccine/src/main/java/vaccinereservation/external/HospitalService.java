
package vaccinereservation.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Map;

@FeignClient(name="Hospital", url="${api.url.hospital}")//#"http://Hospital:8080")
public interface HospitalService {

    @RequestMapping(method= RequestMethod.GET, path="/hospitals/assignHospital")
    public Map<String,String> assignHospital(@RequestParam("vaccineType") Long vaccineType, @RequestParam("vaccineId") Long vaccineId, @RequestParam("reservationId") Long reservationId);
}