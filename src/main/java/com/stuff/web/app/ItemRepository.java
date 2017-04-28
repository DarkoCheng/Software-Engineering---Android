package com.stuff.web.app;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Collection;

public interface ItemRepository extends JpaRepository<Item, String> {
    Collection<Item> findByLenderPhoneNumber(String phoneNumber);
    Collection<Item> findByLendeeNumber(String phoneNumber);
    Item findById(Long id);
}