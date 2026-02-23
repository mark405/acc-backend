package com.traffgun.acc.dto.login;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TotpStatusResponse {
    private Boolean enabled;
}