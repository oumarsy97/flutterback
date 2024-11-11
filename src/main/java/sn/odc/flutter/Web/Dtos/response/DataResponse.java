package sn.odc.flutter.Web.Dtos.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataResponse {
    private String message;
    private Object data;
    private String  status;
    public DataResponse(String message, Object data) {
        this.message = message;
        this.data = data;
        this.status = "success";
    }

}