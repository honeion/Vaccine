package vaccinereservation;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel="vaccineManagements", path="vaccineManagements")
public interface VaccineManagementRepository extends PagingAndSortingRepository<VaccineManagement, Long>{


}
