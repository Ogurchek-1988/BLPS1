package com.etoxto.lab1.model.issue;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {
    Issue getIssueByProcurementName(String name);
}
