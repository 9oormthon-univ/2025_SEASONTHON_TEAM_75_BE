package com.trashheroesbe.feature.partner.application;

import static com.trashheroesbe.global.response.type.ErrorCode.ENTITY_NOT_FOUND;
import static com.trashheroesbe.global.response.type.ErrorCode.EXISTS_EMAIL;
import static com.trashheroesbe.global.response.type.ErrorCode.S3_UPLOAD_FAIL;

import com.trashheroesbe.feature.partner.domain.entity.Partner;
import com.trashheroesbe.feature.partner.dto.request.RegisterPartnerRequest;
import com.trashheroesbe.feature.partner.dto.request.UpdatePartnerRequest;
import com.trashheroesbe.feature.partner.dto.response.RegisterPartnerResponse;
import com.trashheroesbe.feature.partner.infrastructure.PartnerRepository;
import com.trashheroesbe.feature.user.domain.entity.User;
import com.trashheroesbe.feature.user.infrastructure.UserRepository;
import com.trashheroesbe.global.exception.BusinessException;
import com.trashheroesbe.global.util.FileUtils;
import com.trashheroesbe.infrastructure.port.s3.FileStoragePort;
import jakarta.validation.Valid;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PartnerService {

    private static final String S3_PARTNER_PREFIX = "partner/";

    private final FileStoragePort fileStoragePort;

    private final PartnerRepository partnerRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public RegisterPartnerResponse registerPartner(
        RegisterPartnerRequest request,
        MultipartFile image
    ) {
        if (partnerRepository.existsByEmail(request.email())) {
            throw new BusinessException(EXISTS_EMAIL);
        }

        String rawPassword = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8);
        String encodedPassword = passwordEncoder.encode(rawPassword);

        String imageUrl = "";
        if (image != null && !image.isEmpty()) {
            try {
                String storedKey = FileUtils.generateStoredKey(
                    Objects.requireNonNull(image.getOriginalFilename()), S3_PARTNER_PREFIX);
                imageUrl = fileStoragePort.uploadFile(
                    storedKey,
                    image.getContentType(),
                    image.getBytes()
                );
            } catch (Exception e) {
                throw new BusinessException(S3_UPLOAD_FAIL, e);
            }
        }

        Partner partner = request.toPartner(encodedPassword, imageUrl);
        User user = User.createPartnerUser(request.partnerName(), partner);

        partnerRepository.save(partner);
        userRepository.save(user);

        return RegisterPartnerResponse.from(request.partnerName(), request.email(), rawPassword);
    }

    @Transactional
    public void updatePartner(
        UpdatePartnerRequest request,
        MultipartFile image,
        User user
    ) {
        Long partnerId = user.getPartner().getId();
        Partner partner = partnerRepository.findById(partnerId)
            .orElseThrow(() -> new BusinessException(ENTITY_NOT_FOUND));

        if (request.partnerName() != null) {
            partner.updatePartnerName(request.partnerName());
            user.updateNickname(request.partnerName());
        }

        if (request.email() != null && !request.email().equals(partner.getEmail())) {
            if (partnerRepository.existsByEmail(request.email())) {
                throw new BusinessException(EXISTS_EMAIL);
            }
            partner.updateEmail(request.email());
        }

        if (request.password() != null) {
            String encodedPassword = passwordEncoder.encode(request.password());
            partner.updatePassword(encodedPassword);
        }

        if (request.address() != null) {
            partner.updateAddress(request.address());
        }

        if (request.description() != null) {
            partner.updateDescription(request.description());
        }

        if (image != null && !image.isEmpty()) {
            try {
                String storedKey = FileUtils.generateStoredKey(
                    Objects.requireNonNull(image.getOriginalFilename()),
                    S3_PARTNER_PREFIX
                );

                String imageUrl = fileStoragePort.uploadFile(
                    storedKey,
                    image.getContentType(),
                    image.getBytes()
                );

                if (partner.getImageUrl() != null && !partner.getImageUrl().isEmpty()) {
                    fileStoragePort.deleteFileByUrl(partner.getImageUrl());
                }
                partner.updateImageUrl(imageUrl);
            } catch (Exception e) {
                throw new BusinessException(S3_UPLOAD_FAIL, e);
            }
        }
    }
}
