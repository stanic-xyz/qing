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

package person.pluto.natcross2.serverside.client.adapter;

/**
 * <p>
 * 传值适配器的handler回复信息
 * </p>
 *
 * @author Pluto
 * @since 2020-01-08 16:40:54
 */
public enum PassValueNextEnum {

    // 停止并关闭
    STOP_CLOSE(false, true),
    // 停止但不关闭
    STOP_KEEP(false, false),
    // 继续执行，默认关闭
    NEXT(true, true),
    // 继续执行，但不要关闭
    NEXT_KEEP(true, false),
    //
    ;

    private boolean nextFlag;
    private boolean closeFlag;

    private PassValueNextEnum(boolean nextFlag, boolean closeFlag) {
        this.nextFlag = nextFlag;
        this.closeFlag = closeFlag;
    }

    public boolean isNextFlag() {
        return nextFlag;
    }

    public boolean isCloseFlag() {
        return closeFlag;
    }

}
