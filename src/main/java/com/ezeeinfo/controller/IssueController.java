package com.ezeeinfo.controller;

import com.ezeeinfo.issuemanager.model.Issue;
import com.ezeeinfo.issuemanager.service.IssueService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import java.security.Principal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/issues")
public class IssueController {

    private final IssueService issueService;


    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }


    @PostMapping()
    public final ResponseEntity<Issue> create(
            final Authentication principal,
            final @RequestBody Issue issue)
            throws SQLException, IOException {
            issue.setTenant(principal.getCredentials().toString());
            Issue createdIssue = issueService.create(principal.getName(), issue);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdIssue);
    }

    @PutMapping()
    public final ResponseEntity<Issue> update(
            final Authentication principal,
            final @RequestHeader("issueId")  Long issueId,
            final @RequestBody Issue issue)
            throws SQLException, IOException {
        issue.setTenant(principal.getCredentials().toString());
        Optional updated = issueService.update(principal.getName(), issue.getId(), issue);
        return ResponseEntity.status(HttpStatus.OK).body((Issue)updated.get());
    }

    @GetMapping()
    public final ResponseEntity<List<Issue>> list(
            final Authentication principal)
            throws SQLException, IOException {
        List<Issue> issues = issueService.list(principal.getName(), principal.getCredentials().toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(issues);
    }

}
