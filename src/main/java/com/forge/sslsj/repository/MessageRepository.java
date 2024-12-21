package com.forge.sslsj.repository;

import com.forge.sslsj.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @PostFilter("hasPermission(filterObject, 'READ')")
    List<Message> findAll();

    @PostAuthorize("hasPermission(returnObject, 'READ')")
    Message findById(Integer id);

    @PreAuthorize("hasPermission(#message, 'WRITE')")
    Message save(@Param("message")Message message);

}
