package be.kdg.warehouse.Repositories;

import be.kdg.warehouse.Models.OutgoingOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutgoingOrderRepository extends JpaRepository<OutgoingOrder,Long> {
}
