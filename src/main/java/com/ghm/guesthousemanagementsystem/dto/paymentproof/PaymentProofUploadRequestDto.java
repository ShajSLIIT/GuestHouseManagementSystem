// PaymentProofUploadRequestDto.java
package com.naveen.guesthousemanagementsystem.dto.paymentproof;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentProofUploadRequestDto {
    private String referenceId;
    private MultipartFile file;
}