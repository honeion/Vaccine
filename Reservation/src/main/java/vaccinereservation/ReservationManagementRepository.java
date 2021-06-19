package vaccinereservation;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel="reservationManagements", path="reservationManagements")
public interface ReservationManagementRepository extends PagingAndSortingRepository<ReservationManagement, Long>{


}
