package com.nexusblog.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TagDTO {

    private Long id;
    private String name;
    private Long articleCount;
}
