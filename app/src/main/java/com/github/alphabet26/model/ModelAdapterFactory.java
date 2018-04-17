package com.github.alphabet26.model;

import com.ryanharter.auto.value.moshi.MoshiAdapterFactory;
import com.squareup.moshi.JsonAdapter;

/**
 * thing
 */
@MoshiAdapterFactory
public abstract class ModelAdapterFactory implements JsonAdapter.Factory {
    /**
     * creates instance
     * @return returns json adapter
     */
    public static JsonAdapter.Factory create() {
        return new AutoValueMoshi_ModelAdapterFactory();
    }
}
