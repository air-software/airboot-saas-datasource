package com.airboot.project.monitor.controller;

import com.airboot.common.security.annotation.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.airboot.common.component.BaseController;
import com.airboot.common.model.vo.AjaxResult;
import com.airboot.common.model.vo.Server;

/**
 * 服务器监控
 *
 * @author airboot
 */
@RestController
@RequestMapping("/monitor/server")
public class ServerController extends BaseController {
    
    @PreAuthorize("monitor:server:list")
    @GetMapping()
    public AjaxResult getInfo() throws Exception {
        Server server = new Server();
        server.copyTo();
        return success(server);
    }
}
