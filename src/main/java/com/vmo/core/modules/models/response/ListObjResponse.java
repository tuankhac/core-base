package com.vmo.core.modules.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vmo.core.common.CommonResponseMessages;

import java.util.List;

public class ListObjResponse<T> implements CommonResponseMessages {
    private List<T> data;
    @JsonProperty("total_item")
    private int totalItem;
    private int page;
    @JsonProperty("page_size")
    private int pageSize;
    @JsonProperty("total_page")
    private int totalPage;

    @Deprecated
    /**
     * Only for json mapper
     * Dont use for creating API response
     */
    public ListObjResponse() {
    }

    public ListObjResponse(int page, int pageSize) {
        if (page < 0) {
            throw new IllegalArgumentException(PAGE_NUMBER_NOT_NOT_NEGATIVE);
        }
        if (pageSize < 1) {
            throw new IllegalArgumentException(PAGE_SIZE_MUST_POSITIVE);
        }
        this.page = page+1;
        this.pageSize = pageSize;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public int getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(int totalItem) {
        this.totalItem = totalItem;
        if (pageSize > 0) {
            totalPage = (int) Math.ceil((float) totalItem / pageSize);
        }
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page+1;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
        if (pageSize > 0) {
            totalPage = (int) Math.ceil((float) totalItem / pageSize);
        }
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
}
