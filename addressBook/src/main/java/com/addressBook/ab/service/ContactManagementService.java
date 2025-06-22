package com.addressBook.ab.service;

import com.addressBook.ab.dao.ContactDetailsDao;
import com.addressBook.ab.dto.ContactDetails;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Log4j2
public class ContactManagementService {

    @Autowired
    ContactDetailsDao contactDetailsDao;

    @Autowired
    OptimisedSearchingService optimisedSearchingService;

    public List<ContactDetails> createContact(List<ContactDetails> contactDetailsList) {

        log.info("Contact list received is {}", contactDetailsList.size());
        for (ContactDetails contactDetails : contactDetailsList) {
            String uuid = UUID.randomUUID().toString();
            try {
                //log.info("uuid created for contact list {}", uuid);
                contactDetails.setId(uuid);
                contactDetailsDao.saveRecord(contactDetails);
                optimisedSearchingService.indexName(contactDetails.getName(), contactDetails.getId());
            } catch (Exception e) {
                log.error("Exception caught while saving contact with uuid {} is {}", uuid, e.getMessage(), e);
            }
        }
        log.info("records created, going to push into database");
        return contactDetailsList;
    }

    public List<ContactDetails> updateContact(List<ContactDetails> contactDetailsList) {
        List<ContactDetails> updatedContactDetails = new ArrayList<>();
        log.info("Contact list received to be updated {}", contactDetailsList.size());
        for (ContactDetails contactDetails : contactDetailsList) {

            try {
                log.info("uuid created for contact list {}", contactDetails.getId());
                if(Objects.isNull(contactDetailsDao.getByUid(contactDetails.getId()))){
                    log.info("No existing record found with the id {}", contactDetails.getId());
                }else{
                    ContactDetails contactDetailsOld = contactDetailsDao.getByUid(contactDetails.getId());
                    boolean nameChanged = contactDetails.getName() != null && !contactDetails.getName().equals(contactDetailsOld.getName());
                    if (nameChanged) {
                        optimisedSearchingService.removeName(contactDetailsOld.getName(), contactDetailsOld.getId());
                    }

                    ContactDetails updatedContact = contactDetailsDao.updateRecord(contactDetails);
                    updatedContactDetails.add(updatedContact);
                    if (nameChanged) {
                        optimisedSearchingService.indexName(updatedContact.getName(), updatedContact.getId());
                    }
                }
            } catch (Exception e) {
                log.error("Exception caught while updating contact with uuid {} is {}", contactDetails.getId(), e.getMessage(), e);
            }
        }
        log.info("records created, going to push into database");
        return updatedContactDetails;
    }

    public int deletContactDetails(List<String> list) {

        //log.info("Contact list received is {}", list);
        int deleted = 0;
        for (String idToBeDeleted: list) {

            try {
                ContactDetails contact = contactDetailsDao.getByUid(idToBeDeleted);
                if (contact != null) {
                    optimisedSearchingService.removeName(contact.getName(), idToBeDeleted);
                    contactDetailsDao.deleteRecord(idToBeDeleted);
                    deleted++;
                }
            } catch (Exception e) {
                log.error("Exception caught deleting contact with uuid {} is {}", idToBeDeleted, e.getMessage(), e);
            }
        }
        log.info("records created, going to push into database");
        return deleted;
    }

//    public List<ContactDetails> searchParam(String param){
//        return contactDetailsDao.searchParam(param);
//    }

    public List<ContactDetails> searchParam(String query) {
        Set<String> ids = optimisedSearchingService.search(query);
        List<ContactDetails> result = new ArrayList<>();

        for (String id : ids) {
            ContactDetails contact = contactDetailsDao.getByUid(id);
            if (contact != null) {
                result.add(contact);
            }
        }

        return result;
    }

    public List<ContactDetails> getAllContacts() {
        return new ArrayList<>(contactDetailsDao.getAll());
    }



}
