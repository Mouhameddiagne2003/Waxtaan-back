package com.whatsapxml.controller;

import com.whatsapxml.model.Group;
import com.whatsapxml.service.GroupXmlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupController {
    @Autowired
    private GroupXmlService groupXmlService;

    @GetMapping
    public List<Group> getAllGroups() {
        return groupXmlService.getAllGroups();
    }

    @GetMapping("/{id}")
    public Group getGroup(@PathVariable String id) {
        return groupXmlService.getGroupById(id);
    }

    @PostMapping
    public Group addGroup(@RequestBody Group group) {
        groupXmlService.addGroup(group);
        return group;
    }

    @PutMapping("/{id}")
    public void updateGroup(@PathVariable String id, @RequestBody Group group) {
        groupXmlService.updateGroup(id, group);
    }

    @DeleteMapping("/{id}")
    public void deleteGroup(@PathVariable String id) {
        groupXmlService.deleteGroup(id);
    }
}
