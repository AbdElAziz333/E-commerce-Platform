package com.aziz.notification_service.repository;

import com.aziz.notification_service.model.Notification;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NotificationRepository extends CassandraRepository<Notification, UUID> {

}