package com.example.urlhealthmonitor.repository;

import com.example.urlhealthmonitor.model.UrlCheckHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UrlCheckHistoryRepository extends JpaRepository<UrlCheckHistory, Long> {
    List<UrlCheckHistory> findByMonitoredUrlIdOrderByCheckedAtDesc(Long monitoredUrlId);
}
