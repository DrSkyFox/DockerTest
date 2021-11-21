package org.messager.dao;



import lombok.*;


@Builder(toBuilder = true)
@Getter
@Setter(value = AccessLevel.PUBLIC)
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMessage {

    private Integer code;
    private String status;


}
