package com.todimu.backend.dropboxclone.service.supplier;

import com.todimu.backend.dropboxclone.data.dto.request.CreateSharedLinkRequest;
import com.todimu.backend.dropboxclone.data.entity.SharedLink;

public interface CreateSharedLinkForObject {

    SharedLink createSharedLink(CreateSharedLinkRequest linkRequest);
}
