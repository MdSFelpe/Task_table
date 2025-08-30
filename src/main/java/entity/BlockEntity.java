package entity;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class BlockEntity {
    private Long id;
    private OffsetDateTime blocked_timestamp;
    private String block_cause;
    private OffsetDateTime unblock_timestamp;
    private String unblock_cause;
}
