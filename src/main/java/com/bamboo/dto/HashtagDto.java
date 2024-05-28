package com.bamboo.dto;

import com.bamboo.entity.Hashtag;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class HashtagDto {

    private Long tagId;

    private String tagName;

    private static ModelMapper modelMapper = new ModelMapper();

    public static HashtagDto of(Hashtag hashtag) {
        return modelMapper.map(hashtag, HashtagDto.class);
    }

}
