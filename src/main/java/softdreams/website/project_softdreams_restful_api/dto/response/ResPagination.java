package softdreams.website.project_softdreams_restful_api.dto.response;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResPagination {
    private Meta meta;
    private Object data;

    @Getter
    @Setter
    public static class Meta {
        // Trang hiện tại
        private int page;
        // Số phần tử trên mỗi trang
        private int pageSize;
        // Tổng số trang
        private int pages;
        // Tổng số phần tử
        private long total;
    }

    public static ResPagination.Meta addMeta(Page<?> object, Pageable pageable){
        ResPagination.Meta meta = new ResPagination.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(object.getTotalPages());
        meta.setTotal(object.getTotalElements());
        return meta;
    }
}
