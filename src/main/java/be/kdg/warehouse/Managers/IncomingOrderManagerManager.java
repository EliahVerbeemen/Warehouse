package be.kdg.warehouse.Managers;

import be.kdg.warehouse.Repositories.IncomingOrderRepository;
import be.kdg.warehouse.Models.IncomingOrder;
import org.springframework.stereotype.Component;

@Component
public class IncomingOrderManagerManager implements IIncomingOrderManager {
    private IncomingOrderRepository repo;

    public IncomingOrderManagerManager(IncomingOrderRepository repository){
        repo = repository;
    }

    public IncomingOrder addIncomingOrder(IncomingOrder incomingOrder){
        return repo.save(incomingOrder);
    }


}
