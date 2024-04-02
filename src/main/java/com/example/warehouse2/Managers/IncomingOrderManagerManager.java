package com.example.warehouse2.Managers;

import com.example.warehouse2.Models.IncomingOrder;
import com.example.warehouse2.Repositories.IncomingOrderRepository;
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
