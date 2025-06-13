package com.example.computer_item_repair.web;

import com.example.computer_item_repair.domain.Project;
import com.example.computer_item_repair.services.MapValidationErrorService;
import com.example.computer_item_repair.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

//CRUD operations to service layer
@RestController
@RequestMapping("/api/project")
@CrossOrigin(origins = " * ", allowedHeaders = " * ")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @PostMapping("")
    public ResponseEntity<?> createNewProject(@Valid @RequestBody Project project, BindingResult result,
            Principal principal) {

        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);

        if (errorMap != null)
            return errorMap;

        Project savedProject = projectService.saveOrUpdateProject(project, principal.getName());
        return new ResponseEntity<>(savedProject, HttpStatus.CREATED);
    }

    // project by projectId
    @GetMapping("/{projectId}")
    public ResponseEntity<?> getProjectById(@PathVariable String projectId, Principal principal) {

        Project project = projectService.findProjectByIdentifier(projectId, principal.getName());
        return new ResponseEntity<Project>(project, HttpStatus.OK);
    }

    // all projects in the database
    @GetMapping("/all")
    public Iterable<Project> getAllProjects(Principal principal) {
        return projectService.findAllProjects(principal.getName());
    }

    // deleting project by projectId
    @DeleteMapping("/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable String projectId, Principal principal) {
        projectService.deleteProjectByIdentifier(projectId, principal.getName());

        return new ResponseEntity<String>("Project with ID " + projectId + " deleted", HttpStatus.OK);
    }
}
