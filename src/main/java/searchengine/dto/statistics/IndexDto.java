package searchengine.dto.statistics;


import lombok.Value;

@Value
public class IndexDto {
    Integer pageID;
    Integer lemmaID;
    Float rank;
}
