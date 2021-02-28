package tv.zhanqi.essearch.ctl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import tv.zhanqi.essearch.dto.Result;

@org.springframework.stereotype.Controller
public abstract class Controller {

    protected ObjectMapper objectMapper;

    public Controller() {
    }

    @Autowired
    public Controller(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String wrapOKResult(int code, String message, Object data) throws JsonProcessingException {
        return this.objectMapper.writeValueAsString(new Result<>(code, message, data));
    }

}
