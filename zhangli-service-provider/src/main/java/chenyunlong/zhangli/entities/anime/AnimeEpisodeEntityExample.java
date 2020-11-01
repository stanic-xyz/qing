package chenyunlong.zhangli.entities.anime;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AnimeEpisodeEntityExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private Integer limit;

    private Long offset;

    public AnimeEpisodeEntityExample() {
        oredCriteria = new ArrayList<>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setOffset(Long offset) {
        this.offset = offset;
    }

    public Long getOffset() {
        return offset;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andAnimeIdIsNull() {
            addCriterion("anime_id is null");
            return (Criteria) this;
        }

        public Criteria andAnimeIdIsNotNull() {
            addCriterion("anime_id is not null");
            return (Criteria) this;
        }

        public Criteria andAnimeIdEqualTo(Long value) {
            addCriterion("anime_id =", value, "animeId");
            return (Criteria) this;
        }

        public Criteria andAnimeIdNotEqualTo(Long value) {
            addCriterion("anime_id <>", value, "animeId");
            return (Criteria) this;
        }

        public Criteria andAnimeIdGreaterThan(Long value) {
            addCriterion("anime_id >", value, "animeId");
            return (Criteria) this;
        }

        public Criteria andAnimeIdGreaterThanOrEqualTo(Long value) {
            addCriterion("anime_id >=", value, "animeId");
            return (Criteria) this;
        }

        public Criteria andAnimeIdLessThan(Long value) {
            addCriterion("anime_id <", value, "animeId");
            return (Criteria) this;
        }

        public Criteria andAnimeIdLessThanOrEqualTo(Long value) {
            addCriterion("anime_id <=", value, "animeId");
            return (Criteria) this;
        }

        public Criteria andAnimeIdIn(List<Long> values) {
            addCriterion("anime_id in", values, "animeId");
            return (Criteria) this;
        }

        public Criteria andAnimeIdNotIn(List<Long> values) {
            addCriterion("anime_id not in", values, "animeId");
            return (Criteria) this;
        }

        public Criteria andAnimeIdBetween(Long value1, Long value2) {
            addCriterion("anime_id between", value1, value2, "animeId");
            return (Criteria) this;
        }

        public Criteria andAnimeIdNotBetween(Long value1, Long value2) {
            addCriterion("anime_id not between", value1, value2, "animeId");
            return (Criteria) this;
        }

        public Criteria andNameIsNull() {
            addCriterion("`name` is null");
            return (Criteria) this;
        }

        public Criteria andNameIsNotNull() {
            addCriterion("`name` is not null");
            return (Criteria) this;
        }

        public Criteria andNameEqualTo(String value) {
            addCriterion("`name` =", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotEqualTo(String value) {
            addCriterion("`name` <>", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThan(String value) {
            addCriterion("`name` >", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThanOrEqualTo(String value) {
            addCriterion("`name` >=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThan(String value) {
            addCriterion("`name` <", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThanOrEqualTo(String value) {
            addCriterion("`name` <=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLike(String value) {
            addCriterion("`name` like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotLike(String value) {
            addCriterion("`name` not like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameIn(List<String> values) {
            addCriterion("`name` in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotIn(List<String> values) {
            addCriterion("`name` not in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameBetween(String value1, String value2) {
            addCriterion("`name` between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotBetween(String value1, String value2) {
            addCriterion("`name` not between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("`status` is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("`status` is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Integer value) {
            addCriterion("`status` =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Integer value) {
            addCriterion("`status` <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Integer value) {
            addCriterion("`status` >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("`status` >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Integer value) {
            addCriterion("`status` <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Integer value) {
            addCriterion("`status` <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Integer> values) {
            addCriterion("`status` in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Integer> values) {
            addCriterion("`status` not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Integer value1, Integer value2) {
            addCriterion("`status` between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("`status` not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andUploadderNameIsNull() {
            addCriterion("uploadder_name is null");
            return (Criteria) this;
        }

        public Criteria andUploadderNameIsNotNull() {
            addCriterion("uploadder_name is not null");
            return (Criteria) this;
        }

        public Criteria andUploadderNameEqualTo(String value) {
            addCriterion("uploadder_name =", value, "uploadderName");
            return (Criteria) this;
        }

        public Criteria andUploadderNameNotEqualTo(String value) {
            addCriterion("uploadder_name <>", value, "uploadderName");
            return (Criteria) this;
        }

        public Criteria andUploadderNameGreaterThan(String value) {
            addCriterion("uploadder_name >", value, "uploadderName");
            return (Criteria) this;
        }

        public Criteria andUploadderNameGreaterThanOrEqualTo(String value) {
            addCriterion("uploadder_name >=", value, "uploadderName");
            return (Criteria) this;
        }

        public Criteria andUploadderNameLessThan(String value) {
            addCriterion("uploadder_name <", value, "uploadderName");
            return (Criteria) this;
        }

        public Criteria andUploadderNameLessThanOrEqualTo(String value) {
            addCriterion("uploadder_name <=", value, "uploadderName");
            return (Criteria) this;
        }

        public Criteria andUploadderNameLike(String value) {
            addCriterion("uploadder_name like", value, "uploadderName");
            return (Criteria) this;
        }

        public Criteria andUploadderNameNotLike(String value) {
            addCriterion("uploadder_name not like", value, "uploadderName");
            return (Criteria) this;
        }

        public Criteria andUploadderNameIn(List<String> values) {
            addCriterion("uploadder_name in", values, "uploadderName");
            return (Criteria) this;
        }

        public Criteria andUploadderNameNotIn(List<String> values) {
            addCriterion("uploadder_name not in", values, "uploadderName");
            return (Criteria) this;
        }

        public Criteria andUploadderNameBetween(String value1, String value2) {
            addCriterion("uploadder_name between", value1, value2, "uploadderName");
            return (Criteria) this;
        }

        public Criteria andUploadderNameNotBetween(String value1, String value2) {
            addCriterion("uploadder_name not between", value1, value2, "uploadderName");
            return (Criteria) this;
        }

        public Criteria andUploaderIdIsNull() {
            addCriterion("uploader_id is null");
            return (Criteria) this;
        }

        public Criteria andUploaderIdIsNotNull() {
            addCriterion("uploader_id is not null");
            return (Criteria) this;
        }

        public Criteria andUploaderIdEqualTo(Long value) {
            addCriterion("uploader_id =", value, "uploaderId");
            return (Criteria) this;
        }

        public Criteria andUploaderIdNotEqualTo(Long value) {
            addCriterion("uploader_id <>", value, "uploaderId");
            return (Criteria) this;
        }

        public Criteria andUploaderIdGreaterThan(Long value) {
            addCriterion("uploader_id >", value, "uploaderId");
            return (Criteria) this;
        }

        public Criteria andUploaderIdGreaterThanOrEqualTo(Long value) {
            addCriterion("uploader_id >=", value, "uploaderId");
            return (Criteria) this;
        }

        public Criteria andUploaderIdLessThan(Long value) {
            addCriterion("uploader_id <", value, "uploaderId");
            return (Criteria) this;
        }

        public Criteria andUploaderIdLessThanOrEqualTo(Long value) {
            addCriterion("uploader_id <=", value, "uploaderId");
            return (Criteria) this;
        }

        public Criteria andUploaderIdIn(List<Long> values) {
            addCriterion("uploader_id in", values, "uploaderId");
            return (Criteria) this;
        }

        public Criteria andUploaderIdNotIn(List<Long> values) {
            addCriterion("uploader_id not in", values, "uploaderId");
            return (Criteria) this;
        }

        public Criteria andUploaderIdBetween(Long value1, Long value2) {
            addCriterion("uploader_id between", value1, value2, "uploaderId");
            return (Criteria) this;
        }

        public Criteria andUploaderIdNotBetween(Long value1, Long value2) {
            addCriterion("uploader_id not between", value1, value2, "uploaderId");
            return (Criteria) this;
        }

        public Criteria andUploadTimeIsNull() {
            addCriterion("upload_time is null");
            return (Criteria) this;
        }

        public Criteria andUploadTimeIsNotNull() {
            addCriterion("upload_time is not null");
            return (Criteria) this;
        }

        public Criteria andUploadTimeEqualTo(LocalDateTime value) {
            addCriterion("upload_time =", value, "uploadTime");
            return (Criteria) this;
        }

        public Criteria andUploadTimeNotEqualTo(LocalDateTime value) {
            addCriterion("upload_time <>", value, "uploadTime");
            return (Criteria) this;
        }

        public Criteria andUploadTimeGreaterThan(LocalDateTime value) {
            addCriterion("upload_time >", value, "uploadTime");
            return (Criteria) this;
        }

        public Criteria andUploadTimeGreaterThanOrEqualTo(LocalDateTime value) {
            addCriterion("upload_time >=", value, "uploadTime");
            return (Criteria) this;
        }

        public Criteria andUploadTimeLessThan(LocalDateTime value) {
            addCriterion("upload_time <", value, "uploadTime");
            return (Criteria) this;
        }

        public Criteria andUploadTimeLessThanOrEqualTo(LocalDateTime value) {
            addCriterion("upload_time <=", value, "uploadTime");
            return (Criteria) this;
        }

        public Criteria andUploadTimeIn(List<LocalDateTime> values) {
            addCriterion("upload_time in", values, "uploadTime");
            return (Criteria) this;
        }

        public Criteria andUploadTimeNotIn(List<LocalDateTime> values) {
            addCriterion("upload_time not in", values, "uploadTime");
            return (Criteria) this;
        }

        public Criteria andUploadTimeBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("upload_time between", value1, value2, "uploadTime");
            return (Criteria) this;
        }

        public Criteria andUploadTimeNotBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("upload_time not between", value1, value2, "uploadTime");
            return (Criteria) this;
        }

        public Criteria andUrl1IsNull() {
            addCriterion("url1 is null");
            return (Criteria) this;
        }

        public Criteria andUrl1IsNotNull() {
            addCriterion("url1 is not null");
            return (Criteria) this;
        }

        public Criteria andUrl1EqualTo(String value) {
            addCriterion("url1 =", value, "url1");
            return (Criteria) this;
        }

        public Criteria andUrl1NotEqualTo(String value) {
            addCriterion("url1 <>", value, "url1");
            return (Criteria) this;
        }

        public Criteria andUrl1GreaterThan(String value) {
            addCriterion("url1 >", value, "url1");
            return (Criteria) this;
        }

        public Criteria andUrl1GreaterThanOrEqualTo(String value) {
            addCriterion("url1 >=", value, "url1");
            return (Criteria) this;
        }

        public Criteria andUrl1LessThan(String value) {
            addCriterion("url1 <", value, "url1");
            return (Criteria) this;
        }

        public Criteria andUrl1LessThanOrEqualTo(String value) {
            addCriterion("url1 <=", value, "url1");
            return (Criteria) this;
        }

        public Criteria andUrl1Like(String value) {
            addCriterion("url1 like", value, "url1");
            return (Criteria) this;
        }

        public Criteria andUrl1NotLike(String value) {
            addCriterion("url1 not like", value, "url1");
            return (Criteria) this;
        }

        public Criteria andUrl1In(List<String> values) {
            addCriterion("url1 in", values, "url1");
            return (Criteria) this;
        }

        public Criteria andUrl1NotIn(List<String> values) {
            addCriterion("url1 not in", values, "url1");
            return (Criteria) this;
        }

        public Criteria andUrl1Between(String value1, String value2) {
            addCriterion("url1 between", value1, value2, "url1");
            return (Criteria) this;
        }

        public Criteria andUrl1NotBetween(String value1, String value2) {
            addCriterion("url1 not between", value1, value2, "url1");
            return (Criteria) this;
        }

        public Criteria andUrl3IsNull() {
            addCriterion("url3 is null");
            return (Criteria) this;
        }

        public Criteria andUrl3IsNotNull() {
            addCriterion("url3 is not null");
            return (Criteria) this;
        }

        public Criteria andUrl3EqualTo(String value) {
            addCriterion("url3 =", value, "url3");
            return (Criteria) this;
        }

        public Criteria andUrl3NotEqualTo(String value) {
            addCriterion("url3 <>", value, "url3");
            return (Criteria) this;
        }

        public Criteria andUrl3GreaterThan(String value) {
            addCriterion("url3 >", value, "url3");
            return (Criteria) this;
        }

        public Criteria andUrl3GreaterThanOrEqualTo(String value) {
            addCriterion("url3 >=", value, "url3");
            return (Criteria) this;
        }

        public Criteria andUrl3LessThan(String value) {
            addCriterion("url3 <", value, "url3");
            return (Criteria) this;
        }

        public Criteria andUrl3LessThanOrEqualTo(String value) {
            addCriterion("url3 <=", value, "url3");
            return (Criteria) this;
        }

        public Criteria andUrl3Like(String value) {
            addCriterion("url3 like", value, "url3");
            return (Criteria) this;
        }

        public Criteria andUrl3NotLike(String value) {
            addCriterion("url3 not like", value, "url3");
            return (Criteria) this;
        }

        public Criteria andUrl3In(List<String> values) {
            addCriterion("url3 in", values, "url3");
            return (Criteria) this;
        }

        public Criteria andUrl3NotIn(List<String> values) {
            addCriterion("url3 not in", values, "url3");
            return (Criteria) this;
        }

        public Criteria andUrl3Between(String value1, String value2) {
            addCriterion("url3 between", value1, value2, "url3");
            return (Criteria) this;
        }

        public Criteria andUrl3NotBetween(String value1, String value2) {
            addCriterion("url3 not between", value1, value2, "url3");
            return (Criteria) this;
        }

        public Criteria andUrl2IsNull() {
            addCriterion("url2 is null");
            return (Criteria) this;
        }

        public Criteria andUrl2IsNotNull() {
            addCriterion("url2 is not null");
            return (Criteria) this;
        }

        public Criteria andUrl2EqualTo(String value) {
            addCriterion("url2 =", value, "url2");
            return (Criteria) this;
        }

        public Criteria andUrl2NotEqualTo(String value) {
            addCriterion("url2 <>", value, "url2");
            return (Criteria) this;
        }

        public Criteria andUrl2GreaterThan(String value) {
            addCriterion("url2 >", value, "url2");
            return (Criteria) this;
        }

        public Criteria andUrl2GreaterThanOrEqualTo(String value) {
            addCriterion("url2 >=", value, "url2");
            return (Criteria) this;
        }

        public Criteria andUrl2LessThan(String value) {
            addCriterion("url2 <", value, "url2");
            return (Criteria) this;
        }

        public Criteria andUrl2LessThanOrEqualTo(String value) {
            addCriterion("url2 <=", value, "url2");
            return (Criteria) this;
        }

        public Criteria andUrl2Like(String value) {
            addCriterion("url2 like", value, "url2");
            return (Criteria) this;
        }

        public Criteria andUrl2NotLike(String value) {
            addCriterion("url2 not like", value, "url2");
            return (Criteria) this;
        }

        public Criteria andUrl2In(List<String> values) {
            addCriterion("url2 in", values, "url2");
            return (Criteria) this;
        }

        public Criteria andUrl2NotIn(List<String> values) {
            addCriterion("url2 not in", values, "url2");
            return (Criteria) this;
        }

        public Criteria andUrl2Between(String value1, String value2) {
            addCriterion("url2 between", value1, value2, "url2");
            return (Criteria) this;
        }

        public Criteria andUrl2NotBetween(String value1, String value2) {
            addCriterion("url2 not between", value1, value2, "url2");
            return (Criteria) this;
        }

        public Criteria andOrderNoIsNull() {
            addCriterion("order_no is null");
            return (Criteria) this;
        }

        public Criteria andOrderNoIsNotNull() {
            addCriterion("order_no is not null");
            return (Criteria) this;
        }

        public Criteria andOrderNoEqualTo(Integer value) {
            addCriterion("order_no =", value, "orderNo");
            return (Criteria) this;
        }

        public Criteria andOrderNoNotEqualTo(Integer value) {
            addCriterion("order_no <>", value, "orderNo");
            return (Criteria) this;
        }

        public Criteria andOrderNoGreaterThan(Integer value) {
            addCriterion("order_no >", value, "orderNo");
            return (Criteria) this;
        }

        public Criteria andOrderNoGreaterThanOrEqualTo(Integer value) {
            addCriterion("order_no >=", value, "orderNo");
            return (Criteria) this;
        }

        public Criteria andOrderNoLessThan(Integer value) {
            addCriterion("order_no <", value, "orderNo");
            return (Criteria) this;
        }

        public Criteria andOrderNoLessThanOrEqualTo(Integer value) {
            addCriterion("order_no <=", value, "orderNo");
            return (Criteria) this;
        }

        public Criteria andOrderNoIn(List<Integer> values) {
            addCriterion("order_no in", values, "orderNo");
            return (Criteria) this;
        }

        public Criteria andOrderNoNotIn(List<Integer> values) {
            addCriterion("order_no not in", values, "orderNo");
            return (Criteria) this;
        }

        public Criteria andOrderNoBetween(Integer value1, Integer value2) {
            addCriterion("order_no between", value1, value2, "orderNo");
            return (Criteria) this;
        }

        public Criteria andOrderNoNotBetween(Integer value1, Integer value2) {
            addCriterion("order_no not between", value1, value2, "orderNo");
            return (Criteria) this;
        }
    }

    /**
     */
    public static class Criteria extends GeneratedCriteria {
        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}