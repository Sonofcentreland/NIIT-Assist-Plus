package com.jeeva.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnquiryRequest {

    private String title;
    private String description;
    private String type;
    private String studentId;
    private String courseId;
    
}
