package org.quantum.dto;

import lombok.Value;

@Value(staticConstructor = "of")
public class User {

    private Integer id;
    private String username;
    private String password;
}
