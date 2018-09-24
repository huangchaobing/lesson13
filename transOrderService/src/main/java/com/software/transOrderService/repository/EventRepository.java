package com.software.transOrderService.repository;

import com.software.transOrderService.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event,Integer> {
}
