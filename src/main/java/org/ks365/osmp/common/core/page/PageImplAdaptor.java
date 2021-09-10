package org.ks365.osmp.common.core.page;


import com.github.pagehelper.PageInfo;
import org.ks365.osmp.common.entity.PageEntity;
import org.springframework.data.domain.*;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;


public class PageImplAdaptor<T> implements Page<T> {

    private final PageImpl<T> pageImpl;

    public PageImplAdaptor(List<T> content) {
        this(content, 1, 15);
    }

    public PageImplAdaptor(List<T> content, PageEntity page) {
        this(content, page.getPage(), page.getSize());
    }

    public PageImplAdaptor(List<T> content, int pageNum, int pageSize) {
        if (pageSize < 0) {
            throw new RuntimeException(" pageSize must than one!");
        }
        if (pageNum < 0) {
            throw new RuntimeException(" pageNum must than one!");
        }
        PageInfo<T> pageInfo = new PageInfo<>(content);
        long        total    = pageInfo.getTotal();
        Pageable    pageable = null;
        if (total > 0) {
            pageable = PageRequest.of(pageInfo.getPageNum() - 1, pageInfo.getPageSize());
        } else {
            pageable = PageRequest.of(pageNum - 1, pageSize);
        }
        this.pageImpl = new PageImpl<T>(content, pageable, total);
    }

    @Override
    public int getTotalPages() {
        return pageImpl.getTotalPages();
    }

    @Override
    public long getTotalElements() {
        return pageImpl.getTotalElements();
    }

    @Override
    public int getNumber() {
        return pageImpl.getNumber();
    }

    @Override
    public int getSize() {
        return pageImpl.getSize();
    }

    @Override
    public int getNumberOfElements() {
        return pageImpl.getNumberOfElements();
    }

    @Override
    public List<T> getContent() {
        return pageImpl.getContent();
    }

    @Override
    public boolean hasContent() {
        return pageImpl.hasContent();
    }

    @Override
    public Sort getSort() {
        return pageImpl.getSort();
    }

    @Override
    public boolean isFirst() {
        return pageImpl.isFirst();
    }

    @Override
    public boolean isLast() {
        return pageImpl.isLast();
    }

    @Override
    public boolean hasNext() {
        return pageImpl.hasNext();
    }

    @Override
    public boolean hasPrevious() {
        return pageImpl.hasPrevious();
    }

    @Override
    public Pageable nextPageable() {
        return pageImpl.nextPageable();
    }

    @Override
    public Pageable previousPageable() {
        return pageImpl.previousPageable();
    }

    @Override
    public <U> Page<U> map(Function<? super T, ? extends U> converter) {
        return pageImpl.map(converter);
    }

    @Override
    public Iterator<T> iterator() {
        return pageImpl.iterator();
    }
}
