package com.aziz.payment_service.service;

import com.aziz.payment_service.kafka.PaymentPublisher;
import com.aziz.payment_service.kafka.events.OrderPaymentEvent;
import com.aziz.payment_service.mapper.PaymentMapper;
import com.aziz.payment_service.model.Payment;
import com.aziz.payment_service.repository.PaymentRepository;
import com.aziz.payment_service.util.enums.PaymentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository repository;
    private final PaymentMapper mapper;
    private final PaymentPublisher publisher;

    public void sendAndSavePayment(OrderPaymentEvent event) {
        Payment payment = mapper.orderEventToPayment(event);

        try {
            switch (payment.getMethod()) {
                case VODAFONE_CASH -> {
                    /// vodafone logic
//                restTemplate.postForObject(paymobConfig.getBaseUrl() + "/auth/tokens")
                }
                case VISA -> {
                    /// visa logic
                }
                default -> {

                }
            }

            payment.setStatus(PaymentStatus.SUCCESS);
        } catch (Exception e) {
            publisher.publishPaymentFailed(event.getEmail(), payment.getOrderNumber(), payment.getMethod());
            payment.setStatus(PaymentStatus.FAILED);
        }

        publisher.publishPaymentCompleted(event.getEmail(), payment.getOrderNumber(), payment.getMethod());
        repository.save(payment);
    }
}