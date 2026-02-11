package com.example.urlhealthmonitor.repository;

import com.example.urlhealthmonitor.model.MonitoredUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonitoredUrlRepository extends JpaRepository<MonitoredUrl, Long> {
}
