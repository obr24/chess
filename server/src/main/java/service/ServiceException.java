package service;

import com.google.gson.Gson;
import spark.Response;

import java.util.HashMap;

public class ServiceException extends Exception {
  private int responseCode;
    public ServiceException(String message, int responseCode) {
        super(message);
        this.responseCode = responseCode;
    }

    public int getResponseCode() {
      return responseCode;
    }

    public HashMap<String, String> getResponse() {
        HashMap<String, String> responseHashmap = new HashMap<>();
        responseHashmap.put("message", getMessage());
        return responseHashmap;
    }
}
