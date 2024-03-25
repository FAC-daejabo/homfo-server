package com.homfo.naver.sms.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.homfo.error.ThirdPartyUnavailableException;
import com.homfo.naver.sms.request.NaverSmsRequest;
import com.homfo.naver.sms.response.NaverSmsResponse;
import com.homfo.sms.dto.SmsSendDto;
import com.homfo.sms.infra.enums.SmsErrorCode;
import com.homfo.sms.port.SendSmsPort;
import lombok.NonNull;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.reactive.function.client.WebClient;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class NaverSmsClient implements SendSmsPort {
    @Value("${naver-cloud-sms.accessKey}")
    private String accessKey;

    @Value("${naver-cloud-sms.secretKey}")
    private String secretKey;

    @Value("${naver-cloud-sms.serviceId}")
    private String serviceId;

    @Value("${naver-cloud-sms.senderPhone}")
    private String senderPhone;

    private static final String NCP_TIMESTAMP_HEADER = "x-ncp-apigw-timestamp";

    private static final String NCP_ACCESS_KEY_HEADER = "x-ncp-iam-access-key";

    private static final String NCP_SIGNATURE_HEADER = "x-ncp-apigw-signature-v2";

    private final WebClient webClient;

    @Autowired
    public NaverSmsClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://sens.apigw.ntruss.com").build();
    }


    public void sendSms(@NonNull SmsSendDto smsSendDto) {
        try {
            NaverSmsResponse response = sendSmsToNcp(smsSendDto);

            if(!Objects.equals(response.getStatusCode(), "202")) {
                throw new ThirdPartyUnavailableException(SmsErrorCode.FAILED_SEND_SMS);
            }
        } catch (Exception e) {
            throw new ThirdPartyUnavailableException(SmsErrorCode.FAILED_SEND_SMS);
        }
    }

    private NaverSmsResponse sendSmsToNcp(SmsSendDto smsSendDto) throws JsonProcessingException, RestClientException, InvalidKeyException, NoSuchAlgorithmException {
        HttpHeaders headers = getNcpHttpHeaders();
        List<SmsSendDto> messages = new ArrayList<>();
        messages.add(smsSendDto);

        NaverSmsRequest request = NaverSmsRequest.builder()
                .type("SMS")
                .contentType("COMM")
                .countryCode("82")
                .from(senderPhone)
                .content(smsSendDto.message())
                .messages(messages)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(request);

        return webClient.post()
                .uri("/sms/v2/services/{serviceId}/messages", serviceId)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .bodyValue(body)
                .retrieve()
                .bodyToMono(NaverSmsResponse.class)
                .block();
    }

    private String makeSignature(Long time) throws NoSuchAlgorithmException, InvalidKeyException {
        String space = " ";
        String newLine = "\n";
        String method = "POST";
        String url = "/sms/v2/services/" + this.serviceId + "/messages";
        String timestamp = time.toString();
        String accessKey = this.accessKey;
        String secretKey = this.secretKey;
        String message = method +
                space +
                url +
                newLine +
                timestamp +
                newLine +
                accessKey;

        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));

        return Base64.encodeBase64String(rawHmac);
    }

    private HttpHeaders getNcpHttpHeaders() throws NoSuchAlgorithmException, InvalidKeyException {
        Long time = System.currentTimeMillis();
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(NCP_TIMESTAMP_HEADER, time.toString());
        headers.set(NCP_ACCESS_KEY_HEADER, accessKey);
        headers.set(NCP_SIGNATURE_HEADER, makeSignature(time));

        return headers;
    }
}
