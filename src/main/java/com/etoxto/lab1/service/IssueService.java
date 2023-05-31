package com.etoxto.lab1.service;

import com.etoxto.lab1.model.issue.Issue;

public interface IssueService {
    Issue save(Issue issue);
    Issue getIssueByProcurementName(String name);
    Issue update(Issue issue);
}
