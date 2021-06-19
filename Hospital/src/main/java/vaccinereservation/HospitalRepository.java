package vaccinereservation;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel="hospitals", path="hospitals")
public interface HospitalRepository extends PagingAndSortingRepository<Hospital, Long>{


}
