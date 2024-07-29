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

/**
 * IssueController class
 */
@RestController
@RequestMapping("/api/issues")
public class IssueController {

    private final IssueService issueService;


    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }


    /**
     *
     * @param principal
     * @param issue
     * @return
     * @throws SQLException
     * @throws IOException
     */
    @PostMapping()
    public final ResponseEntity<Issue> create(
            final Authentication principal,
            final @RequestBody Issue issue)
            throws SQLException, IOException {
            issue.setTenent(principal.getCredentials().toString());
            Issue createdIssue = issueService.create(principal.getName(), issue);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdIssue);
    }

    /**
     *
     * @param id
     * @param principal
     * @param issue
     * @return
     * @throws SQLException
     * @throws IOException
     */
    @PutMapping("/{id}")
    public final ResponseEntity<Issue> update(
            @PathVariable Long id,
            final Authentication principal,
            final @RequestBody Issue issue)
            throws SQLException, IOException {
        issue.setTenent(principal.getCredentials().toString());
        Optional updated = issueService.update(principal.getName(), id, issue);
        return ResponseEntity.status(HttpStatus.OK).body((Issue)updated.get());
    }

    /**
     *
     * @param principal
     * @return
     * @throws SQLException
     * @throws IOException
     */
    @GetMapping()
    public final ResponseEntity<List<Issue>> list(
            final Authentication principal)
            throws SQLException, IOException {
        List<Issue> issues = issueService.list(principal.getName(), principal.getCredentials().toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(issues);
    }

}
