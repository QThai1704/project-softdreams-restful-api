package softdreams.website.project_softdreams_restful_api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReceiverReq {
    private String receiverName;
    private String receiverAddress;
    private String receiverPhone;
    private String paymentMethod;
}
