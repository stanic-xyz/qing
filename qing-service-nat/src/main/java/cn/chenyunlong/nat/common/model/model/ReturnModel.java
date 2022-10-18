/*
 * Copyright (c) 2019-2022 YunLong Chen
 * Project Qing is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 *
 */

package cn.chenyunlong.nat.common.model.model;

/**
 * <p>
 * boolean类型返回model
 * </p>
 *
 * @author wangmin1994@qq.com
 * @since 2019-03-28 10:46:06
 */
public class ReturnModel {
    private boolean success;
    private Object data;

    private ReturnModel(boolean success, Object data) {
        this.success = success;
        this.data = data;
    }

    public ReturnModel() {
    }

    public static ReturnModel of(boolean success, Object data) {
        return new ReturnModel(success, data);
    }

    public static ReturnModel ofSuccess() {
        return new ReturnModel(true, null);
    }

    public static ReturnModel ofSuccess(Object data) {
        return new ReturnModel(true, data);
    }

    public static ReturnModel ofFail(Object data) {
        return new ReturnModel(false, data);
    }

    public boolean isSuccess() {
        return this.success;
    }

    public Object getData() {
        return this.data;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ReturnModel)) return false;
        final ReturnModel other = (ReturnModel) o;
        if (!other.canEqual((Object) this)) return false;
        if (this.isSuccess() != other.isSuccess()) return false;
        final Object this$data = this.getData();
        final Object other$data = other.getData();
        if (this$data == null ? other$data != null : !this$data.equals(other$data)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ReturnModel;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + (this.isSuccess() ? 79 : 97);
        final Object $data = this.getData();
        result = result * PRIME + ($data == null ? 43 : $data.hashCode());
        return result;
    }

    public String toString() {
        return "ReturnModel(success=" + this.isSuccess() + ", data=" + this.getData() + ")";
    }
}
