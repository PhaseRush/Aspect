package main.utility;

import com.ibm.watson.developer_cloud.language_translator.v3.model.TranslateOptions;
import com.ibm.watson.developer_cloud.language_translator.v3.model.TranslationResult;
import com.ibm.watson.developer_cloud.service.exception.NotFoundException;
import com.ibm.watson.developer_cloud.service.exception.RequestTooLargeException;
import com.ibm.watson.developer_cloud.service.exception.ServiceResponseException;
import main.Aspect;

public class TranslateUtils {
    public static String transEnFr(String engl) {

        TranslateOptions translateOptions = new TranslateOptions.Builder()
                .addText(engl)
                .modelId("en-fr")
                .build();
        try {
            TranslationResult translationResult = Aspect.translator.translate(translateOptions).execute();
            return translationResult.getTranslations().get(0).getTranslationOutput();
        } catch (NotFoundException e) {
            // Handle Not Found (404) exception
            System.out.println("request not found");
        } catch (RequestTooLargeException e) {
            // Handle Request Too Large (413) exception
            System.out.println("request too big");
        } catch (ServiceResponseException e) {
            // Base class for all exceptions caused by error responses from the service
            System.out.println("Service returned status code " + e.getStatusCode() + ": " + e.getMessage());
        }
        return "Translate failed";
    }
}
