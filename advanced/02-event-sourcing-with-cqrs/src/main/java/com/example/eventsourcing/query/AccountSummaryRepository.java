package com.example.eventsourcing.query;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountSummaryRepository extends JpaRepository<AccountSummary, String> {
}
