package com.elanyudho.core.base.data.pagination

/**
 * Wrapper for a single page of results from the API/repository.
 *
 * @param T The type of items in the page
 * @param items Items returned in this page
 * @param currentPage The page number of this result
 * @param totalPages Total pages available (null if unknown)
 * @param totalItems Total items across all pages (null if unknown)
 */
data class PagedResult<T>(
    val items: List<T>,
    val currentPage: Int,
    val totalPages: Int? = null,
    val totalItems: Int? = null
) {
    /**
     * Determines if more pages are available.
     * If [totalPages] is known, compares against it.
     * Otherwise, assumes more pages exist if [items.size] >= [pageSize].
     */
    fun hasMorePages(pageSize: Int): Boolean {
        return if (totalPages != null) {
            currentPage < totalPages
        } else {
            items.size >= pageSize
        }
    }
}
