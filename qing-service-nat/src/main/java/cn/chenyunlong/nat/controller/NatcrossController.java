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

package cn.chenyunlong.nat.controller;

import cn.chenyunlong.nat.enumeration.PortTypeEnum;
import cn.chenyunlong.nat.model.request.ListenPortParam;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import person.pluto.natcross2.serverside.listen.ListenServerControl;
import person.pluto.natcross2.serverside.listen.ServerListenThread;
import cn.chenyunlong.nat.common.model.model.ResultModel;
import cn.chenyunlong.nat.common.model.model.enumeration.ResultEnum;
import cn.chenyunlong.nat.entity.ListenPort;
import cn.chenyunlong.nat.server.FileServer;
import cn.chenyunlong.nat.server.NatcrossServer;
import cn.chenyunlong.nat.service.IListenPortService;
import cn.chenyunlong.nat.tools.ValidatorUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Stan
 */
@RestController
@RequestMapping("/natcross")
public class NatcrossController {

    private final IListenPortService listenPortService;

    private final NatcrossServer natcrossServer;

    private final FileServer fileServer;

    public NatcrossController(IListenPortService listenPortService, NatcrossServer natcrossServer, FileServer fileServer) {
        this.listenPortService = listenPortService;
        this.natcrossServer = natcrossServer;
        this.fileServer = fileServer;
    }

    /**
     * 获取可用的端口类型
     *
     * @return 响应信息
     * @author Pluto
     * @since 2020-01-10 13:57:07
     */
    @RequestMapping(value = "/usablePortTypes", method = RequestMethod.GET)
    public ResultModel usablePortTypes() {
        PortTypeEnum[] values = PortTypeEnum.values();
        return ResultModel.ofSuccess(values);
    }

    /**
     * 创建新的监听，并保存记录
     *
     * @param listenPortParam 监听端口信息
     * @return 响应结果
     */
    @PostMapping(value = "/createListenPort")
    public ResultModel createListenPort(@RequestBody ListenPortParam listenPortParam) {

        if (listenPortParam == null || listenPortParam.getListenPort() == null || listenPortParam.getDestIp() == null
                || !ValidatorUtils.isIPv4Address(listenPortParam.getDestIp()) || listenPortParam.getDestPort() == null) {
            return ResultEnum.PARAM_FAIL.toResultModel();
        }
        // 检查以前是否有设定保存
        ListenPort service = listenPortService.getByListenPort(listenPortParam.getListenPort());
        if (service != null) {
            return ResultEnum.LISTEN_PORT_HAS.toResultModel();
        }

        ListenPort listenPort = new ObjectMapper().convertValue(listenPortParam, ListenPort.class);

        listenPort.setCertPath(null);
        listenPort.setCertPassword(null);
        listenPort.setGmtCreate(null);
        listenPort.setGmtModify(null);

        boolean save = listenPortService.save(listenPort);
        if (!save) {
            return ResultEnum.SAVE_NEW_LISTEN_FAIL.toResultModel();
        }

        // 如果指定不自启动，则直接返回，不启动端口
        if (!listenPort.getOnStart()) {
            return ResultEnum.SUCCESS.toResultModel();
        }

        // 创建监听
        boolean createNewListen = natcrossServer.createNewListen(listenPort);
        if (!createNewListen) {
            return ResultEnum.CREATE_NEW_LISTEN_FAIL.toResultModel();
        }
        return ResultEnum.SUCCESS.toResultModel();
    }

    /**
     * 更新监听记录
     *
     * @param listenPortParam 更新记录
     * @author Pluto
     * @since 2019-07-22 14:20:35
     */
    @RequestMapping(value = "/updateListenPort", method = RequestMethod.POST)
    public ResultModel updateListenPort(@RequestBody ListenPortParam listenPortParam) {

        if (listenPortParam == null || listenPortParam.getListenPort() == null
                || (listenPortParam.getDestIp() != null && !ValidatorUtils.isIPv4Address(listenPortParam.getDestIp()))) {
            return ResultEnum.PARAM_FAIL.toResultModel();
        }

        ListenPort listenPort = new ObjectMapper().convertValue(listenPortParam, ListenPort.class);

        int count = listenPortService.count();
        if (count < 1) {
            return ResultEnum.LISTEN_PORT_NO_HAS.toResultModel();
        }

        listenPort.setGmtCreate(null);
        listenPort.setGmtModify(null);

        if (listenPort.getOnStart() == null) {
            listenPort.setOnStart(false);
        }

        boolean save = listenPortService.updateById(listenPort);
        if (!save) {
            return ResultEnum.SAVE_NEW_LISTEN_FAIL.toResultModel();
        }

        return ResultEnum.SUCCESS.toResultModel();
    }

