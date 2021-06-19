
package vaccinereservation.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

@FeignClient(name="Hospital", url="http://Hospital:8080")
public interface HospitalService {

    @RequestMapping(method= RequestMethod.GET, path="/hospitals")
    public void assignHospital(@RequestBody Hospital hospital);

}