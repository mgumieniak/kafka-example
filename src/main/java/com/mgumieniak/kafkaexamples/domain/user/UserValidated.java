package com.mgumieniak.kafkaexamples.domain.user;

import lombok.*;

@Value
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class UserValidated {
    User user;
    Boolean valid;
}
