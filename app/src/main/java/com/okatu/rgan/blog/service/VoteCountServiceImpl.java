package com.okatu.rgan.blog.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class VoteCountServiceImpl {

    private static LoadingCache<Long, AtomicInteger> voteCountCache =
        CacheBuilder.newBuilder()
            .maximumSize(200)
            .expireAfterAccess(24, TimeUnit.HOURS)
            .build(new CacheLoader<Long, AtomicInteger>() {
                @Override
                public AtomicInteger load(Long aLong) throws Exception {
                    return null;
                }
            });


}
