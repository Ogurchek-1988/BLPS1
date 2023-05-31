package com.etoxto.lab1.service;

import com.etoxto.lab1.model.issue.Issue;
import com.etoxto.lab1.model.issue.IssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IssueServiceImpl implements IssueService {

    private final IssueRepository issueRepository;

    @Autowired
    public IssueServiceImpl(IssueRepository issueRepository) {
        this.issueRepository = issueRepository;
    }

    @Override
    public Issue save(Issue issue) {
        return issueRepository.save(issue);
    }

    @Override
    public Issue getIssueByProcurementName(String name) {
        return issueRepository.getIssueByProcurementName(name);
    }

    @Override
    public Issue update(Issue issue) {
        return issueRepository.save(issue);
    }
}
