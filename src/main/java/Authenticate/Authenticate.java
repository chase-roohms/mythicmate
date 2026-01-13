package Authenticate;

import io.github.cdimascio.dotenv.Dotenv;

public class Authenticate {
    /*Private Fields                                                                            */
    /*==========================================================================================*/
    private static final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
    
    private final static String password = getEnvVar("MYTHICMATE_PASSWORD", "");
    private final static String token = getEnvVar("MYTHICMATE_TOKEN", "");
    private final static String GPTKey = getEnvVar("MYTHICMATE_GPT_KEY", "");
    
    private static String getEnvVar(String key, String defaultValue) {
        String value = dotenv.get(key);
        if (value == null || value.isEmpty()) {
            value = System.getenv(key);
        }
        return value != null ? value : defaultValue;
    }

    /*Login Function                                                                            */
    /*==========================================================================================*/
    public boolean login(String userPassword){
        return userPassword.equals(password);
    }

    public String getToken(){
        return token;
    }

    public static String getGPTKey() {
        return GPTKey;
    }
}
