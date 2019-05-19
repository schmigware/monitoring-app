package org.cit.mcaleerj.thesis.monitorservice.dao.repository;

import org.cit.mcaleerj.thesis.monitorservice.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Message repository.
 *
 */
public interface MessageRepository extends JpaRepository<Message, Long>, MessageRepositoryCustom {

}