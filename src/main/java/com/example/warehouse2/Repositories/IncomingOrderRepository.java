package com.example.warehouse2.Repositories;

import com.example.warehouse2.Models.IncomingOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncomingOrderRepository extends JpaRepository<IncomingOrder,Long> {
}
