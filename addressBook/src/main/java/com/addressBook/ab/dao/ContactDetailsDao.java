package com.addressBook.ab.dao;

import com.addressBook.ab.dto.ContactDetails;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
@Log4j2
public class ContactDetailsDao {

    private final Map<String, ContactDetails> directory = new ConcurrentHashMap<>();

    public ContactDetails saveRecord(ContactDetails contactDetails) {
        directory.put(contactDetails.getId(), contactDetails);
        return contactDetails;
    }

    public ContactDetails updateRecord(ContactDetails contactDetails) {
       // log.info("Going to update the records for contactDetails for uid {}",contactDetails.getId());
        return directory.computeIfPresent(contactDetails.getId(), (key, existing) -> {
            if (contactDetails.getName() != null) {
                existing.setName(contactDetails.getName());
            }
            if (contactDetails.getPhone() != null) {
                existing.setPhone(contactDetails.getPhone());
            }
            if (contactDetails.getEmail() != null) {
                existing.setEmail(contactDetails.getEmail());
            }
            return existing;
        });
    }

    public ContactDetails getByUid(String id) {
        return directory.get(id);
    }

    public boolean deleteRecord(String id) {
       if(Objects.isNull(directory.remove(id))){
           log.info("Nothing found for id {}", id);
           return false;
       }else{
           log.info("Id found going to remove");
           return true;
       }
    }

    public List<ContactDetails> searchParam(String query) {
        String lowercaseQuery = query.toLowerCase();
        List<ContactDetails> ans = new ArrayList<>();

        for (ContactDetails contact : directory.values()) {
            try {
                if (contact.getName().toLowerCase().contains(lowercaseQuery)) {
                    ans.add(contact);
                }
            }catch (Exception e){
                log.error("Excpetion caught while adding to search list {}",e.getMessage(),e);
            }
        }

        return ans;
    }

    public Collection<ContactDetails> getAll() {
        return directory.values();
    }


}
