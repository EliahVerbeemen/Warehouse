package com.example.warehouse2.Repositories;

import com.example.warehouse2.Models.IncommingOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncommingOrderRepository extends JpaRepository<IncommingOrder,Long> {
}
