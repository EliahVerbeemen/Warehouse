package com.example.warehouse2.Repositories;

import com.example.warehouse2.Models.OutgoingOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutgoingOrderRepository extends JpaRepository<OutgoingOrder,Long> {
}
