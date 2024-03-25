package com.homfo.naver.sms.request;

import com.homfo.sms.dto.SmsSendDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NaverSmsRequest {
  String type;
  String contentType;
  String countryCode;
  String from;
  String content;
  List<SmsSendDto> messages;
}
