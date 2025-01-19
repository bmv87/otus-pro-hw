package ru.otus.pro.hw.serialization.models.in;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Member {

    @JsonProperty("handle_id")
    private Integer handleId;
    private String first;
    private String middle;
    private String last;
    @JsonProperty("phone_number")
    private String phoneNumber;
    private String service;
    @JsonProperty("image_path")
    private String imagePath;
    @JsonProperty("thumb_path")
    private String thumbPath;


//          "first":"Moskow",
//                  "handle_id":934,
//                  "image_path":"N:\\Tenorshare iCareFone\\Temp\\AnalysisTemp\\113.jpg",
//                  "last":"Saint-Petersburg",
//                  "middle":"Bologoe",
//                  "phone_number":"Apple",
//                  "service":"SMS",
//                  "thumb_path":"N:\\Tenorshare iCareFone\\Temp\\AnalysisTemp\\113.thumb"
}
