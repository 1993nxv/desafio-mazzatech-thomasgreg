package com.thomasgreg.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PageResponseDTO<T> {
    private List<T> content;
    private PageableDTO pageable;
    private boolean last;
    private int totalPages;
    private long totalElements;
    private int size;
    private int number;
    private SortDTO sort;
    private boolean first;
    private int numberOfElements;
    private boolean empty;

    // Classes internas para estruturas aninhadas
    public static class PageableDTO {
        private SortDTO sort;
        private int offset;
        private int pageSize;
        private int pageNumber;
        private boolean unpaged;
        private boolean paged;

        // Getters e Setters
        public SortDTO getSort() { return sort; }
        public void setSort(SortDTO sort) { this.sort = sort; }
        public int getOffset() { return offset; }
        public void setOffset(int offset) { this.offset = offset; }
        public int getPageSize() { return pageSize; }
        public void setPageSize(int pageSize) { this.pageSize = pageSize; }
        public int getPageNumber() { return pageNumber; }
        public void setPageNumber(int pageNumber) { this.pageNumber = pageNumber; }
        public boolean isUnpaged() { return unpaged; }
        public void setUnpaged(boolean unpaged) { this.unpaged = unpaged; }
        public boolean isPaged() { return paged; }
        public void setPaged(boolean paged) { this.paged = paged; }
    }

    public static class SortDTO {
        private boolean empty;
        private boolean sorted;
        private boolean unsorted;

        // Getters e Setters
        public boolean isEmpty() { return empty; }
        public void setEmpty(boolean empty) { this.empty = empty; }
        public boolean isSorted() { return sorted; }
        public void setSorted(boolean sorted) { this.sorted = sorted; }
        public boolean isUnsorted() { return unsorted; }
        public void setUnsorted(boolean unsorted) { this.unsorted = unsorted; }
    }

    // Getters e Setters principais
    public List<T> getContent() { return content; }
    public void setContent(List<T> content) { this.content = content; }
    public PageableDTO getPageable() { return pageable; }
    public void setPageable(PageableDTO pageable) { this.pageable = pageable; }
    public boolean isLast() { return last; }
    public void setLast(boolean last) { this.last = last; }
    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
    public long getTotalElements() { return totalElements; }
    public void setTotalElements(long totalElements) { this.totalElements = totalElements; }
    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }
    public int getNumber() { return number; }
    public void setNumber(int number) { this.number = number; }
    public SortDTO getSort() { return sort; }
    public void setSort(SortDTO sort) { this.sort = sort; }
    public boolean isFirst() { return first; }
    public void setFirst(boolean first) { this.first = first; }
    public int getNumberOfElements() { return numberOfElements; }
    public void setNumberOfElements(int numberOfElements) { this.numberOfElements = numberOfElements; }
    public boolean isEmpty() { return empty; }
    public void setEmpty(boolean empty) { this.empty = empty; }
}
