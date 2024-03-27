package com.jeeva.dto.response;


import java.util.Date;

import com.jeeva.model.enumerate.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

    private String pid;
    private PaymentStatus status;
    private Double amount;
    private String mode;
    private String studentId;
    private String courseId;
    private Date createdAt;

}
