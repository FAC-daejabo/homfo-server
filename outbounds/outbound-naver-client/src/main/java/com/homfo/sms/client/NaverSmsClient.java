package com.homfo.sms.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.homfo.error.ThirdPartyUnavailableException;
import com.homfo.sms.dto.SmsSendDto;
import com.homfo.sms.infra.enums.SmsErrorCode;
import com.homfo.sms.port.SendSmsPort;
import com.homfo.sms.request.NaverSmsPayload;
import com.homfo.sms.request.NaverSmsRequest;
import com.homfo.sms.response.NaverSmsResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.reactive.function.client.WebClient;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class NaverSmsClient implements SendSmsPort {
    @Value("${naver-cloud.sms.accessKey}")
    private String accessKey;

    @Value("${naver-cloud.sms.secretKey}")
    private String secretKey;

    @Value("${naver-cloud.sms.serviceId}")
    private String serviceId;

    @Value("${naver-cloud.sms.senderPhone}")
    private String senderPhone;

    private static final String NCP_TIMESTAMP_HEADER = "x-ncp-apigw-timestamp";

    private static final String NCP_ACCESS_KEY_HEADER = "x-ncp-iam-access-key";

    private static final String NCP_SIGNATURE_HEADER = "x-ncp-apigw-signature-v2";

    private final WebClient webClient;

    @Autowired
    public NaverSmsClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://sens.apigw.ntruss.com/sms/v2/services").build();
    }


    public void sendSms(@NonNull SmsSendDto smsSendDto) {
        try {
            NaverSmsResponse response = sendSmsToNcp(smsSendDto);

            if (!Objects.equals(response.getStatusCode(), "202")) {
                throw new ThirdPartyUnavailableException(SmsErrorCode.FAILED_SEND_SMS);
            }
        } catch (Exception e) {
            log.warn("NaverSmsClient sendSms error " + e);
            throw new ThirdPartyUnavailableException(SmsErrorCode.FAILED_SEND_SMS);
        }
    }

    private NaverSmsResponse sendSmsToNcp(SmsSendDto smsSendDto) throws JsonProcessingException, RestClientException, InvalidKeyException, NoSuchAlgorithmException, URISyntaxException {
        HttpHeaders headers = getNcpHttpHeaders();
        List<NaverSmsPayload> messages = new ArrayList<>();
        NaverSmsPayload payload = new NaverSmsPayload(smsSendDto.phoneNumber(), "", smsSendDto.message());
        messages.add(payload);

        NaverSmsRequest request = new NaverSmsRequest(
                "SMS",
                "COMM",
                "82",
                senderPhone,
                smsSendDto.message(),
                messages
        );
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(request);

        return webClient.post()
                .uri(uriBuilder -> uriBuilder.path("/"+ serviceId + "/messages").build())
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
