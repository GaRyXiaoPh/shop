package cn.kt.mall.shop.coupon.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserCouponLogEntityExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public UserCouponLogEntityExample() {
        oredCriteria = new ArrayList<Criteria>();
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

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
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
            addCriterion("`id` is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("`id` is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(String value) {
            addCriterion("`id` =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(String value) {
            addCriterion("`id` <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(String value) {
            addCriterion("`id` >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(String value) {
            addCriterion("`id` >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(String value) {
            addCriterion("`id` <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(String value) {
            addCriterion("`id` <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLike(String value) {
            addCriterion("`id` like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotLike(String value) {
            addCriterion("`id` not like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<String> values) {
            addCriterion("`id` in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<String> values) {
            addCriterion("`id` not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(String value1, String value2) {
            addCriterion("`id` between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(String value1, String value2) {
            addCriterion("`id` not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andUseridIsNull() {
            addCriterion("`userId` is null");
            return (Criteria) this;
        }

        public Criteria andUseridIsNotNull() {
            addCriterion("`userId` is not null");
            return (Criteria) this;
        }

        public Criteria andUseridEqualTo(String value) {
            addCriterion("`userId` =", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridNotEqualTo(String value) {
            addCriterion("`userId` <>", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridGreaterThan(String value) {
            addCriterion("`userId` >", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridGreaterThanOrEqualTo(String value) {
            addCriterion("`userId` >=", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridLessThan(String value) {
            addCriterion("`userId` <", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridLessThanOrEqualTo(String value) {
            addCriterion("`userId` <=", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridLike(String value) {
            addCriterion("`userId` like", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridNotLike(String value) {
            addCriterion("`userId` not like", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridIn(List<String> values) {
            addCriterion("`userId` in", values, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridNotIn(List<String> values) {
            addCriterion("`userId` not in", values, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridBetween(String value1, String value2) {
            addCriterion("`userId` between", value1, value2, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridNotBetween(String value1, String value2) {
            addCriterion("`userId` not between", value1, value2, "userid");
            return (Criteria) this;
        }

        public Criteria andTradeidIsNull() {
            addCriterion("`tradeId` is null");
            return (Criteria) this;
        }

        public Criteria andTradeidIsNotNull() {
            addCriterion("`tradeId` is not null");
            return (Criteria) this;
        }

        public Criteria andTradeidEqualTo(String value) {
            addCriterion("`tradeId` =", value, "tradeid");
            return (Criteria) this;
        }

        public Criteria andTradeidNotEqualTo(String value) {
            addCriterion("`tradeId` <>", value, "tradeid");
            return (Criteria) this;
        }

        public Criteria andTradeidGreaterThan(String value) {
            addCriterion("`tradeId` >", value, "tradeid");
            return (Criteria) this;
        }

        public Criteria andTradeidGreaterThanOrEqualTo(String value) {
            addCriterion("`tradeId` >=", value, "tradeid");
            return (Criteria) this;
        }

        public Criteria andTradeidLessThan(String value) {
            addCriterion("`tradeId` <", value, "tradeid");
            return (Criteria) this;
        }

        public Criteria andTradeidLessThanOrEqualTo(String value) {
            addCriterion("`tradeId` <=", value, "tradeid");
            return (Criteria) this;
        }

        public Criteria andTradeidLike(String value) {
            addCriterion("`tradeId` like", value, "tradeid");
            return (Criteria) this;
        }

        public Criteria andTradeidNotLike(String value) {
            addCriterion("`tradeId` not like", value, "tradeid");
            return (Criteria) this;
        }

        public Criteria andTradeidIn(List<String> values) {
            addCriterion("`tradeId` in", values, "tradeid");
            return (Criteria) this;
        }

        public Criteria andTradeidNotIn(List<String> values) {
            addCriterion("`tradeId` not in", values, "tradeid");
            return (Criteria) this;
        }

        public Criteria andTradeidBetween(String value1, String value2) {
            addCriterion("`tradeId` between", value1, value2, "tradeid");
            return (Criteria) this;
        }

        public Criteria andTradeidNotBetween(String value1, String value2) {
            addCriterion("`tradeId` not between", value1, value2, "tradeid");
            return (Criteria) this;
        }

        public Criteria andBeforenumIsNull() {
            addCriterion("`beforeNum` is null");
            return (Criteria) this;
        }

        public Criteria andBeforenumIsNotNull() {
            addCriterion("`beforeNum` is not null");
            return (Criteria) this;
        }

        public Criteria andBeforenumEqualTo(BigDecimal value) {
            addCriterion("`beforeNum` =", value, "beforenum");
            return (Criteria) this;
        }

        public Criteria andBeforenumNotEqualTo(BigDecimal value) {
            addCriterion("`beforeNum` <>", value, "beforenum");
            return (Criteria) this;
        }

        public Criteria andBeforenumGreaterThan(BigDecimal value) {
            addCriterion("`beforeNum` >", value, "beforenum");
            return (Criteria) this;
        }

        public Criteria andBeforenumGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("`beforeNum` >=", value, "beforenum");
            return (Criteria) this;
        }

        public Criteria andBeforenumLessThan(BigDecimal value) {
            addCriterion("`beforeNum` <", value, "beforenum");
            return (Criteria) this;
        }

        public Criteria andBeforenumLessThanOrEqualTo(BigDecimal value) {
            addCriterion("`beforeNum` <=", value, "beforenum");
            return (Criteria) this;
        }

        public Criteria andBeforenumIn(List<BigDecimal> values) {
            addCriterion("`beforeNum` in", values, "beforenum");
            return (Criteria) this;
        }

        public Criteria andBeforenumNotIn(List<BigDecimal> values) {
            addCriterion("`beforeNum` not in", values, "beforenum");
            return (Criteria) this;
        }

        public Criteria andBeforenumBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("`beforeNum` between", value1, value2, "beforenum");
            return (Criteria) this;
        }

        public Criteria andBeforenumNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("`beforeNum` not between", value1, value2, "beforenum");
            return (Criteria) this;
        }

        public Criteria andRechargenumIsNull() {
            addCriterion("`rechargeNum` is null");
            return (Criteria) this;
        }

        public Criteria andRechargenumIsNotNull() {
            addCriterion("`rechargeNum` is not null");
            return (Criteria) this;
        }

        public Criteria andRechargenumEqualTo(BigDecimal value) {
            addCriterion("`rechargeNum` =", value, "rechargenum");
            return (Criteria) this;
        }

        public Criteria andRechargenumNotEqualTo(BigDecimal value) {
            addCriterion("`rechargeNum` <>", value, "rechargenum");
            return (Criteria) this;
        }

        public Criteria andRechargenumGreaterThan(BigDecimal value) {
            addCriterion("`rechargeNum` >", value, "rechargenum");
            return (Criteria) this;
        }

        public Criteria andRechargenumGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("`rechargeNum` >=", value, "rechargenum");
            return (Criteria) this;
        }

        public Criteria andRechargenumLessThan(BigDecimal value) {
            addCriterion("`rechargeNum` <", value, "rechargenum");
            return (Criteria) this;
        }

        public Criteria andRechargenumLessThanOrEqualTo(BigDecimal value) {
            addCriterion("`rechargeNum` <=", value, "rechargenum");
            return (Criteria) this;
        }

        public Criteria andRechargenumIn(List<BigDecimal> values) {
            addCriterion("`rechargeNum` in", values, "rechargenum");
            return (Criteria) this;
        }

        public Criteria andRechargenumNotIn(List<BigDecimal> values) {
            addCriterion("`rechargeNum` not in", values, "rechargenum");
            return (Criteria) this;
        }

        public Criteria andRechargenumBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("`rechargeNum` between", value1, value2, "rechargenum");
            return (Criteria) this;
        }

        public Criteria andRechargenumNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("`rechargeNum` not between", value1, value2, "rechargenum");
            return (Criteria) this;
        }

        public Criteria andAfternumIsNull() {
            addCriterion("`afterNum` is null");
            return (Criteria) this;
        }

        public Criteria andAfternumIsNotNull() {
            addCriterion("`afterNum` is not null");
            return (Criteria) this;
        }

        public Criteria andAfternumEqualTo(BigDecimal value) {
            addCriterion("`afterNum` =", value, "afternum");
            return (Criteria) this;
        }

        public Criteria andAfternumNotEqualTo(BigDecimal value) {
            addCriterion("`afterNum` <>", value, "afternum");
            return (Criteria) this;
        }

        public Criteria andAfternumGreaterThan(BigDecimal value) {
            addCriterion("`afterNum` >", value, "afternum");
            return (Criteria) this;
        }

        public Criteria andAfternumGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("`afterNum` >=", value, "afternum");
            return (Criteria) this;
        }

        public Criteria andAfternumLessThan(BigDecimal value) {
            addCriterion("`afterNum` <", value, "afternum");
            return (Criteria) this;
        }

        public Criteria andAfternumLessThanOrEqualTo(BigDecimal value) {
            addCriterion("`afterNum` <=", value, "afternum");
            return (Criteria) this;
        }

        public Criteria andAfternumIn(List<BigDecimal> values) {
            addCriterion("`afterNum` in", values, "afternum");
            return (Criteria) this;
        }

        public Criteria andAfternumNotIn(List<BigDecimal> values) {
            addCriterion("`afterNum` not in", values, "afternum");
            return (Criteria) this;
        }

        public Criteria andAfternumBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("`afterNum` between", value1, value2, "afternum");
            return (Criteria) this;
        }

        public Criteria andAfternumNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("`afterNum` not between", value1, value2, "afternum");
            return (Criteria) this;
        }

        public Criteria andCouponnameIsNull() {
            addCriterion("`couponName` is null");
            return (Criteria) this;
        }

        public Criteria andCouponnameIsNotNull() {
            addCriterion("`couponName` is not null");
            return (Criteria) this;
        }

        public Criteria andCouponnameEqualTo(String value) {
            addCriterion("`couponName` =", value, "couponname");
            return (Criteria) this;
        }

        public Criteria andCouponnameNotEqualTo(String value) {
            addCriterion("`couponName` <>", value, "couponname");
            return (Criteria) this;
        }

        public Criteria andCouponnameGreaterThan(String value) {
            addCriterion("`couponName` >", value, "couponname");
            return (Criteria) this;
        }

        public Criteria andCouponnameGreaterThanOrEqualTo(String value) {
            addCriterion("`couponName` >=", value, "couponname");
            return (Criteria) this;
        }

        public Criteria andCouponnameLessThan(String value) {
            addCriterion("`couponName` <", value, "couponname");
            return (Criteria) this;
        }

        public Criteria andCouponnameLessThanOrEqualTo(String value) {
            addCriterion("`couponName` <=", value, "couponname");
            return (Criteria) this;
        }

        public Criteria andCouponnameLike(String value) {
            addCriterion("`couponName` like", value, "couponname");
            return (Criteria) this;
        }

        public Criteria andCouponnameNotLike(String value) {
            addCriterion("`couponName` not like", value, "couponname");
            return (Criteria) this;
        }

        public Criteria andCouponnameIn(List<String> values) {
            addCriterion("`couponName` in", values, "couponname");
            return (Criteria) this;
        }

        public Criteria andCouponnameNotIn(List<String> values) {
            addCriterion("`couponName` not in", values, "couponname");
            return (Criteria) this;
        }

        public Criteria andCouponnameBetween(String value1, String value2) {
            addCriterion("`couponName` between", value1, value2, "couponname");
            return (Criteria) this;
        }

        public Criteria andCouponnameNotBetween(String value1, String value2) {
            addCriterion("`couponName` not between", value1, value2, "couponname");
            return (Criteria) this;
        }

        public Criteria andCreatetimeIsNull() {
            addCriterion("`createTime` is null");
            return (Criteria) this;
        }

        public Criteria andCreatetimeIsNotNull() {
            addCriterion("`createTime` is not null");
            return (Criteria) this;
        }

        public Criteria andCreatetimeEqualTo(Date value) {
            addCriterion("`createTime` =", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeNotEqualTo(Date value) {
            addCriterion("`createTime` <>", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeGreaterThan(Date value) {
            addCriterion("`createTime` >", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeGreaterThanOrEqualTo(Date value) {
            addCriterion("`createTime` >=", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeLessThan(Date value) {
            addCriterion("`createTime` <", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeLessThanOrEqualTo(Date value) {
            addCriterion("`createTime` <=", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeIn(List<Date> values) {
            addCriterion("`createTime` in", values, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeNotIn(List<Date> values) {
            addCriterion("`createTime` not in", values, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeBetween(Date value1, Date value2) {
            addCriterion("`createTime` between", value1, value2, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeNotBetween(Date value1, Date value2) {
            addCriterion("`createTime` not between", value1, value2, "createtime");
            return (Criteria) this;
        }

        public Criteria andGoodidIsNull() {
            addCriterion("`goodId` is null");
            return (Criteria) this;
        }

        public Criteria andGoodidIsNotNull() {
            addCriterion("`goodId` is not null");
            return (Criteria) this;
        }

        public Criteria andGoodidEqualTo(String value) {
            addCriterion("`goodId` =", value, "goodid");
            return (Criteria) this;
        }

        public Criteria andGoodidNotEqualTo(String value) {
            addCriterion("`goodId` <>", value, "goodid");
            return (Criteria) this;
        }

        public Criteria andGoodidGreaterThan(String value) {
            addCriterion("`goodId` >", value, "goodid");
            return (Criteria) this;
        }

        public Criteria andGoodidGreaterThanOrEqualTo(String value) {
            addCriterion("`goodId` >=", value, "goodid");
            return (Criteria) this;
        }

        public Criteria andGoodidLessThan(String value) {
            addCriterion("`goodId` <", value, "goodid");
            return (Criteria) this;
        }

        public Criteria andGoodidLessThanOrEqualTo(String value) {
            addCriterion("`goodId` <=", value, "goodid");
            return (Criteria) this;
        }

        public Criteria andGoodidLike(String value) {
            addCriterion("`goodId` like", value, "goodid");
            return (Criteria) this;
        }

        public Criteria andGoodidNotLike(String value) {
            addCriterion("`goodId` not like", value, "goodid");
            return (Criteria) this;
        }

        public Criteria andGoodidIn(List<String> values) {
            addCriterion("`goodId` in", values, "goodid");
            return (Criteria) this;
        }

        public Criteria andGoodidNotIn(List<String> values) {
            addCriterion("`goodId` not in", values, "goodid");
            return (Criteria) this;
        }

        public Criteria andGoodidBetween(String value1, String value2) {
            addCriterion("`goodId` between", value1, value2, "goodid");
            return (Criteria) this;
        }

        public Criteria andGoodidNotBetween(String value1, String value2) {
            addCriterion("`goodId` not between", value1, value2, "goodid");
            return (Criteria) this;
        }

        public Criteria andCouponidIsNull() {
            addCriterion("`couponId` is null");
            return (Criteria) this;
        }

        public Criteria andCouponidIsNotNull() {
            addCriterion("`couponId` is not null");
            return (Criteria) this;
        }

        public Criteria andCouponidEqualTo(String value) {
            addCriterion("`couponId` =", value, "couponid");
            return (Criteria) this;
        }

        public Criteria andCouponidNotEqualTo(String value) {
            addCriterion("`couponId` <>", value, "couponid");
            return (Criteria) this;
        }

        public Criteria andCouponidGreaterThan(String value) {
            addCriterion("`couponId` >", value, "couponid");
            return (Criteria) this;
        }

        public Criteria andCouponidGreaterThanOrEqualTo(String value) {
            addCriterion("`couponId` >=", value, "couponid");
            return (Criteria) this;
        }

        public Criteria andCouponidLessThan(String value) {
            addCriterion("`couponId` <", value, "couponid");
            return (Criteria) this;
        }

        public Criteria andCouponidLessThanOrEqualTo(String value) {
            addCriterion("`couponId` <=", value, "couponid");
            return (Criteria) this;
        }

        public Criteria andCouponidLike(String value) {
            addCriterion("`couponId` like", value, "couponid");
            return (Criteria) this;
        }

        public Criteria andCouponidNotLike(String value) {
            addCriterion("`couponId` not like", value, "couponid");
            return (Criteria) this;
        }

        public Criteria andCouponidIn(List<String> values) {
            addCriterion("`couponId` in", values, "couponid");
            return (Criteria) this;
        }

        public Criteria andCouponidNotIn(List<String> values) {
            addCriterion("`couponId` not in", values, "couponid");
            return (Criteria) this;
        }

        public Criteria andCouponidBetween(String value1, String value2) {
            addCriterion("`couponId` between", value1, value2, "couponid");
            return (Criteria) this;
        }

        public Criteria andCouponidNotBetween(String value1, String value2) {
            addCriterion("`couponId` not between", value1, value2, "couponid");
            return (Criteria) this;
        }

        public Criteria andSendfinishIsNull() {
            addCriterion("`sendFinish` is null");
            return (Criteria) this;
        }

        public Criteria andSendfinishIsNotNull() {
            addCriterion("`sendFinish` is not null");
            return (Criteria) this;
        }

        public Criteria andSendfinishEqualTo(Integer value) {
            addCriterion("`sendFinish` =", value, "sendfinish");
            return (Criteria) this;
        }

        public Criteria andSendfinishNotEqualTo(Integer value) {
            addCriterion("`sendFinish` <>", value, "sendfinish");
            return (Criteria) this;
        }

        public Criteria andSendfinishGreaterThan(Integer value) {
            addCriterion("`sendFinish` >", value, "sendfinish");
            return (Criteria) this;
        }

        public Criteria andSendfinishGreaterThanOrEqualTo(Integer value) {
            addCriterion("`sendFinish` >=", value, "sendfinish");
            return (Criteria) this;
        }

        public Criteria andSendfinishLessThan(Integer value) {
            addCriterion("`sendFinish` <", value, "sendfinish");
            return (Criteria) this;
        }

        public Criteria andSendfinishLessThanOrEqualTo(Integer value) {
            addCriterion("`sendFinish` <=", value, "sendfinish");
            return (Criteria) this;
        }

        public Criteria andSendfinishIn(List<Integer> values) {
            addCriterion("`sendFinish` in", values, "sendfinish");
            return (Criteria) this;
        }

        public Criteria andSendfinishNotIn(List<Integer> values) {
            addCriterion("`sendFinish` not in", values, "sendfinish");
            return (Criteria) this;
        }

        public Criteria andSendfinishBetween(Integer value1, Integer value2) {
            addCriterion("`sendFinish` between", value1, value2, "sendfinish");
            return (Criteria) this;
        }

        public Criteria andSendfinishNotBetween(Integer value1, Integer value2) {
            addCriterion("`sendFinish` not between", value1, value2, "sendfinish");
            return (Criteria) this;
        }

        public Criteria andCurrentreleasenumIsNull() {
            addCriterion("`currentReleaseNum` is null");
            return (Criteria) this;
        }

        public Criteria andCurrentreleasenumIsNotNull() {
            addCriterion("`currentReleaseNum` is not null");
            return (Criteria) this;
        }

        public Criteria andCurrentreleasenumEqualTo(Integer value) {
            addCriterion("`currentReleaseNum` =", value, "currentreleasenum");
            return (Criteria) this;
        }

        public Criteria andCurrentreleasenumNotEqualTo(Integer value) {
            addCriterion("`currentReleaseNum` <>", value, "currentreleasenum");
            return (Criteria) this;
        }

        public Criteria andCurrentreleasenumGreaterThan(Integer value) {
            addCriterion("`currentReleaseNum` >", value, "currentreleasenum");
            return (Criteria) this;
        }

        public Criteria andCurrentreleasenumGreaterThanOrEqualTo(Integer value) {
            addCriterion("`currentReleaseNum` >=", value, "currentreleasenum");
            return (Criteria) this;
        }

        public Criteria andCurrentreleasenumLessThan(Integer value) {
            addCriterion("`currentReleaseNum` <", value, "currentreleasenum");
            return (Criteria) this;
        }

        public Criteria andCurrentreleasenumLessThanOrEqualTo(Integer value) {
            addCriterion("`currentReleaseNum` <=", value, "currentreleasenum");
            return (Criteria) this;
        }

        public Criteria andCurrentreleasenumIn(List<Integer> values) {
            addCriterion("`currentReleaseNum` in", values, "currentreleasenum");
            return (Criteria) this;
        }

        public Criteria andCurrentreleasenumNotIn(List<Integer> values) {
            addCriterion("`currentReleaseNum` not in", values, "currentreleasenum");
            return (Criteria) this;
        }

        public Criteria andCurrentreleasenumBetween(Integer value1, Integer value2) {
            addCriterion("`currentReleaseNum` between", value1, value2, "currentreleasenum");
            return (Criteria) this;
        }

        public Criteria andCurrentreleasenumNotBetween(Integer value1, Integer value2) {
            addCriterion("`currentReleaseNum` not between", value1, value2, "currentreleasenum");
            return (Criteria) this;
        }

        public Criteria andNextreleasetimeIsNull() {
            addCriterion("`nextReleaseTime` is null");
            return (Criteria) this;
        }

        public Criteria andNextreleasetimeIsNotNull() {
            addCriterion("`nextReleaseTime` is not null");
            return (Criteria) this;
        }

        public Criteria andNextreleasetimeEqualTo(Date value) {
            addCriterion("`nextReleaseTime` =", value, "nextreleasetime");
            return (Criteria) this;
        }

        public Criteria andNextreleasetimeNotEqualTo(Date value) {
            addCriterion("`nextReleaseTime` <>", value, "nextreleasetime");
            return (Criteria) this;
        }

        public Criteria andNextreleasetimeGreaterThan(Date value) {
            addCriterion("`nextReleaseTime` >", value, "nextreleasetime");
            return (Criteria) this;
        }

        public Criteria andNextreleasetimeGreaterThanOrEqualTo(Date value) {
            addCriterion("`nextReleaseTime` >=", value, "nextreleasetime");
            return (Criteria) this;
        }

        public Criteria andNextreleasetimeLessThan(Date value) {
            addCriterion("`nextReleaseTime` <", value, "nextreleasetime");
            return (Criteria) this;
        }

        public Criteria andNextreleasetimeLessThanOrEqualTo(Date value) {
            addCriterion("`nextReleaseTime` <=", value, "nextreleasetime");
            return (Criteria) this;
        }

        public Criteria andNextreleasetimeIn(List<Date> values) {
            addCriterion("`nextReleaseTime` in", values, "nextreleasetime");
            return (Criteria) this;
        }

        public Criteria andNextreleasetimeNotIn(List<Date> values) {
            addCriterion("`nextReleaseTime` not in", values, "nextreleasetime");
            return (Criteria) this;
        }

        public Criteria andNextreleasetimeBetween(Date value1, Date value2) {
            addCriterion("`nextReleaseTime` between", value1, value2, "nextreleasetime");
            return (Criteria) this;
        }

        public Criteria andNextreleasetimeNotBetween(Date value1, Date value2) {
            addCriterion("`nextReleaseTime` not between", value1, value2, "nextreleasetime");
            return (Criteria) this;
        }

        public Criteria andEverytimereleasenumIsNull() {
            addCriterion("`everyTimeReleaseNum` is null");
            return (Criteria) this;
        }

        public Criteria andEverytimereleasenumIsNotNull() {
            addCriterion("`everyTimeReleaseNum` is not null");
            return (Criteria) this;
        }

        public Criteria andEverytimereleasenumEqualTo(BigDecimal value) {
            addCriterion("`everyTimeReleaseNum` =", value, "everytimereleasenum");
            return (Criteria) this;
        }

        public Criteria andEverytimereleasenumNotEqualTo(BigDecimal value) {
            addCriterion("`everyTimeReleaseNum` <>", value, "everytimereleasenum");
            return (Criteria) this;
        }

        public Criteria andEverytimereleasenumGreaterThan(BigDecimal value) {
            addCriterion("`everyTimeReleaseNum` >", value, "everytimereleasenum");
            return (Criteria) this;
        }

        public Criteria andEverytimereleasenumGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("`everyTimeReleaseNum` >=", value, "everytimereleasenum");
            return (Criteria) this;
        }

        public Criteria andEverytimereleasenumLessThan(BigDecimal value) {
            addCriterion("`everyTimeReleaseNum` <", value, "everytimereleasenum");
            return (Criteria) this;
        }

        public Criteria andEverytimereleasenumLessThanOrEqualTo(BigDecimal value) {
            addCriterion("`everyTimeReleaseNum` <=", value, "everytimereleasenum");
            return (Criteria) this;
        }

        public Criteria andEverytimereleasenumIn(List<BigDecimal> values) {
            addCriterion("`everyTimeReleaseNum` in", values, "everytimereleasenum");
            return (Criteria) this;
        }

        public Criteria andEverytimereleasenumNotIn(List<BigDecimal> values) {
            addCriterion("`everyTimeReleaseNum` not in", values, "everytimereleasenum");
            return (Criteria) this;
        }

        public Criteria andEverytimereleasenumBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("`everyTimeReleaseNum` between", value1, value2, "everytimereleasenum");
            return (Criteria) this;
        }

        public Criteria andEverytimereleasenumNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("`everyTimeReleaseNum` not between", value1, value2, "everytimereleasenum");
            return (Criteria) this;
        }
    }

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