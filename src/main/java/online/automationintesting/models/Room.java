package online.automationintesting.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Room {

    @JsonProperty("roomid")
    private Integer roomId;

    @JsonProperty("roomName")
    private String roomName;

    @JsonProperty("roomPrice")
    private Integer roomPrice;

    @JsonProperty("type")
    private String type;

    @JsonProperty("accessible")
    private Boolean accessible;

    @JsonProperty("image")
    private String image;

    @JsonProperty("description")
    private String description;

    @JsonProperty("features")
    private List<String> features;
}