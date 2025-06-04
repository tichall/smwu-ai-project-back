package smwu.server.domain.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class OpenAiService {
    @Value("${openai.endpoint}")
    private String endpoint;

    @Value("${openai.apiKey}")
    private String apiKey;

    public String callOpenAi(String contentText) {
        try(CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // POST 요청 객체 생성
            HttpPost request = new HttpPost(endpoint);
            request.addHeader("api-key", apiKey);
            request.addHeader("Content-Type", "application/json");

            ObjectMapper mapper = new ObjectMapper();

            String systemPrompt = loadSystemPrompt();

            ObjectNode systemMessage = mapper.createObjectNode();
            systemMessage.put("role", "system");
            systemMessage.put("content", systemPrompt);

            // 요청 바디 구성: "messages" 배열에 "user" 역할과 자연어 content
            ObjectNode userMessage = mapper.createObjectNode();
            userMessage.put("role", "user");
            userMessage.put("content", contentText);  // ✅ 자연어 텍스트

            ArrayNode messages = mapper.createArrayNode();
            messages.add(systemMessage);
            messages.add(userMessage);

            ObjectNode requestBody = mapper.createObjectNode();
            requestBody.set("messages", messages);

            String body = mapper.writeValueAsString(requestBody);

            // 요청 본문 설정
            request.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));

            try (CloseableHttpResponse response = (CloseableHttpResponse) httpClient.execute(request)) {
                String json = EntityUtils.toString(response.getEntity());
                JsonNode responseJson = new ObjectMapper().readTree(json);
                System.out.println("GPT 응답 전체: \n" + responseJson);

                return responseJson.get("choices").get(0).get("message").get("content").asText();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "오류 발생: " + e.getMessage();
        }
    }

    public String loadSystemPrompt() {
        try {
            ClassPathResource resource = new ClassPathResource("prompts/recommendation-prompt.txt");
            return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return "당신은 금융 전문가입니다...";
        }
    }

}
