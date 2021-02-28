package tv.zhanqi.essearch.svr;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "es")
@EnableConfigurationProperties(EsServerOpts.class)
public class EsServerOpts {
    private String serverHost;

    private Integer serverPort;

    private Integer serverBossNum;

    private Integer serverWorkNum;

    private Boolean serverTcpKeepalive;

    private Integer serverTcpBacklog;
}
