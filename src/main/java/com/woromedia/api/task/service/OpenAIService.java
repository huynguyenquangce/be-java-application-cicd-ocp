package com.woromedia.api.task.service;

import com.woromedia.api.task.model.OpenAIRequest;
import com.woromedia.api.task.model.OpenAIResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
@Service
public class OpenAIService {
    private static final Logger logger = LoggerFactory.getLogger(OpenAIService.class);

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    private final List<String> allowedDepartments = Arrays.asList(
            "Khoa Khám bệnh", "Khoa Tầm soát ung thư", "Khoa Gây mê hồi sức",
            "Khoa Điều trị tổng hợp", "Khoa Chăm sóc giảm nhẹ", "Khoa Dinh dưỡng tiết chế",
            "Khoa Cấp cứu", "Khoa Ngoại Tuyến Giáp", "Khoa Ngoại Phụ Khoa",
            "Khoa Ngoại Ngực bụng", "Khoa Ngoại Đầu cổ tai mũi họng",
            "Khoa Ngoại Tuyến vú", "Khoa Ngoại Đầu cổ hàm mặt", "Khoa Nội Huyết học hạch",
            "Khoa Nội Phụ khoa phổi", "Khoa Ung bướu nhi", "Khoa Nội Tuyến vú, tiêu hóa, gan, niệu",
            "Khoa Xạ Tổng quát", "Khoa Xạ đầu cổ tai mũi họng hàm mặt",
            "Khoa Xạ Phụ khoa", "Khoa Vận hành máy xạ", "Khoa Y học hạt nhân",
            "Đơn vị gan lồng ngực"
    );

    public String getMedicalAdvice(String symptom) {
        logger.info("Generating medical advice for symptom: {}", symptom);

// Chat gpt prompt
String systemPrompt = "Bạn là một bác sĩ chuyên tư vấn y khoa. "
        + "Dựa trên triệu chứng của bệnh nhân, hãy trả lời chỉ với một JSON hợp lệ, không có giải thích thêm. "
        + "JSON phải có 3 trường: "
        + "- 'suggested_departments': một danh sách các khoa phù hợp với triệu chứng, giá trị phải nằm trong danh sách sau: "
        + allowedDepartments.toString() + ". "
        + "- 'department_ids': một danh sách ID của các khoa trên, tương ứng với thứ tự của 'suggested_departments', dựa trên danh sách ID sau: "
        + "{1: 'Khoa Khám bệnh', 2: 'Khoa Tầm soát ung thư', 3: 'Khoa Gây mê hồi sức', 4: 'Khoa Điều trị tổng hợp', "
        + "5: 'Khoa Chăm sóc giảm nhẹ', 6: 'Khoa Dinh dưỡng tiết chế', 7: 'Khoa Cấp cứu', 8: 'Khoa Ngoại Tuyến Giáp', "
        + "9: 'Khoa Ngoại Phụ Khoa', 10: 'Khoa Ngoại Ngực bụng', 11: 'Khoa Ngoại Đầu cổ tai mũi họng', 12: 'Khoa Ngoại Tuyến vú', "
        + "13: 'Khoa Ngoại Đầu cổ hàm mặt', 14: 'Khoa Nội Huyết học hạch', 15: 'Khoa Nội Phụ khoa phổi', 16: 'Khoa Ung bướu nhi', "
        + "17: 'Khoa Nội Tuyến vú, tiêu hóa, gan, niệu', 18: 'Khoa Xạ Tổng quát', 19: 'Khoa Xạ đầu cổ tai mũi họng hàm mặt', "
        + "20: 'Khoa Xạ Phụ khoa', 21: 'Khoa Vận hành máy xạ', 22: 'Khoa Y học hạt nhân', 23: 'Đơn vị gan lồng ngực'}. "
        + "- 'advice': một lời khuyên ngắn gọn cho bệnh nhân một cách chi tiết nhất."
        + "- 'hospital_ids': một danh sách ID của các bệnh viện có chứa các khoa trên, được lấy từ bảng dữ liệu bệnh viện sau: "
        + "[{1: [1, 2, 3]}, {2: [2, 6, 7, 8, 9, 10, 11, 12]}, {3: [1, 2, 6, 7, 8, 9, 10]}, {4: [1, 2, 3, 4, 5, 8, 9, 10]}, "
        + "{5: [1, 2, 3, 4, 5, 6, 7, 8]}, {6: [1, 2, 3, 4, 5, 6, 7, 8]}, {7: [1, 2, 3, 4, 5, 6, 7, 8]}, {8: [1, 2, 3, 4, 5, 6, 7, 8]}, "
        + "{9: [1, 2, 3, 4, 5, 6, 7, 8]}, {10: [1, 2, 3, 4, 5, 6, 7, 8]}]. "
        + "Dựa vào 'department_ids', hãy tìm danh sách 'hospital_ids' tương ứng. Nếu giá trị hospital_id trùng thì chỉ lấy 1 giá trị duy nhất";


        OpenAIRequest requestBody = new OpenAIRequest(
                "gpt-4o",
                Arrays.asList(
                        new OpenAIRequest.Message("system", systemPrompt),
                        new OpenAIRequest.Message("user", "Bây giờ tôi đang có triệu chứng: " + symptom)
                ),
                new OpenAIRequest.ResponseFormat("json_object")
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<OpenAIRequest> request = new HttpEntity<>(requestBody, headers);

        try {
            // ResponseEntity<OpenAIResponse> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, OpenAIResponse.class);

            // if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            //     logger.info("OpenAI API response received successfully.");
            //     return response.getBody().getChoices().get(0).getMessage().getContent();
            // } else {
            //     logger.error("OpenAI API returned an unexpected response: {}", response.getStatusCode());
            //     return "Lỗi: Không thể lấy phản hồi từ OpenAI API.";
            // }
                    ResponseEntity<OpenAIResponse> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, OpenAIResponse.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            logger.info("OpenAI API response received successfully.");
            String responseContent = response.getBody().getChoices().get(0).getMessage().getContent();
            
            // Parse JSON để lấy department_ids
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseContent);
            JsonNode departmentIdsNode = jsonNode.get("department_ids");

            if (departmentIdsNode != null && departmentIdsNode.isArray()) {
                List<Integer> departmentIds = objectMapper.convertValue(departmentIdsNode, new TypeReference<List<Integer>>() {});
                // logger.info("Danh sách department_ids: {}", departmentIds);
                System.out.println("Danh sách department_ids: " + departmentIds);
            } else {
                logger.warn("Không tìm thấy trường 'department_ids' trong phản hồi.");
            }

            return responseContent;
        } else {
            logger.error("OpenAI API returned an unexpected response: {}", response.getStatusCode());
            return "Lỗi: Không thể lấy phản hồi từ OpenAI API.";
        }



        } catch (HttpStatusCodeException e) {
            logger.error("Error calling OpenAI API: {} - {}", e.getStatusCode(), e.getResponseBodyAsString(), e);
            return "Lỗi: Không thể kết nối đến OpenAI API.";
        } catch (Exception e) {
            logger.error("Unexpected error occurred while calling OpenAI API", e);
            return "Lỗi: Đã xảy ra lỗi không mong muốn.";
        }
    }
}

