package com.addressBook.ab.controller;

import com.addressBook.ab.dto.ContactDetails;
import com.addressBook.ab.service.ContactManagementService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
@Log4j2
public class ContactMangamentController {


    @Autowired
    ContactManagementService contactManagementService;


    @PostMapping("/create")
    public List<ContactDetails> createContactDetails(@RequestBody List<ContactDetails> contactDetails) {
        log.info("Contact Details received are {}", contactDetails.size());
        try {
            return contactManagementService.createContact(contactDetails);
        } catch (Exception e) {
            log.error("Exception occured while creating contact {}", e.getMessage(), e);
        }
        return null;
    }

    @PutMapping("/update")
    public List<ContactDetails> updateContacts(@RequestBody List<ContactDetails> contactDetails) {
        log.info("Contact Details received are {}", contactDetails.size());
        try {
            return contactManagementService.updateContact(contactDetails);
        } catch (Exception e) {
            log.error("Exception occured while creating contact {}", e.getMessage(), e);
        }
        return null;
    }

    @DeleteMapping("/delete")
    public Map<String, Integer> deleteContacts(@RequestBody List<String> ids) {

        log.info("Contact Details received are {}", ids.size());
        try {
            int deleted = contactManagementService.deletContactDetails(ids);
            return Collections.singletonMap("deleted", deleted);
        } catch (Exception e) {
            log.error("Exception occured while deleting contact {}", e.getMessage(), e);
        }
        return null;
    }

    @PostMapping("/search")
    public List<ContactDetails> searchContacts(@RequestBody Map<String, String> body) {
        try {
            String param = body.getOrDefault("query", "");
            return contactManagementService.searchParam(param);
        } catch (Exception e) {
            log.error("Exception occured while searching contact {}", e.getMessage(), e);
        }
        return null;
    }

    @GetMapping("/all")
    public List<ContactDetails> getAllContacts() {
        return contactManagementService.getAllContacts();
    }

}
