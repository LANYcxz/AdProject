package com.ad.search;

import com.ad.search.vo.SearchRequest;
import com.ad.search.vo.SearchResponse;

/**
 * Created By
 *
 * @author ZhanXiaowei
 * ^_^
 */
public interface ISearch {
        SearchResponse fetchAds(SearchRequest request);
}
