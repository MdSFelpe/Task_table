package entity;

import lombok.Data;
import org.w3c.dom.Text;

import java.time.OffsetDateTime;

@Data
public class CardEntity {

    private Long id;
    private String title;
    private String description;
    private BoardColumnEntity boardColumn = new BoardColumnEntity();

}