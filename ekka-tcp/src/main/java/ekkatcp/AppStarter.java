package ekkatcp;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import com.fasterxml.jackson.databind.ObjectMapper;

import ekkatcp.server.Server;

public class AppStarter {

    private static final Logger log = LoggerFactory.getLogger(AppStarter.class);

    public static void main(String[] args) throws Exception {

        App app = getConfig().getApp(); 
        Server server = new Server(app.getServer(), app.getMessageProcessor());
        server.start();

        log.info("tcp app");
    }

    private static AppConf getConfig() throws Exception {
        Yaml yaml = new Yaml(new Constructor(AppConf.class, new LoaderOptions()));
        InputStream inputStream = AppStarter.class.getClassLoader()
            .getResourceAsStream("app.yml");
        AppConf conf = yaml.load(inputStream);
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("config {}",objectMapper.writeValueAsString(conf));
        return conf;
    } 

}