package vaccinereservation;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="ReservationStatus_table")
public class ReservationStatus {

        @Id
        @GeneratedValue(strategy=GenerationType.AUTO)
        private Long id;


        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

}
