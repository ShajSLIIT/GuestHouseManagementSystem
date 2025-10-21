// PaymentProofService.java
package com.naveen.guesthousemanagementsystem.service;


import com.naveen.guesthousemanagementsystem.dto.paymentproof.PaymentProofResponseDto;
import com.naveen.guesthousemanagementsystem.dto.paymentproof.PaymentProofUploadRequestDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface PaymentProofService {
    // Using referenceId methods
    PaymentProofResponseDto uploadPaymentProof(PaymentProofUploadRequestDto uploadRequest);

    PaymentProofResponseDto uploadPaymentProof(String referenceId, MultipartFile file);

    PaymentProofResponseDto updatePaymentProof(String referenceId, MultipartFile file);

    PaymentProofResponseDto getPaymentProofByReferenceId(String referenceId);

    PaymentProofResponseDto getPaymentProof(UUID paymentProofId);

    void deletePaymentProofByReferenceId(String referenceId);

    void deletePaymentProof(UUID paymentProofId);

    byte[] downloadPaymentProofByReferenceId(String referenceId);

    byte[] downloadPaymentProof(UUID paymentProofId);

    boolean hasPaymentProof(String referenceId);
}