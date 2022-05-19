package com.vmo.core.modules.models.requests;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;

public class AllDataPageRequest implements Pageable {
    private Sort sort;

    public AllDataPageRequest() {
        this(Sort.unsorted());
    }

    public AllDataPageRequest(Sort sort) {
        this.sort = sort;
    }

    public AllDataPageRequest(String property, String direction) {
        this.sort = Sort.by(Sort.Direction.fromString(direction), property);
    }


    @Override
    public boolean isPaged() {
        return false;
    }

    @Override
    public boolean isUnpaged() {
        return Pageable.super.isUnpaged();
    }

    @Override
    public int getPageNumber() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getPageSize() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getOffset() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public Sort getSortOr(Sort sort) {
        return Pageable.super.getSortOr(sort);
    }

    @Override
    public Pageable next() {
        return this;
    }

    @Override
    public Pageable previousOrFirst() {
        return this;
    }

    @Override
    public Pageable first() {
        return this;
    }

    @Override
    public boolean hasPrevious() {
        return false;
    }

    @Override
    public Optional<Pageable> toOptional() {
        return Pageable.super.toOptional();
    }
}
