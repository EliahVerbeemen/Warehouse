package be.kdg.warehouse.Managers;

import be.kdg.warehouse.Models.OutgoingOrder;
import be.kdg.warehouse.Repositories.OutgoingOrderRepository;
import org.springframework.stereotype.Component;

@Component
public class OutgoingOrderManager implements IOutgoingOrderManager {
    private OutgoingOrderRepository repo;

    public OutgoingOrderManager(OutgoingOrderRepository repository) {
        repo = repository;
    }

    @Override
    public OutgoingOrder addOutgoingOrder(OutgoingOrder outgoingOrder) {
        return repo.save(outgoingOrder);
    }
}
