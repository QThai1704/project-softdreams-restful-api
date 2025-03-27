package softdreams.website.project_softdreams_restful_api.service;

import softdreams.website.project_softdreams_restful_api.domain.Cart;
import softdreams.website.project_softdreams_restful_api.domain.User;
import softdreams.website.project_softdreams_restful_api.dto.response.CartDetailRes;

public interface CartService {
    public Cart fetchByUser(User user);
    public CartDetailRes convertCartDetailRes(String email);
}
