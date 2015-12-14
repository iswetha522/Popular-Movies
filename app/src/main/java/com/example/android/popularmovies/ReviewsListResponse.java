package com.example.android.popularmovies;

import java.io.Serializable;

public class ReviewsListResponse implements Serializable {
    private int page;
    private Review[] results;
    private int total_pages;
    private int total_results;

    public ReviewsListResponse(){

    }

    public int getPage(){return page;}

    public void setPage(int page){this.page = page;}

    public Review[] getResults() {return results;}

    public void setResults(Review[] results){this.results = results;}

    public int getTotal_pages(){return total_pages;}

    public void setTotal_pages(int total_pages){this.total_pages = total_pages;}

    public int getTotal_results(){return total_results;}

    public void setTotal_results(int total_results){this.total_results = total_results;}
}
