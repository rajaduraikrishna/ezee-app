package com.ezeeinfo.controller;

import com.ezeeinfo.issuemanager.model.Issue;
import com.ezeeinfo.issuemanager.service.IssueService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * IssueController class.
 */
@RestController
@RequestMapping("/api/issues")
public class IssueController {

    /**
     * IssueService class and issueService variable.
     */
    private final IssueService issueService;

    /**
     * IssueController constructer.
     * @param service
     */

    public IssueController(final IssueService service) {
        this.issueService = service;
    }


    /**
     *  PostMapping for creation.
     * @param principal
     * @param issue
     * @return
     * @throws SQLException
     * @throws IOException
     * @return create
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
     * PutMapping for update.
     * @param id
     * @param principal
     * @param issue
     * @return
     * @throws SQLException
     * @throws IOException
     * @return update
     */
    @PutMapping("/{id}")
    public final ResponseEntity<Issue> update(
            @PathVariable final Long id,
            final Authentication principal,
            final @RequestBody Issue issue)
            throws SQLException, IOException {
        issue.setTenent(principal.getCredentials().toString());
        Optional updated = issueService.update(principal.getName(), id, issue);
        return ResponseEntity.status(HttpStatus.OK).body((Issue) updated.get());
    }

    /**
     * GetMapping for list.
     * @param principal
     * @return
     * @throws SQLException
     * @throws IOException
     * @return list
     */
    @GetMapping()
    public final ResponseEntity<List<Issue>> list(
            final Authentication principal)
            throws SQLException,
            IOException {
        List<Issue> issues = issueService.list(principal.getName(),
                principal.getCredentials().toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(issues);
    }

}
