package io.github.teamomo.moment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data @AllArgsConstructor
@Schema(
    name = "Response",
    description = "Schema to hold successful response information"
)
public class ResponseDto {

  @Schema(
      description = "Status code in the response"
  )
  private HttpStatus errorCode;

  @Schema(
      description = "Status message in the response"
  )
  private String statusMsg;


}