    @PostMapping("uploadCertFile")
    public ResultModel uploadCertFile(@RequestParam MultipartFile certFile) {

        if (certFile != null && certFile.getSize() > 0L) {
            ResultModel resultModel = ResultEnum.SUCCESS.toResultModel();
            String saveCertFile = null;
            try {
                saveCertFile = fileServer.saveCertFile(certFile, 10086);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            resultModel.setData(saveCertFile);
            return resultModel;
        } else {
            ResultModel resultModel = ResultEnum.PARAM_FAIL.toResultModel();
            resultModel.setRetMsg("证书不能为空");
            return resultModel;
        }
    }

    /**
     * 创建新的监听
     *
     * @param listenPort 监听端口
     * @author Pluto
     * @since 2019-07-19 16:29:18
     */
    @RequestMapping(value = "createNewListen", method = RequestMethod.POST)
    public ResultModel createNewListen(@RequestParam Integer listenPort) {

        ListenPort model = listenPortService.getByListenPort(listenPort);
        boolean createNewListen;
        if (model == null) {
            createNewListen = natcrossServer.createNewListen(listenPort);
        } else {
            createNewListen = natcrossServer.createNewListen(model);
        }

        if (!createNewListen) {
            return ResultEnum.CREATE_NEW_LISTEN_FAIL.toResultModel();
        }
        return ResultEnum.SUCCESS.toResultModel();
    }

    /**
     * 停止某个端口的监听
     *
     * @param listenPort 监听端口
     * @author Pluto
     * @since 2019-07-22 15:16:26
     */
    @RequestMapping(value = "stopListen", method = RequestMethod.POST)
    public ResultModel stopListen(@RequestParam Integer listenPort) {
        natcrossServer.removeListen(listenPort);
        return ResultEnum.SUCCESS.toResultModel();
    }

    /**
     * 移除某个监听
     *
     * @param listenPort 监听端口
     * @author Pluto
     * @since 2019-07-19 16:29:26
     */
    @RequestMapping(value = "removeListen", method = RequestMethod.DELETE)
    public ResultModel removeListen(@RequestParam Integer listenPort) {
        natcrossServer.removeListen(listenPort);
        listenPortService.removeById(listenPort);
        return ResultEnum.SUCCESS.toResultModel();
    }

    /**
     * 获取全部监听信息
     *
     * @author Pluto
     * @since 2019-07-19 16:29:33
     */
    @RequestMapping(value = "getAllListenServer", method = RequestMethod.GET)
    public ResultModel getAllListenServer() {

        List<ListenPort> listenPortList = listenPortService.list();

        Set<Integer> listenPortExist = new TreeSet<>();

        for (ListenPort model : listenPortList) {
            ServerListenThread serverListenThread = ListenServerControl.get(model.getListenPort());
            model.setServerListenThread(serverListenThread);
            listenPortExist.add(model.getListenPort());
        }

        List<ServerListenThread> serverListenList = ListenServerControl.getAll();
        List<ListenPort> independentList = new LinkedList<>();

        for (ServerListenThread model : serverListenList) {
            if (!listenPortExist.contains(model.getListenPort())) {
                ListenPort listenPort = new ListenPort();
                listenPort.setListenPort(model.getListenPort());
                listenPort.setPortDescribe("临时端口");
                listenPort.setServerListenThread(model);
                independentList.add(listenPort);
            }
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("listenPortList", listenPortList);
        jsonObject.put("independentList", independentList);

        ResultModel resultModel = ResultEnum.SUCCESS.toResultModel();
        resultModel.setData(jsonObject);
        return resultModel;
    }
}
