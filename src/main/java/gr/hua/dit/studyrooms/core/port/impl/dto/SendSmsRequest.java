package gr.hua.dit.studyrooms.core.port.impl.dto;

public record SendSmsRequest(
        String e164,
        String content
) {}
