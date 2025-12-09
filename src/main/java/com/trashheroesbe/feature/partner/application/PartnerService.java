package com.trashheroesbe.feature.partner.application;

import static com.trashheroesbe.global.response.type.ErrorCode.EXISTS_EMAIL;
import static com.trashheroesbe.global.response.type.ErrorCode.S3_UPLOAD_FAIL;

import com.trashheroesbe.feature.partner.domain.entity.Partner;
import com.trashheroesbe.feature.partner.dto.request.RegisterPartnerRequest;
import com.trashheroesbe.feature.partner.dto.response.RegisterPartnerResponse;
import com.trashheroesbe.feature.partner.infrastructure.PartnerRepository;
import com.trashheroesbe.feature.user.domain.entity.User;
import com.trashheroesbe.feature.user.infrastructure.UserRepository;
import com.trashheroesbe.global.exception.BusinessException;
import com.trashheroesbe.global.util.FileUtils;
import com.trashheroesbe.infrastructure.port.s3.FileStoragePort;
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
            String storedKey = FileUtils.generateStoredKey(
                Objects.requireNonNull(image.getOriginalFilename()), S3_PARTNER_PREFIX);

            try {
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
        User user = User.createPartnerUser(request.partnerName());

        partnerRepository.save(partner);
        userRepository.save(user);

        return RegisterPartnerResponse.from(request.partnerName(), request.email(), rawPassword);
    }
}
