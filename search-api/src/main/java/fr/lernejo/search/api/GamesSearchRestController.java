package fr.lernejo.search.api;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
public class GamesSearchRestController {
    private final RestHighLevelClient client;

    @Autowired
    public GamesSearchRestController(RestHighLevelClient client) {
        this.client = client;
    }

    @GetMapping("/api/games")
    public List<Map<String, Object>> searchGamesFromElasticSearch(@RequestParam String query) throws IOException {
        SearchRequest searchRequest = buildSearchRequest("games", query, 100);
        SearchResponse searchResponse = launchSearchRequest(searchRequest);
        return extractSearchHitsAsMapList(searchResponse);
    }

    private SearchRequest buildSearchRequest(String index, String query, int size) {
        QueryStringQueryBuilder queryBuilder = QueryBuilders.queryStringQuery(query);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder().query(queryBuilder).size(size);
        return new SearchRequest(index).source(sourceBuilder);
    }

    private SearchResponse launchSearchRequest(SearchRequest searchRequest) throws IOException {
        return client.search(searchRequest, RequestOptions.DEFAULT);
    }

    private List<Map<String, Object>> extractSearchHitsAsMapList(SearchResponse searchResponse) {
        List<Map<String, Object>> gameList = new ArrayList<>();
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            gameList.add(hit.getSourceAsMap());
        }
        return gameList;
    }
}
