package vaccinereservation;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel="hospitalManagements", path="hospitalManagements")
public interface HospitalManagementRepository extends PagingAndSortingRepository<HospitalManagement, Long>{


}
