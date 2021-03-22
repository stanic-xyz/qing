package chenyunlong.zhangli.config.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.autoconfigure.mail.MailProperties;

/**
 * @author Stan
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EmailProperties extends MailProperties {

    private String emailFromName;

    private boolean enabled;

}
