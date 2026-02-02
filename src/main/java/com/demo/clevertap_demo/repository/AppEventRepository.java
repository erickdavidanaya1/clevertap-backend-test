package com.demo.clevertap_demo.repository;

import com.demo.clevertap_demo.model.AppEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppEventRepository extends JpaRepository<AppEvent, Long> {
  List<AppEvent> findByUser_IdOrderByTimestampDesc(Long userId);
}
