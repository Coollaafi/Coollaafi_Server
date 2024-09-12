package coollaafi.wot.web.ai;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class AIService {
    public MultipartFile generateLookBookImage(MultipartFile ootdImage){
        // 실제 AI API 완성 시 수정 필요
        return ootdImage;
    }
}
