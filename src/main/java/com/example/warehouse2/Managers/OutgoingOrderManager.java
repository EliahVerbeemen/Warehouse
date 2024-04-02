package com.example.warehouse2.Managers;

import com.example.warehouse2.Models.OutgoingOrder;
import com.example.warehouse2.Repositories.OutgoingOrderRepository;
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
