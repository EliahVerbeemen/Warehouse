package be.kdg.warehouse.Repositories;

import be.kdg.warehouse.Models.IncomingOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncomingOrderRepository extends JpaRepository<IncomingOrder,Long> {
}
