package com.sducat.common.util;

import java.util.HashMap;
import java.util.Map;

public class MapBuildUtil {

    private Map<String, Object> params;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final MapBuildUtil util;

        public Builder() {
            util = new MapBuildUtil();
            util.params = new HashMap<>();
        }

        public Builder data(String key, Object param) {
            util.params.put(key, param);
            return this;
        }

        public Map<String, Object> get() {
            return util.params;
        }

    }

}