package com.iny.side.chat.application.port;

import com.iny.side.chat.infrastructure.external.dto.AiChatRequestDto;
import com.iny.side.chat.infrastructure.external.dto.AiChatResponseDto;
import com.iny.side.common.result.Result;

public interface AiClient {

    Result<AiChatResponseDto> sendChatMessage(AiChatRequestDto requestDto);
}
